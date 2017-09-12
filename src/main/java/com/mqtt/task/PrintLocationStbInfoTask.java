/**
 * 
 */
package com.mqtt.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mqtt.server.MQTTServerHandler;
import com.mqtt.util.DateUtil;


/**
 * 打印本地连接终端信息
 * @author Administrator
 *
 */
public class PrintLocationStbInfoTask implements Runnable {
	
	public static Logger log = LogManager.getLogger(PrintLocationStbInfoTask.class);

	@Override
	public void run() {
		
		log.debug("开始记录本地链接上的终端信息。。。。");
		printConnectStbInfo();
		log.debug("开始记录本地链接断开的终端信息。。。。");
		printDisconnectStbInfo();
	}
	
	
	/**
	 * 打印断开连接的终端信息
	 */
	private void  printDisconnectStbInfo(){
		log.debug("定时任务printDisconnectStbInfo记录断连接的机顶盒信息...");
		File file = new File("D:/usr/local/mqtt/disconnectUsers_"+DateUtil.getCurrentDateStr1()+".txt");
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			if (MQTTServerHandler.unconnectMap.size() > 0) {
				String line = "";
				for (String key : MQTTServerHandler.unconnectMap.keySet()) {
					line = key + ","
							+ MQTTServerHandler.unconnectMap.get(key);
					
					writer.write(line);
					writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 打印服务器连接的终端信息
	 */
	private void  printConnectStbInfo(){
		log.debug("定时任务printConnectStbInfo记录连接的机顶盒信息...");
		File file = new File("D:/usr/local/mqtt/connectUsers_"+DateUtil.getCurrentDateStr1()+".txt");
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			if (MQTTServerHandler.userOnlineMap.size() > 0) {
				String line = "";
				for (String key : MQTTServerHandler.userOnlineMap.keySet()) {
					line = key + ","
							+ MQTTServerHandler.userOnlineMap.get(key);
					writer.write(line);
					writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
