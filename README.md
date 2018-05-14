
#mqttserver，基于netty 4.1.1，可解码http、mqtt协议请求。
----
项目包括
1.基于netty绑定端口监听，对于mqtt消息和http请求消息分别绑定不同的监听端口；<br>
2.在MQTTServerInitializer中，分别添加mqtt编码解码器和http编码解码器，并分别将自定义的mqtt消息处理handle类和http消息handle类添加到信道中。添加心跳监听<br>
3.在MQTTServerHandler中实现对mqtt消息的自定义处理。该handle类中处理包含内容：<br>

对长链接通道建立clientid-channel内存缓存；<br>

客户端接入时的在线状态处理；<br>

对于不同类型的mqtt消息的分发处理；<br>

连接断开时，客户端的离线状态处理；<br>

心跳超时处理；<br>

消息发布/订阅处理；<br>

4.HttpServerHandler类实现对http消息的自定义处理。该handle类包含以下内容：<br>

对一次完整的http请求进行解码然后处理该请求。<br>

本项目采用log4j2管理日志。


