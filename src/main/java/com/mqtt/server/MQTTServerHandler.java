/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mqtt.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnAckVariableHeader;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubAckPayload;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonSyntaxException;
import com.mqtt.bean.forBusiness.MsgToNode;
import com.mqtt.bean.forBusiness.UpMessage;
import com.mqtt.bean.forStb.StbReportMsg;
import com.mqtt.server.sendMessage.SendOfflineMessageThread;
import com.mqtt.util.Constants;
import com.mqtt.util.DateUtil;
import com.mqtt.util.json.gson.GsonJsonUtil;


/**
 * @description mqtt消息处理实现类
 * @author binggu
 * @date 2017-03-03
 */
@Sharable
public class MQTTServerHandler extends SimpleChannelInboundHandler<Object>
{
    
    public static Logger log = LogManager.getLogger(MQTTServerHandler.class);
    
    private final AttributeKey<String> USER = AttributeKey.valueOf("user");
    
    public static Map<String,Long> unconnectMap=new HashMap<String, Long>();
    
    // 所有该上报的消息集合   mac+plan
    //    public static Map<Integer,Map<String, UpMessage>> upMap=new ConcurrentHashMap<Integer,Map<String, UpMessage>>();
    
    //用户数据缓存。<机顶盒号，ctx>
    public static Map<String, ChannelHandlerContext> userMap = new ConcurrentHashMap<String, ChannelHandlerContext>();
    //记载在线用户登入时间
    public static Map<String, String> userOnlineMap = new ConcurrentHashMap<String, String>();
    
    @Override
    //连接成功后调用的方法
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        // Send greeting for a new connection
        System.out.println("Welcome to " + InetAddress.getLocalHost().getHostName());
        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.writeAndFlush("It is " + new Date() + " now.\r\n");
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object request) throws Exception
    {
        try
        {
            //处理mqtt消息
            if (((MqttMessage)request).decoderResult().isSuccess())
            {
                MqttMessage req = (MqttMessage)request;
                switch (req.fixedHeader().messageType())
                {
                    case CONNECT:
                        doConnectMessage(ctx, request);
                        return;
                    case SUBSCRIBE:
                        doSubMessage(ctx, request);
                        return;
                    case PUBLISH:
                        doPublishMessage(ctx, request);
                        return;
                    case PINGREQ:
                        doPingreoMessage(ctx, request);
                        return;
                    case PUBACK:
                        doPubAck(ctx, request);
                        return;
                    case PUBREC:
                    case PUBREL:
                    case PUBCOMP:
                    case UNSUBACK:
                        
                        return;
                    case PINGRESP:
                        doPingrespMessage(ctx, request);
                        return;
                    case DISCONNECT:
                        ctx.close();
                        return;
                    default:
                        return;
                }
            }
        }
        catch (Exception ex)
        {
            
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
    {
        log.debug(ctx.channel().remoteAddress().toString().substring(1,ctx.channel().remoteAddress().toString().lastIndexOf(":")) + "is close!");
        //清理用户缓存
        if (ctx.channel().hasAttr(USER))
        {
            String user = ctx.channel().attr(USER).get();
            userMap.remove(user);
            userOnlineMap.remove(user);
        }
    }
    
    /**
     * 超时处理
     * 服务器端 设置超时 ALL_IDLE  <  READER_IDLE ， ALL_IDLE 触发时发送心跳，客户端需响应，
     * 如果客户端没有响应 说明 掉线了 ，然后触发 READER_IDLE ，
     * READER_IDLE 里 关闭链接
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent)
        {
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state().equals(IdleState.READER_IDLE))
            {
            	if (ctx.channel().hasAttr(USER)){
            		String user = ctx.channel().attr(USER).get();
            		 log.debug("ctx heartbeat timeout,close!"+user);//+ctx);
            		 log.debug("ctx heartbeat timeout,close!");//+ctx);
                     if(unconnectMap.containsKey(user))
                     {
                     	unconnectMap.put(user, unconnectMap.get(user)+1);
                     }else
                     {
                     	unconnectMap.put(user, new Long(1));
                     }
            	}
               
                ctx.fireChannelInactive();
                ctx.close();
            }else if(event.state().equals(IdleState.ALL_IDLE))
            {
            	log.debug("发送心跳给客户端！");
            	buildHearBeat(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }
    
    /**
     * 心跳响应
     * @param ctx
     * @param request
     */
    private void doPingreoMessage(ChannelHandlerContext ctx, Object request)
    {
        //MqttMessage message=(MqttMessage)request;
                System.out.println("响应心跳！");
        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessage pingespMessage = new MqttMessage(header);
        ctx.write(pingespMessage);
    }
    
    private void doPingrespMessage(ChannelHandlerContext ctx, Object request)
    {
        //       System.out.println("收到心跳请求！");
    }
    
    /**
     * 封装心跳请求
     * @param ctx
     */
        private void buildHearBeat(ChannelHandlerContext ctx)
        {
            MqttFixedHeader mqttFixedHeader=new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttMessage message=new MqttMessage(mqttFixedHeader);
            ctx.writeAndFlush(message);
        }
    /**
     * 封装发布
     * @param str
     * @param topicName
     * @return
     */
    public static MqttPublishMessage buildPublish(String str, String topicName, Integer messageId)
    {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, str.length());
        MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader(topicName, messageId);//("MQIsdp",3,false,false,false,0,false,false,60);
        ByteBuf payload = Unpooled.wrappedBuffer(str.getBytes(CharsetUtil.UTF_8));
        MqttPublishMessage msg = new MqttPublishMessage(mqttFixedHeader, variableHeader, payload);
        return msg;
    }
    
    /**
     * 处理连接请求
     * @param ctx
     * @param request
     */
    private void doConnectMessage(ChannelHandlerContext ctx, Object request)
    {
        
        MqttConnectMessage message = (MqttConnectMessage)request;
        MqttConnAckVariableHeader variableheader = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, false);
        MqttConnAckMessage connAckMessage = new MqttConnAckMessage(Constants.CONNACK_HEADER, variableheader);
        //ctx.write(MQEncoder.doEncode(ctx.alloc(),connAckMessage));
        ctx.write(connAckMessage);
        //String user = message.variableHeader().name();
        String stb_code = message.payload().clientIdentifier();
        log.debug("connect ,stb_code is :" + stb_code);
        //将用户信息写入变量
        if (!ctx.channel().hasAttr(USER))
        {
            ctx.channel().attr(USER).set(stb_code);
        }
        //将连接信息写入缓存
        userMap.put(stb_code, ctx);
        userOnlineMap.put(stb_code, DateUtil.getCurrentTimeStr());
//        log.debug("the user num is " + userMap.size());
        
        /**
         * 用户上线时，处理离线消息
         */
        for (String key : HttpServerHandler.OffLineUserMsgMap.keySet())
        {
            if (HttpServerHandler.OffLineUserMsgMap.get(key).contains(stb_code))
            {
                MsgToNode msg = HttpServerHandler.messageMap.get(key);
                SendOfflineMessageThread t = new SendOfflineMessageThread(msg, stb_code);
                HttpServerHandler.scheduledExecutorService.execute(t);
            }
        }
    }
    
    /**
     * 处理 客户端订阅消息
     * @param ctx
     * @param request
     */
    private void doSubMessage(ChannelHandlerContext ctx, Object request)
    {
        MqttSubscribeMessage message = (MqttSubscribeMessage)request;
        int msgId = message.variableHeader().messageId();
        if (msgId == -1)
            msgId = 1;
        MqttMessageIdVariableHeader header = MqttMessageIdVariableHeader.from(msgId);
        MqttSubAckPayload payload = new MqttSubAckPayload(0);
        MqttSubAckMessage suback = new MqttSubAckMessage(Constants.SUBACK_HEADER, header, payload);
        ctx.write(suback);
    }
    
    /**
     * 处理客户端回执消息
     * @param ctx
     * @param request
     */
    private void doPubAck(ChannelHandlerContext ctx, Object request)
    {
        MqttPubAckMessage message = (MqttPubAckMessage)request;
//        log.debug(request);
        /* String user = ctx.channel().attr(USER).get();
         Map<String, UpMessage> requestMap=upMap.get(message.variableHeader().messageId());
         if(requestMap!=null&&requestMap.size()>0)
         {
             UpMessage upmessage=requestMap.get(user);
             if(upmessage!=null)
             {
                 upmessage.setStatus(Constants.SENDSUCESS);
                 requestMap.put(user, upmessage);
                 upMap.put(message.variableHeader().messageId(), requestMap);
             }
         }*/
    }
    
    /**
     * 处理 客户端发布消息。此处只有终端上报的 指令消息
     * 终端上报 指令执行结果。
     * @param ctx
     * @param request
     */
    private void doPublishMessage(ChannelHandlerContext ctx, Object request)
    {
        //        long time = System.currentTimeMillis();
        MqttPublishMessage message = (MqttPublishMessage)request;
        ByteBuf buf = message.payload();
        String msg = new String(ByteBufUtil.getBytes(buf));
        log.debug("终端消息上报 start，终端编码为："+ctx.channel().attr(USER).get()+" 终端上报消息体："+msg);
        int msgId = message.variableHeader().messageId();
        if (msgId == -1)
            msgId = 1;
        //主题名
        String topicName = message.variableHeader().topicName();
        //test code
    /*    if(topicName.equals("test"))
        {
            
            MsgToNode msgs=new MsgToNode();
            MsgPublish pub=new MsgPublish();
            pub.setMqttQos(1);
            pub.setMsgPushType(1);
            pub.setMsgPushDst("111");
            msgs.setMsgPublish(pub);
            
            MsgInfo info=new MsgInfo();
            info.setMsgCode("mm123");
            msgs.setMsgInfo(info);
            SendOnlineMessageThread t = new SendOnlineMessageThread(msgs);
            HttpServerHandler.scheduledExecutorService.execute(t);
        }
        */
        try
        {
            //上报消息写入文件
            StbReportMsg stbmsg=GsonJsonUtil.fromJson(msg, StbReportMsg.class);
            //机顶盒编号||消息编号||发送状态||点击状态 ||更新时间||消息应下发用户总数
            if(!StringUtils.isEmpty(stbmsg.getMsgId()))
            {   
                UpMessage upmessage=new UpMessage();
                upmessage.setDeviceId(StringUtils.isEmpty(stbmsg.getDeviceNum())?ctx.channel().attr(USER).get():stbmsg.getDeviceNum());
                upmessage.setMsgCode(stbmsg.getMsgId());
                upmessage.setStatus(stbmsg.getStatus());
                upmessage.setIsOnclick(stbmsg.getJumpFlag());
                upmessage.setDate(UpMessage.getCurrentDate());
                upmessage.setMsgType(stbmsg.getMsgType());
                if(HttpServerHandler.messageMap.containsKey(stbmsg.getMsgId()))
                {
                    upmessage.setUserNums(HttpServerHandler.messageMap.get(stbmsg.getMsgId()).getUserNumbers());
                }
                log.debug("终端消息上报 end 终端上报消息成功。终端编号："+ctx.channel().attr(USER).get()+" 消息编码："+stbmsg.getMsgId()+"消息状态："+stbmsg.getStatus());
                HttpServerHandler.reportMsgLog.debug(upmessage.getDeviceId()+"||"+upmessage.getMsgCode()+"||"
                        +upmessage.getStatus()+"||"+upmessage.getIsOnclick()+"||"+upmessage.getDate()
                        +"||"+upmessage.getUserNums()+"||"+upmessage.getMsgType());
            }else
            {
                log.error("终端消息上报 end 终端上报消息编码为空！终端编号为: "+ctx.channel().attr(USER).get()+" 上报消息为： "+msg);
            }
        }
        catch (JsonSyntaxException e)
        {
            log.error("终端消息上报 end 终端上报消息格式错误！终端编号为: "+ctx.channel().attr(USER).get()+" 上报消息为： "+msg);
        }
        
        if (message.fixedHeader().qosLevel() == MqttQoS.AT_LEAST_ONCE)
        {
            MqttMessageIdVariableHeader header = MqttMessageIdVariableHeader.from(msgId);
            MqttPubAckMessage puback = new MqttPubAckMessage(Constants.PUBACK_HEADER, header);
            ctx.write(puback);
        }
        msg = null;
        topicName = null;
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
    
    public static Map<String, ChannelHandlerContext> getUserMap()
    {
        return userMap;
    }
    
    public static void setUserMap(Map<String, ChannelHandlerContext> userMap)
    {
        MQTTServerHandler.userMap = userMap;
    }
   
    
    public static void main(String[] args)
    {
//        String msg = "{\"deviceNum\":\"88888888\",\"jumpFlag\":0,\"msgId\":\"M20170829153611748025\",\"status\":1,\"msgType\":6}";
    	 String msg = "{\"deviceNum\":\"88888888\",\"jumpFlag\":0,\"msgId\":\"M20170829153611748025\",\"status\":1}";
    	StbReportMsg stbmsg= GsonJsonUtil.fromJson(msg, StbReportMsg.class);
    	System.out.println(stbmsg.getMsgType());
    }
}
