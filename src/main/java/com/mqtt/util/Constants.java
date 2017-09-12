package com.mqtt.util;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants
{
   
    
   /**
    * 管理系统指令下发主体
    */
    //计划指令（包括app升级、app卸载、rom升级）主题
    public static final String TOPIC_PLAN="SYSTEMPLAN";
    //实时指令（包括截图、关机、重启等）主题
    public static final String TOPIC_ORDER="SYSTEMORDER";
    
    public static final String HADOOP_COMMAS="||";

    /**
     * mqtt服务器下发给终端
     */
    //指令下发终端主题
    public static final String TOPIC_STB="SYSORDER";
    
    /**
     * 终端上报主题
     */
    //终端上报消息 主题
    public static final String TOPIC_UP="CLIREPORT";
    
    /**
     * mqtt服务器将指令执行情况上报至管理系统  主题
     */
    //指令发送成功主题
    public static final String TOPIC_ACK="SENDACK";
    //app升级回复主题
    public static final String TOPIC_UP_APPINSTALL="APPUPAGADE";
    //app卸载回复主题
    public static final String TOPIC_UP_APPUPINSTALL="APPUNINSTALL";
    //rom升级回复主题
    public static final String TOPIC_UP_ROM="SYSTEMUPGRADE";
    //截图回复主题
    public static final String TOPIC_SCREENSHOOT="SCREENSHOOT";
    
    
    public static final String FILTER="-";
    //指令发送未到达
    public static final Integer SENDFAILED=0;
    //指令发送已到达
    public static final Integer SENDSUCESS=1;
    //指令执行成功
    public static final Integer SENDACK=2;
    //按mac地址发送指令
    public static final Integer MAC_OP=0;
    //按区域发送指令
    public static final Integer AREA_OP=1;
    
    
    //指令已发送
    public static final Integer SENDSUCCESS=0;
    //指令已点击
    public static final Integer READSUCCESS=1;
    //指令已停播
    public static final Integer STOPSUCCESS=2;
    
    /**
     * 消息链接是否被点击
     */
    public static final Integer CLICKSUCCESS=1;
    
    
     // 字符集编码s
    public static final String CHAR_ENCODING = "UTF-8";
    
    /**
     * 日志构造
     */
    //开机认证（非实时）
    public static Logger registLog=LogManager.getLogger("action_000");
    //开机认证（实时）
    public static Logger realTimeRegistLog=LogManager.getLogger("real_time_action_000");
    //频道实时收视率（实时                                                                                                                                                  ）
    public static Logger realTimeTam=LogManager.getLogger("real_time_tam");
    //设备状态信息(实时)
    public static Logger stbStatusLog=LogManager.getLogger("StbStatus");
    
    //用户行为
    public static Logger powerOnLog=LogManager.getLogger("action_001");
    public static Logger tvPlayLog=LogManager.getLogger("action_003");
    public static Logger vodPlayLog=LogManager.getLogger("action_004");
    public static Logger searchLog=LogManager.getLogger("action_005");
    public static Logger appUseLog=LogManager.getLogger("action_006");
    public static Logger netLog=LogManager.getLogger("action_007");
    public static Logger favChannelLog=LogManager.getLogger("action_008");
    public static Logger orderProgramLog=LogManager.getLogger("action_009");
    public static Logger navigatStepLog=LogManager.getLogger("action_010");
    
    //故障
    public static Logger vodPlayErrorLog=LogManager.getLogger("error_110");
    public static Logger tvPlayErrorLog=LogManager.getLogger("error_111");
    public static Logger netErrorLog=LogManager.getLogger("error_112");
    public static Logger appExceptionLog=LogManager.getLogger("error_113");
    
    
    
    //固定报头
    public static MqttFixedHeader CONNACK_HEADER = new MqttFixedHeader(MqttMessageType.CONNACK, false,MqttQoS.AT_MOST_ONCE,false,0);
    public static MqttFixedHeader SUBACK_HEADER = new MqttFixedHeader(MqttMessageType.SUBACK, false,MqttQoS.AT_MOST_ONCE,false,0);
    public static MqttFixedHeader PUBACK_HEADER = new MqttFixedHeader(MqttMessageType.PUBACK, false,MqttQoS.AT_MOST_ONCE,false,0);
    public  static MqttFixedHeader PUBLISH_HEADER = new MqttFixedHeader(MqttMessageType.PUBLISH, false,MqttQoS.AT_MOST_ONCE,false,0);
    public static MqttFixedHeader UNSUBACK_HEADER = new MqttFixedHeader(MqttMessageType.UNSUBACK, false,MqttQoS.AT_MOST_ONCE,false,0);
    public static MqttFixedHeader PINGRESP_HEADER = new MqttFixedHeader(MqttMessageType.PINGRESP, false,MqttQoS.AT_MOST_ONCE,false,0);
    public static MqttFixedHeader PUBREC_HEADER = new MqttFixedHeader(MqttMessageType.PUBREC, false,MqttQoS.AT_MOST_ONCE,false,0);

    
}
