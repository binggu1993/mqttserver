/**
 * Copyright (c) AVIT LTD (2016). All Rights Reserved.
 * Welcome to www.avit.com.cn
 */
package com.mqtt.bean.forBusiness;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @Description:
 * @author wujie
 * @Date:2017-2-15
 * @Version: 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "MsgPublish")

public class MsgPublish implements Serializable {


	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @XmlAttribute
	private String taskTitle;
	
	@XmlAttribute
	private String taskCode;
	
	@XmlAttribute
	private int status;
	
	
	/**
	 * 0，特定用户；1，根据用户分组推送；2，根据区域推送；3，全网推送
	 */
	@XmlAttribute
	private int msgPushType;
	/**
	 * 推送目标值。推送类型为0，值为具体用户编号，用逗号隔开；类型为1，则是用户分组条件；类型为2，值为区域码，多个值用逗号隔开；3全网推送
	 */
	@XmlAttribute
	private String msgPushDst;
	
	@XmlAttribute
	private String msgPushDstDesc;

	@XmlAttribute
	private int mqttQos;
	
	@XmlAttribute
	private int msgLevel;
	
	@XmlAttribute
	private int msgStep;
	
	@XmlAttribute
	private Date beginTime;
	
	@XmlAttribute
	private String pushTime;
	
	@XmlAttribute
	private Date endTime;
	
	@XmlAttribute
	private int isRepeat;

    public String getTaskTitle()
    {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle)
    {
        this.taskTitle = taskTitle;
    }

    public String getTaskCode()
    {
        return taskCode;
    }

    public void setTaskCode(String taskCode)
    {
        this.taskCode = taskCode;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getMsgPushType()
    {
        return msgPushType;
    }

    public void setMsgPushType(int msgPushType)
    {
        this.msgPushType = msgPushType;
    }

    public String getMsgPushDst()
    {
        return msgPushDst;
    }

    public void setMsgPushDst(String msgPushDst)
    {
        this.msgPushDst = msgPushDst;
    }

    public String getMsgPushDstDesc()
    {
        return msgPushDstDesc;
    }

    public void setMsgPushDstDesc(String msgPushDstDesc)
    {
        this.msgPushDstDesc = msgPushDstDesc;
    }

    public int getMqttQos()
    {
        return mqttQos;
    }

    public void setMqttQos(int mqttQos)
    {
        this.mqttQos = mqttQos;
    }

    public int getMsgLevel()
    {
        return msgLevel;
    }

    public void setMsgLevel(int msgLevel)
    {
        this.msgLevel = msgLevel;
    }

    public int getMsgStep()
    {
        return msgStep;
    }

    public void setMsgStep(int msgStep)
    {
        this.msgStep = msgStep;
    }

    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getPushTime()
    {
        return pushTime;
    }

    public void setPushTime(String pushTime)
    {
        this.pushTime = pushTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public int getIsRepeat()
    {
        return isRepeat;
    }

    public void setIsRepeat(int isRepeat)
    {
        this.isRepeat = isRepeat;
    }
	

	
	
	
}
