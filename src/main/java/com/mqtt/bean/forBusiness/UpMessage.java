package com.mqtt.bean.forBusiness;

import com.mqtt.util.DateUtil;




/**
 * 上报消息给业务管理系统 对象
 * @author binggu
 *
 */
public class UpMessage
{
    /**
     * 设备编码（机顶盒号）
     */
    private String deviceId;
    /**
     * 消息编码
     */
    private String msgCode;
    /**
     * 0-已发送，1-已查看 ,2.已停播
     */
    private Integer status=0;
    /**
     * 消息链接是否已点击。0未点击，1已点击。默认为0
     */
    private Integer isOnclick=0;
    
    /**
     * 该条消息应发送总数
     */
    private Integer userNums=0;
    
    /**
     * 该消息类型
     */
    private String msgType;
    
    public Integer getUserNums()
    {
        return userNums;
    }
    public void setUserNums(Integer userNums)
    {
        this.userNums = userNums;
    }

    /**
     * 上报时间
     */
    private String date;
    
    public String getDeviceId()
    {
        return deviceId;
    }
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }
    public Integer getIsOnclick()
    {
        return isOnclick;
    }
    public void setIsOnclick(Integer isOnclick)
    {
        this.isOnclick = isOnclick;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public String getMsgCode()
    {
        return msgCode;
    }
    public void setMsgCode(String msgCode)
    {
        this.msgCode = msgCode;
    }
    public Integer getStatus()
    {
        return status;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public  static String   getCurrentDate()
    {
      return   DateUtil.getCurrentTimeStr();
    }
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
    
    
}
