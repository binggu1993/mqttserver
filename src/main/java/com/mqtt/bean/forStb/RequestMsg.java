package com.mqtt.bean.forStb;

/**
 * 消息下发给终端对象
 * @author binggu
 *
 */
public class RequestMsg
{
    
   /* *//**
     * 消息下发编号
     *//*
    public Integer requestId;*/
    /**
     * 消息类型
     */
    public String msgType;
    /**
     * 消息编号
     */
    public Integer msgCode ;
    
    /**
     * 消息标题
     */
    public String msgTitle;
    
    /**
     * 消息展现样式
     */
    public String  msgDisplay;
    
    /**
     * 消息内容格式
     */
    public String  msgFormat;
    
    
    /**
     * 消息内容
     */
    public String  msgContent;
    
    /**
     * 文件路径
     */
    public String  msgUrl;

    public String getMsgType()
    {
        return msgType;
    }

    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }

    public Integer getMsgCode()
    {
        return msgCode;
    }

    public void setMsgCode(Integer msgCode)
    {
        this.msgCode = msgCode;
    }

    public String getMsgTitle()
    {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle)
    {
        this.msgTitle = msgTitle;
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
    
    
    
    
}
