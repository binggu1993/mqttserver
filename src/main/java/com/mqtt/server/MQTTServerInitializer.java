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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class MQTTServerInitializer extends ChannelInitializer<SocketChannel> {

//    private static final StringDecoder DECODER = new StringDecoder();
//    private static final StringEncoder ENCODER = new StringEncoder();

    private static final MQTTServerHandler SERVER_HANDLER = new MQTTServerHandler();
    private final SslContext sslCtx;

    public MQTTServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception
    {
        
        
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null)
        {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        System.out.println(ch.localAddress().getPort());
        if(ch.localAddress().getPort()==1883)
        {/*
             //mqtt消息解码、编码器
            pipeline.addLast(new MqttDecoder(81920));
            pipeline.addLast("timeout", new IdleStateHandler(0, 0, 20, TimeUnit.SECONDS));
            //自定义mqtt消息业务处理
            pipeline.addLast(SERVER_HANDLER);
            pipeline.addLast(MqttEncoder.INSTANCE);
        */
            
            pipeline.addLast(new MqttDecoder(81920));
            pipeline.addLast(MqttEncoder.INSTANCE);
            pipeline.addLast("timeout", new IdleStateHandler(30, 0, 20,
                    TimeUnit.SECONDS));      
            pipeline.addLast(SERVER_HANDLER);
        
        }else
        {
          //http请求消息解码、编码器，并将http 分段请求消息整合成 FullHttpRequest
            pipeline.addLast(new HttpRequestDecoder());
            pipeline.addLast(new HttpObjectAggregator(65536));
            pipeline.addLast(new HttpResponseEncoder());
            //自定义http请求消息业务处理
            pipeline.addLast(new HttpServerHandler());
           
        }
        
        
       
        
    }

}
