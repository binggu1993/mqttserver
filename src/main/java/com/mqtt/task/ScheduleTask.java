package com.mqtt.task;

/**
 * 定期清理消息缓存、离线用户缓存.1天执行一次
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mqtt.util.SysConfig;


public class ScheduleTask
{
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    
    
    public static void executeTask()
    {
        Long cleanMsgPeriod = Long.parseLong(SysConfig.getSystemConfig("cleanmsgPeriod"));
        Long printBlankPeriod=Long.parseLong(SysConfig.getSystemConfig("printBlankPeriod"));

//                long oneDay = 24 * 60 * 60 * 1000;  
//                long initDelay  = getTimeMillis("00:01:01") - System.currentTimeMillis();  
        //        initDelay = initDelay > 0 ? initDelay : oneDay + initDelay; 
        executor.scheduleAtFixedRate(new CleaOutOfTimeMsgTask(), 0, cleanMsgPeriod * 24, TimeUnit.HOURS);
        executor.scheduleAtFixedRate(new PrintBlankLogTask(), 0, printBlankPeriod*60, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new PrintLocationStbInfoTask(), 0, cleanMsgPeriod*12, TimeUnit.HOURS);

    }
    
    private static long getTimeMillis(String time)
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static void main(String[] args)
    {
        getTimeMillis("00:01:01");
    }
}
