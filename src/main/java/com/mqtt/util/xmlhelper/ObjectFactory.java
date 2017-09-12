package com.mqtt.util.xmlhelper;

import javax.xml.bind.annotation.XmlRegistry;

import com.mqtt.bean.forBusiness.MsgToNode;


/**
 * 
 * @author panxincheng
 * @date 2011-7-15
 */
@XmlRegistry
public class ObjectFactory {
    public MsgToNode createMsgToNode()
    {
        return new MsgToNode();
    }
    
}
