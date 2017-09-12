/**   
* 
*/
package com.mqtt.util.xmlhelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;



/** 
 * @ClassName: DateAdapter 
 * @Description: TODO
 * @author rengm 
 * @date 2013-9-23 下午05:44:25 
 * @version V1.0  
 */
public class DateAdapter extends XmlAdapter<String, Date> {
	private String pattern = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat fmt = new SimpleDateFormat(pattern);
     
    @Override
    public Date unmarshal(String dateStr) throws Exception {
    	if (StringUtils.isNotEmpty(dateStr)) {
    		return fmt.parse(dateStr);
		} else {
			return null;
		}
    }
 
    @Override
    public String marshal(Date date) throws Exception {
        if (date != null) {
        	return fmt.format(date);
		} else {
			return "";
		}
    }
}
