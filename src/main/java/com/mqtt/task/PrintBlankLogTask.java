package com.mqtt.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mqtt.server.HttpServerHandler;


/**
 * 0点一过打印空白日志，以供切割给hadoop使用
 * @author wcs
 *
 */
public class PrintBlankLogTask implements Runnable
 {
	public static Logger log = LogManager.getLogger(PrintBlankLogTask.class);

	@Override
	public void run() {
		HttpServerHandler.reportMsgLog.debug("");
		log.debug("定时任务向日志文件中打印空行，方便日志切换...");
	}

}
