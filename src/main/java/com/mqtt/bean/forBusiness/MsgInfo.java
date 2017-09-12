/**
 * Copyright (c) AVIT LTD (2016). All Rights Reserved.
 * Welcome to www.avit.com.cn
 */
package com.mqtt.bean.forBusiness;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Description:消息体对象
 * @author wujie
 * @Date:2017-2-13
 * @Version: 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "MsgInfo")
public class MsgInfo implements Serializable {

	private static final long serialVersionUID = -5467315556969590515L;

	@XmlAttribute
	private String msgTitle;

	@XmlAttribute
	private String msgCode;

	@XmlAttribute
	private String msgType;

	@XmlAttribute
	private String msgDisplay;

	@XmlAttribute
	private String msgFormat;

	@XmlAttribute
	private String msgContent;

	@XmlAttribute
	private String msgUrl;
	
	@XmlAttribute
    private String msgSource;
	
	@XmlAttribute
	private String msgFormatStr;
	
	@XmlAttribute
    private String msgDisplayStr;
	
	@XmlAttribute
    private String msgTypeStr;
	
	@XmlAttribute
    private String sourceId;
	
	@XmlAttribute
	private String htmlUrl;
	
	@XmlAttribute
	private int status;
	
    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getHtmlUrl()
    {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl)
    {
        this.htmlUrl = htmlUrl;
    }

    public String getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(String sourceId)
    {
        this.sourceId = sourceId;
    }

    public String getMsgFormatStr()
    {
        return msgFormatStr;
    }

    public void setMsgFormatStr(String msgFormatStr)
    {
        this.msgFormatStr = msgFormatStr;
    }

    public String getMsgDisplayStr()
    {
        return msgDisplayStr;
    }

    public void setMsgDisplayStr(String msgDisplayStr)
    {
        this.msgDisplayStr = msgDisplayStr;
    }

    public String getMsgTypeStr()
    {
        return msgTypeStr;
    }

    public void setMsgTypeStr(String msgTypeStr)
    {
        this.msgTypeStr = msgTypeStr;
    }

    public String getMsgTitle()
    {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle)
    {
        this.msgTitle = msgTitle;
    }

    public String getMsgCode()
    {
        return msgCode;
    }

    public void setMsgCode(String msgCode)
    {
        this.msgCode = msgCode;
    }

    public String getMsgType()
    {
        return msgType;
    }

    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }

    public String getMsgDisplay()
    {
        return msgDisplay;
    }

    public void setMsgDisplay(String msgDisplay)
    {
        this.msgDisplay = msgDisplay;
    }

    public String getMsgFormat()
    {
        return msgFormat;
    }

    public void setMsgFormat(String msgFormat)
    {
        this.msgFormat = msgFormat;
    }

    public String getMsgContent()
    {
        return msgContent;
    }

    public void setMsgContent(String msgContent)
    {
        this.msgContent = msgContent;
    }

    public String getMsgUrl()
    {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl)
    {
        this.msgUrl = msgUrl;
    }

    public String getMsgSource()
    {
        return msgSource;
    }

    public void setMsgSource(String msgSource)
    {
        this.msgSource = msgSource;
    }

}
