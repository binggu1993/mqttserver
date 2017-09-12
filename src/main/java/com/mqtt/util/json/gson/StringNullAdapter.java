package com.mqtt.util.json.gson;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Gjson对象序列化时 为空属性是否转换以及转换的值
 * @author 999
 * @date 2016-06-21
 */
public class StringNullAdapter extends TypeAdapter<String> {
    
    private String nullDefaultString;
    public StringNullAdapter(String str)
    {
        nullDefaultString=str;
    }
    public StringNullAdapter()
    {
        super();
    }
    @Override
    public String read(JsonReader reader) throws IOException
    {
        if (reader.peek() == JsonToken.NULL)
        {
            //            reader.nextNull();
            if (StringUtils.isEmpty(nullDefaultString))
            {
                return "";
            }
            else
            {
                return nullDefaultString;
            }
        }
        return reader.nextString();
    }
    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        if (value == null) {
//            writer.nullValue();
            if (StringUtils.isEmpty(nullDefaultString))
            {
                writer.nullValue();
            }else
            {
                writer.value(nullDefaultString);
            }
            return;
        }
        writer.value(value);
    }
    public String getNullDefaultString()
    {
        return nullDefaultString;
    }
    public void setNullDefaultString(String nullDefaultString)
    {
        this.nullDefaultString = nullDefaultString;
    }
    
    
}
