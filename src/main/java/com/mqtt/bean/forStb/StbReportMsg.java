package com.mqtt.bean.forStb;

import java.io.Serializable;


/**
 * 终端上报消息对象
 * @author binggu
 * 2017-03-02
 *
 */
public class StbReportMsg implements Serializable{
    private static final long serialVersionUID = -4459706208756358362L;

    public String msgId;    // -1 or 0. device info ?
    public String deviceNum;
    public Integer status; //已查看；已停播
    public Integer jumpFlag;//链接已点击。默认为0（未点击），1为已点击
    public String msgType;//消息类型
    public StbReportMsg(String deviceNum, String macAddr, int status, int jumpFlag,String msgType) {
        this.deviceNum = deviceNum;
        this.msgId = macAddr;
        this.status = status;
        this.jumpFlag = jumpFlag;
        this.msgType = msgType;
    }
//    public StbReportMsg(String deviceNum, String macAddr, int status, int jumpFlag) {
//        this.deviceNum = deviceNum;
//        this.msgId = macAddr;
//        this.status = status;
//        this.jumpFlag = jumpFlag;
//    }
    public StbReportMsg()
    {
        super();
    }
    public String getMsgId()
    {
        return msgId;
    }
    public void setMsgId(String msgId)
    {
        this.msgId = msgId;
    }
    public String getDeviceNum()
    {
        return deviceNum;
    }
    public void setDeviceNum(String deviceNum)
    {
        this.deviceNum = deviceNum;
    }
    public Integer getStatus()
    {
        return status;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    public Integer getJumpFlag()
    {
        return jumpFlag;
    }
    public void setJumpFlag(Integer jumpFlag)
    {
        this.jumpFlag = jumpFlag;
    }
    public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
}
