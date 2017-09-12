package com.mqtt.util.json.gson;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
/**
 * 
 * @author binggu
 * @date 2016-07-21
 */
@SuppressWarnings("all")
public class GsonJsonUtil
{
    private static Gson gson;
    public static Logger log = LogManager.getLogger(GsonJsonUtil.class);
    
   /**
    * 默认初始化方法
    * gson对象不为空时初始化gson对象。只维持一个gson对象。参数值：
    * @param isSerializeNulls  false
    * @param excludesFieldsWithoutExpose  false
    * @param datePattern  null
    * @param SerializeNullStr  null
    */
    public static void initGsonUtil()
    {
        initGsonUtil(false, false, null, null);
    }
    /**
     * 初始化gson对象
     * @param isSerializeNulls 是否序列化 null值字段
     * @param excludesFieldsWithoutExpose 是否排除 未标注 {@literal @Expose} 注解的字段
     * @param datePattern 日期格式
     * @param SerializeNullStr 设置Sting属性 为null时的序列化默认值
     */
    public static void initGsonUtil(boolean isSerializeNulls, boolean excludesFieldsWithoutExpose, String datePattern, String SerializeNullStr)
    {
        if (gson == null)
        {
            initGson(isSerializeNulls, excludesFieldsWithoutExpose, datePattern, SerializeNullStr);
        }
    }
    
    /**
     * 初始化Gson对象
     * @param isSerializeNulls
     * @param excludesFieldsWithoutExpose
     * @param datePattern
     * @param SerializeNullStr
     */
    private static synchronized void initGson(boolean isSerializeNulls, boolean excludesFieldsWithoutExpose, String datePattern, String SerializeNullStr)
    {
      
        if (gson != null)
        {
            return;
        }
        System.out.println("初始化!");
        GsonBuilder builder = new GsonBuilder();
        if (!StringUtils.isEmpty(SerializeNullStr))
        {
            builder.registerTypeAdapterFactory(new EmptyAdapterFactory(SerializeNullStr));
        }
        if (isSerializeNulls)
        {
            builder.serializeNulls();
        }
        if (excludesFieldsWithoutExpose)
        {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        if (!StringUtils.isEmpty(datePattern))
        {
            builder.setDateFormat(datePattern);
        }
        gson = builder.create();
    }
    
    /* private static final Gson gson = new GsonBuilder().registerTypeAdapterFactory(
             new NullStringToEmptyAdapterFactory()).create();*/
    /**
     * 对象转换为json（null属性拼装为字符串"null"）
     * @param obj
     * @return
     */
    public static String toJson(Object obj)
    {
        initGsonUtil();
        String json = "";
        try
        {
            json = gson.toJson(obj);
        }
        catch (Exception e)
        {
        }
        return json;
    }
    
    /**
     * json转换为对象
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz)
    {
        initGsonUtil();
        T jsonObj = null;
        try
        {
            jsonObj = gson.fromJson(json, clazz);
        }
        catch (JsonSyntaxException e)
        {
            e.printStackTrace();
        }
        return jsonObj;
    }
    
}
