package com.mqtt.util.xmlhelper;

import java.awt.geom.Area;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mqtt.exception.ParseMessageException;




/**
 * @author panxincheng
 * @date 2011-7-15
 */
public class Helper {
	
	public final static String ENCODING = "UTF-8";
    public static Logger log = LogManager.getLogger(Helper.class);
	private static Helper instance = null;
	private static String packagePath;
	JAXBContext jaxbContext = null;
	//Marshaller marshaller = null;
	//Unmarshaller unmarshaller = null;

	private Helper() {
		super();
		packagePath=this.getClass().getPackage().getName();
	}
	
	public static synchronized Helper getInstance() {
		if (instance == null) {
			instance = new Helper();
			instance.prepareJaxbContext();
		}
		return instance;
	}
	
	public synchronized void prepareJaxbContext() {
		try {
			jaxbContext = JAXBContext.newInstance(packagePath);
			// Class Marshaller controls the process of marshalling i.e: Java
			// Object --> XML
			//marshaller = jaxbContext.createMarshaller();
			// Class UnMarshaller controls the process of unmarshalling i.e: XML
			// --> Java Object
			//unmarshaller = jaxbContext.createUnmarshaller();
		} catch (Exception e) {
			log.error("prepareJaxbContext",e);
		}
	}
	
    public String toXML(Object obj)
    {
        String docu = null;
        StringWriter sw = null;
        
        try
        {
            Marshaller marshaller = jaxbContext.createMarshaller();
            sw = new StringWriter();
            marshaller.marshal(obj, sw);
            docu = sw.toString();
        }
        catch (Exception e)
        {
            log.error("object to xml exception", e);
        }
        finally
        {
            if (sw != null)
            {
                try
                {
                    sw.close();
                }
                catch (IOException e)
                {
                    sw = null;
                    log.error("", e);
                }
            }
        }
        
        return docu;
    }
	
    public Object fromXML(String xml) throws ParseMessageException
    {
        if (xml == null)
        {
            return null;
        }
        ByteArrayInputStream bais = null;
        Object obj = null;
        
        try
        {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            bais = new ByteArrayInputStream(replaceUnicodeString(xml).getBytes(ENCODING));
            obj = unmarshaller.unmarshal(bais);
        }
        catch (Exception e)
        {
            log.error("xml to object exception", e);
            throw new ParseMessageException(e);
        }
        finally
        {
            if (bais != null)
            {
                try
                {
                    bais.close();
                }
                catch (IOException e)
                {
                    bais = null;
                }
            }
        }
        
        return obj;
    }
	
	
	
	/**
	 * @Description 节点同名，调用此方法做指定转换
	 * @param xml
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    public static <T> T XML2Obj(String xml, Class<T> clazz) throws Exception 
	{
        if(xml.isEmpty() || clazz == null)
            throw new Exception(">>>>>>>>>>传入的把XML转化为对象的参数不对  obj =" + clazz +"; xml "+xml);
        T t = null;
        StringReader reader = null;
        JAXBContext jaxbContext = null;
        Unmarshaller unmarshaller = null;
        try
        {
            jaxbContext = JAXBContext.newInstance(clazz);
            unmarshaller = jaxbContext.createUnmarshaller();
            reader = new StringReader(xml);
            t = (T)unmarshaller.unmarshal(reader);
        }
        catch (Exception e)
        {
            throw new Exception(">>>>>>>>>>>>>XML转化为对象[" + clazz.getName() + "]错误",e);
        }
        finally
        {
            if(reader != null)
            {
                reader.close();
            }
        }
        return t;
    }

	
	public String replaceUnicodeString(String str) {
        return str.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");  
    }
	
	
	public static void main(String[] args)
    {
	    Area area=new Area();
	    System.out.println(Helper.getInstance().toXML(area));;
    }
}
