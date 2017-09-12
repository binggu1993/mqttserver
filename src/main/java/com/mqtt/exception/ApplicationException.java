/**   
 * 
 * @Title: ApplicationException.java 
 * @Package com.avit.portal.common.exception
 * @Description:  ApplicationException 公共错误类
 * @author  zhayong   
 * @email   zhayong@intranet.com  
 * @date 2012-5-16 上午10:56:28 
 * @version V1.0
 * Copyright (c) 2012 AVIT Company,Inc. All Rights Reserved.
 * 
 */
package com.mqtt.exception;

/**
 * @ClassName: ApplicationException
 * @Description: ApplicationException 公共错误类
 * 
 * @author zhayong
 * @email zhayong@intranet.com
 * @date 2012-5-18 下午04:58:00
 * 
 */
public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 3184998542145188928L;

	/**
	 * 错误码
	 */
	private int code;

	/**
	 * 错误消息
	 */
	private String codeMessage;

	public ApplicationException() {
	}

	public ApplicationException(String message) {
		super(message);
		this.codeMessage = message;
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
		this.codeMessage = message;
	}

	public ApplicationException(int code, String codeMessage) {
		super(codeMessage);
		this.code = code;
		this.codeMessage = codeMessage;
	}

	public ApplicationException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public ApplicationException(int code, String codeMessage, Throwable cause) {
		super(codeMessage, cause);
		this.code = code;
		this.codeMessage = codeMessage;
	}
	
	public ApplicationException(ErrorCode ep){
		this(Integer.parseInt(ep.getCode()), ep.getMessage());
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCodeMessage() {
		return this.codeMessage;
	}

	public void setCodeMessage(String codeMessage) {
		this.codeMessage = codeMessage;
	}
}