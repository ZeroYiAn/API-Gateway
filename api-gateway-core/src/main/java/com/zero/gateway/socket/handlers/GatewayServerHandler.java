package com.zero.gateway.socket.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zero.gateway.bind.IGenericReference;
import com.zero.gateway.session.GatewaySession;
import com.zero.gateway.session.defaults.DefaultGatewaySessionFactory;
import com.zero.gateway.socket.BaseHandler;
import com.zero.gateway.socket.agreement.RequestParser;
import com.zero.gateway.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @description: 网关网络通信服务自定义的处理器
 * 模板模式，实现父类BaseHandler 中的session方法
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */

public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(GatewayServerHandler.class);

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public GatewayServerHandler(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }


    @Override
    protected void session(ChannelHandlerContext ctx, final Channel channel, FullHttpRequest request) {
        logger.info("网关接收请求 uri：{} method：{}", request.uri(), request.method());
        // 1. 解析请求参数
        RequestParser requestParser = new RequestParser(request);
        String uri = requestParser.getUri();
        if (null == uri) return;
        Map<String, Object> args = new RequestParser(request).parse();

        // 2. 调用会话服务
        GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
        // getMapper()方法(根据映射关系)返回的是一个泛化调用服务的代理对象
        IGenericReference reference = gatewaySession.getMapper();
        //TODO 通过返沪服务代理类的对象调用RPC服务，进而调用MapperProxy#intercept()方法--->调用execute()方法-->根据请求类型，去真正地调用rpc服务-->最终泛化服务结果
        Object result = reference.$invoke(args);

        // 3. 封装返回结果
        DefaultFullHttpResponse response = new ResponseParser().parse(result);
        channel.writeAndFlush(response);
    }

}
