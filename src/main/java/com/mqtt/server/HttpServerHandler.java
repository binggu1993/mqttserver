package com.mqtt.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mqtt.bean.forBusiness.MsgToNode;
import com.mqtt.exception.ErrorCode;
import com.mqtt.exception.ParseMessageException;
import com.mqtt.server.sendMessage.SendOnlineMessageThread;
import com.mqtt.util.xmlhelper.Helper;

/**
 * http请求处理
 * @author binggu
 * 2017-02-22
 *
 */
public class HttpServerHandler extends  SimpleChannelInboundHandler<Object>
{
    public static Logger log = LogManager.getLogger(HttpServerHandler.class);
    
    /**
     * 上报消息写入
     */
    public static Logger reportMsgLog = LogManager.getLogger("reportMsg");
    
    /**
     * 消息集合（消息编码-消息对象）
     */
    public static Map<String, MsgToNode> messageMap = new ConcurrentHashMap<String, MsgToNode>();
    
    /**
     * 离线消息用户集合（消息编码-用户集合）
     */
    public static Map<String, Set<String>> OffLineUserMsgMap = new ConcurrentHashMap<String, Set<String>>();
    
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object request) throws Exception
    {
        // TODO Auto-generated method stu
        try
        {
            if (request instanceof FullHttpRequest)
            {
                if (((FullHttpRequest)request).decoderResult().isSuccess())
                {
                    FullHttpRequest reqs = (FullHttpRequest)request;
                    String interfaceName = reqs.uri();
                    if (interfaceName != null && interfaceName.equals("/PublishMsg"))
                    {
                        ByteBuf buf = reqs.content();
                        byte[] req = new byte[buf.readableBytes()];
                        buf.readBytes(req);
                        String xml = new String(req, "UTF-8");
                        log.debug("接收管理系统请求，开始下发指令,请求内容："+xml);
//                        System.out.println(reqs.uri());
                        String resultCode = sendMessage(xml);
                        if(resultCode!=ErrorCode.SUCCEED.getCode())
                        {
                            log.error("请求数据不正确，终止指令下发，返回错误信息给管理系统！请求内容："+xml);
                        }
                        resp(ctx, resultCode);
                    }else if (interfaceName != null && interfaceName.equals("/getOnlineUser"))
                    {
                    	log.error("业务平台请求节点服务器在线用户信息......");
//                        System.out.println(reqs.uri());
                        String resultCode = getOnlineUserInfo();
                        log.error("节点服务器当前在线用户数......"+resultCode);
                        respCount(ctx, resultCode);
                    }else
                    {
                        log.error("非指定接口请求，不予处理，本次请求uri："+reqs.uri());
                    }
                }
            }
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * 下发消息处理逻辑
     * 1.根据推送类型查询需要下发的用户集合UserList
     * 2.将消息存入 待发送消息缓存Map中。包括Key为下发编码，value为消息内容、推送qos等级
     * 3.将消息封装为盒子可识别对象，轮询节点在线用户，循环下发消息，并将已发送用户的消息放到 待上报消息缓存ReportMap，UserList移除已下发用户
     * 4.轮询完毕，UserList中剩余的用户可能是离线用户，也可能是连别的节点服务器。此时将消息Id和UserList放入待发送离线消息缓存中
     * 5.qos=1时，节点收到回执消息，更新ReportMap状态为已到达；用户上报点击消息时；更新ReportMap状态为已点击
     * 6.定时任务时间间隔为1分钟，1分钟后，取出ReportMap，将这次下发结果写入文件，供管理系统解析
     * 7.1分钟后终端上报的点击事件，写入文件，供管理系统解析
     * 8.定时清理 离线消息缓存
     */
    private String sendMessage(String xml)
    {
        String resultCode = ErrorCode.SUCCEED.getCode();
        try
        {
            MsgToNode sendMsg = (MsgToNode)Helper.getInstance().fromXML(xml);
            log.debug("校验请求数据是否正确");
            resultCode = validaRequestXml(sendMsg);
            //请求数据有误，返回错误信息，不下发给终端
            if(resultCode!=ErrorCode.SUCCEED.getCode())
            {
                return resultCode;
            }
            log.debug("校验请求数据完毕，创建指令下发线程，加入线程池处理,消息编码："+sendMsg.getMsgInfo().getMsgCode());
            SendOnlineMessageThread t = new SendOnlineMessageThread(sendMsg);
            scheduledExecutorService.execute(t);
            log.debug("指令处理完毕，返回信息给管理系统,消息编码："+sendMsg.getMsgInfo().getMsgCode());
            
        }
        catch (ParseMessageException e)
        {
            log.error("request xml 解析错误！");
            resultCode = ErrorCode.SERVER_EXCEPTION.getCode();
        }
        catch (Exception e)
        {
            log.error("未知的错误！");
            resultCode = ErrorCode.PARSE_XML_ERROR.getCode();
        }
        return resultCode;
    }
    
    /**
     * 验证xml内容
     * @param sendMsg
     */
    public String validaRequestXml(MsgToNode sendMsg)
    {
        String result = ErrorCode.SUCCEED.getCode();
        if (sendMsg.getMsgInfo() == null || sendMsg.getMsgPublish() == null)
        {
            result = ErrorCode.REQUEST_XML_NULL.getCode();
            if (sendMsg.getMsgInfo() != null)
            {
                log.error("请求msgInfo消息为空，消息编码为：" + sendMsg.getMsgInfo().getMsgCode());
            }
            else
            {
                log.error("请求msgPublish消息为空");
            }
            return result;
        }
        if (sendMsg.getMsgPublish().getMsgPushType() == 0 || sendMsg.getMsgPublish().getMsgPushType() == 1 || sendMsg.getMsgPublish().getMsgPushType() == 2)
        {
            if (StringUtils.isEmpty(sendMsg.getMsgPublish().getMsgPushDst()))
            {
                log.error("推送参数为空，消息编码为：" + sendMsg.getMsgInfo().getMsgCode());
                result = ErrorCode.REQUEST_XML_NULL.getCode();
            }
        }
        return result;
    }
    
    /** 
     *  回复客户端
     * @param xml 
     */
    private void resp(ChannelHandlerContext ctx, String code) throws Exception
    {
        String res = "";
        HttpResponseStatus status = HttpResponseStatus.OK;
        if (code.equals(ErrorCode.SUCCEED.getCode()))
        {
            res = ErrorCode.SUCCEED.getMessage();
        }
        else if (code.equals(ErrorCode.REQUEST_XML_NULL.getCode()))
        {
            res = ErrorCode.REQUEST_XML_NULL.getMessage();
        }
        else if (code.equals(ErrorCode.PARSE_XML_ERROR.getCode()))
        {
            res = ErrorCode.PARSE_XML_ERROR.getMessage();
        }
        else
        {
            res = ErrorCode.SERVER_EXCEPTION.getMessage();
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        /*if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
        }*/
        ctx.writeAndFlush(response);
    }
    
    
    /** 
     *  回复客户端在线数量
     * @param xml 
     */
    private void respCount(ChannelHandlerContext ctx, String count) throws Exception
    {
    	String res = null;
    	res = InetAddress.getLocalHost().getHostAddress()+"="+count;
        HttpResponseStatus status = HttpResponseStatus.OK;
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        /*if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
        }*/
        ctx.writeAndFlush(response);
    }
    
    
    /** 
     * 错误处理 
     * @param ctx 
     * @param status 
     */
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status)
    {
        String ret = null;
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(ret, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/xml; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    /*  class sendMessageThread extends Thread
      {
          MsgToNode msg;
          public sendMessageThread(MsgToNode message){
              this.msg= message;
          }
          @Override
          public void run()
          {
              
              MsgPublish mpg = msg.getMsgPublish();
              Set<String> userSet = null;
              *//**
                * 从数据库查询下发用户集合
                */
    /*
    UserDao userdao = new UserDao();
    userSet = userdao.getUsers(mpg.getMsgPushType(), mpg.getMsgPushDst());
    
    if (userSet != null && userSet.size() > 0)
    {
     Iterator<String> it = userSet.iterator();
     while (it.hasNext())
     {
         String stb_code = it.next();
         *//**
           * 在线用户下发消息，并写入上报文件。从待发送用户集合删除已发送用户
           */
    /*
    if (MQTTServerHandler.userMap.containsKey(stb_code))
    {
     ChannelHandlerContext ctxx = (ChannelHandlerContext)MQTTServerHandler.userMap.get(stb_code);
     if (ctxx != null && ctxx.channel().isActive())
     {
         MqttPublishMessage pubMsg;
         pubMsg = MQTTServerHandler.buildPublish(GsonJsonUtil.toJson(msg.getMsgInfo()), Constants.TOPIC_STB, 1);
         ReferenceCountUtil.retain(pubMsg);
         ctxx.writeAndFlush(pubMsg);
         reportMsgLog.debug(stb_code + "||" + msg.getMsgInfo().getMsgCode() + "||1||" + DateUtil.getCurrentTimeStr());
         it.remove();
     }
    }
    
    }
    
    *//**
      * 有用户没有下发，统一当离线用户处理
      */
    /*
    if (userSet != null & userSet.size() > 0)
    {
     msg.setEditTime(new Date());
     messageMap.put(msg.getMsgInfo().getMsgCode(), msg);
     if(OffLineUserMsgMap.containsKey(msg.getMsgInfo().getMsgCode()))
     {
         Set<String> oldusers=OffLineUserMsgMap.get(msg.getMsgInfo().getMsgCode());
         userSet.addAll(oldusers);
         OffLineUserMsgMap.put(msg.getMsgInfo().getMsgCode(),userSet);
     }
    }
    }
    
    }

    public void sendMessage(String code, int msgCode,int op,String planId,String str,int requestId)
    {
    
    
    }
    
    }*/
    
    /**
     * 当前服务器在线用户数
     * @return
     */
    private String getOnlineUserInfo(){
    	return MQTTServerHandler.userOnlineMap.size()+"";
    }
}
