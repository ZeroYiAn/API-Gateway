package com.zero.gateway.socket;

import com.zero.gateway.session.defaults.DefaultGatewaySessionFactory;
import com.zero.gateway.socket.handlers.GatewayServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @description: 网关channel管道初始化类
 * SessionChannelInitializer类中包装了HTTP Get/Post协议的解析处理，同时包括了我们自己要完成的网络请求，
 * 也就是SessionServerHandler部分的内容。
 * @author: ZeroYiAn
 * @time: 2023/8/3
 */

public class GatewayChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public GatewayChannelInitializer(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        /*
        给Channel添加处理器：
        Netty 的通信会为每个连接上来的用户建立一条Channle管道(对应的ChannellD 唯一)，并在管道中插入一道道板子，
        这些板子可以是：编码器、解码器、流量整形、SSL、自定义服务处理等各类模块。通过这样的方式，让我们可以扩展各类功能。
        对应的也就是接口Channellnitializer的实现类SessionChannelInitializer所完成的事情。
         */
        ChannelPipeline line = channel.pipeline();
        // HttpRequestDecoder：用于解析 HTTP 协议格式
        line.addLast(new HttpRequestDecoder());
        // HttpResponseEncoder：对HTTP响应报文进行加密
        line.addLast(new HttpResponseEncoder());

        //HttpObjectAggregator用于处理除了Get请求外的POST请求时候的对象信息
        //HTTP 消息聚合器，处理POST请求，通过它可以把 HttpMessage 和 HttpContent 聚合成一个 FullHttpRequest 或者 FullHttpResponse
        line.addLast(new HttpObjectAggregator(1024 * 1024));
        //自定义的HTTP协议请求处理器
        line.addLast(new GatewayServerHandler(gatewaySessionFactory));
    }

}
