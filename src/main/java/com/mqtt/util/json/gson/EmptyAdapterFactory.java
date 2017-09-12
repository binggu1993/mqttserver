package com.mqtt.util.json.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * 使用Gson时候对象属性 为空（null）处理为nullDefaultString
 * @author 999
 * @date 2016-06-21
 */

public class EmptyAdapterFactory<T> implements TypeAdapterFactory {
    private String defaultNull;
    public EmptyAdapterFactory(String defaultStr)
    {
        defaultNull=defaultStr;
    }
    public EmptyAdapterFactory()
    {
        super();
    }
    @SuppressWarnings("all")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
    {
        Class<T> rawType = (Class<T>)type.getRawType();
        /* if (rawType != String.class) {
             return null;
         }*/
        
        //根据数据类型，当属性为空时填充的值。默认为空
        if (rawType == String.class)
        {
            return (TypeAdapter<T>)new StringNullAdapter(defaultNull);
        }
        else
        {
            return null;
        }
    }

    public String getDefaultNull()
    {
        return defaultNull;
    }

    public void setDefaultNull(String defaultNull)
    {
        this.defaultNull = defaultNull;
    }
    
    
}
