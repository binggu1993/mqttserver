package com.mqtt.exception;

import java.util.Hashtable;

public enum ErrorCode {
    SUCCEED("200", "成功")
    , SERVER_EXCEPTION("101", "系统内部错误")
    , DATABASE_ACCESS_EXCEPTION("502", "数据库访问异常")
    , SERVICE_BUSINESS_EXCEPTION("503", "业务处理异常")
    , IO_EXCEPTION("504", "输入输出流异常")
    , COMPRESSION_EXCEPTION("505", "解压异常")
    //上架管理
    , CATEGORY_NULL("506", "本栏目已被删除，请刷新树")
    
    //XML解析异常
	, REQUEST_XML_NULL("102", "请求XML数据为空")
	, PARSE_XML_ERROR("103", "XML解析异常")
    ;
    
    

    public static final String NULL_ERROR_CODE = "400";
    private String code = null;
    private String message = null;
    private static Hashtable<String, ErrorCode> aliasEnums;

    ErrorCode(String code, String message) {
        this.init(code, message);
    }

    private void init(String code, String message) {
        this.code = code;
        this.message = message;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable<String, ErrorCode>();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(message, this);
    }

    public static ErrorCode valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public static ErrorCode valueOfAlias(char alias) {
        return aliasEnums.get(String.valueOf(alias));
    }
    
    public static String msgForNull(String params) {
    	return "非空属性" + params + "为空";
    }

    public String getCode() {
        return this.code;
    }

    public char getChar() {
        return this.code.charAt(0);
    }

    public String getMessage() {
        return this.message;
    }

    /**
     * 信息描述
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.getMessage() + "〔信息代码：" + this.getCode() + "〕";
    }
}
