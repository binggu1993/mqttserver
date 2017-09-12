package com.mqtt.bean.forBusiness;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "UserInfo")
public class UserInfo implements Serializable{

	private static final long serialVersionUID = -7860734163954601704L;


    @XmlAttribute
    private String userCode;        //用户编号

    @XmlAttribute
    private String userName;        //用户名称
    
    @XmlAttribute
    private String stbCode;        //机顶盒号
    
    @XmlAttribute
    private String cardCode;        //智能卡号

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getStbCode()
    {
        return stbCode;
    }

    public void setStbCode(String stbCode)
    {
        this.stbCode = stbCode;
    }

    public String getCardCode()
    {
        return cardCode;
    }

    public void setCardCode(String cardCode)
    {
        this.cardCode = cardCode;
    }

   
}
