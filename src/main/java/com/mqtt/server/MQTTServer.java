/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mqtt.server;


import com.mqtt.task.ScheduleTask;
import com.mqtt.util.SysConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Simplistic telnet server.
 */
public final class MQTTServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "1883"));

    public static void main(String[] args) throws Exception {
        final SslContext sslCtx;
        SysConfig.init();
        ScheduleTask.executeTask();
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null; 
        }
        System.out.println(Runtime.getRuntime().availableProcessors());
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();//CPU����*2
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new MQTTServerInitializer(sslCtx))
            .option(ChannelOption.SO_BACKLOG, 128)//等待队列
//            .option(ChannelOption.SO_REUSEADDR, true)//表示是否允许重用服务器所绑定的地址
//            .option(ChannelOption.TCP_NODELAY, true);
            .option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);
//            .childOption(ChannelOption.SO_KEEPALIVE, true)
//            .childOption(ChannelOption.SO_LINGER, 10);
            
             // 服务器绑定端口监听  // 监听服务器关闭监听
            b.bind(PORT).sync().channel();
            b.bind(1884).sync().channel().closeFuture().sync();
            
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
