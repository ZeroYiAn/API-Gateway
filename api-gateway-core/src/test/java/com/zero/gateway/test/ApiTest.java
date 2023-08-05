package com.zero.gateway.test;

import com.zero.gateway.mapping.HttpCommandType;
import com.zero.gateway.mapping.HttpStatement;
import com.zero.gateway.session.Configuration;
import com.zero.gateway.session.defaults.DefaultGatewaySessionFactory;
import com.zero.gateway.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @description: 测试
 * @author: ZeroYiAn
 * @time: 2023/8/3
 */

public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    /**
     * 测试：
     * http://localhost:7397/wg/activity/sayHi
     * 参数：
     * {
     *     "str": "10001"
     * }
     *
     * http://localhost:7397/wg/activity/index
     * 参数：
     * {
     *     "name":"小傅哥",
     *     "uid":"10001"
     * }
     */
    @Test
    public void test_gateway() throws InterruptedException, ExecutionException {
        // 1. 创建配置信息加载注册
        // 1.1实例化Configuration，初始化dubbo配置
        Configuration configuration = new Configuration();

        //1.2创建HTTP声明对象，就是http请求的uri，以及该请求对应的rpc详情
        //实例化对象所传入的参数分别是：应用名称，服务接口，服务方法，参数类型(RPC 限定单参数注册)，网关接口(uri)，接口类型
        HttpStatement httpStatement01 = new HttpStatement(
                "api-gateway-test",
                "cn.bugstack.gateway.rpc.IActivityBooth",
                "sayHi",
                "java.lang.String",
                "/wg/activity/sayHi",
                HttpCommandType.GET);

        HttpStatement httpStatement02 = new HttpStatement(
                "api-gateway-test",
                "cn.bugstack.gateway.rpc.IActivityBooth",
                "insert",
                "cn.bugstack.gateway.rpc.dto.XReq",
                "/wg/activity/insert",
                HttpCommandType.POST);

        //1.3 将http声明信息注入Configuration
        configuration.addMapper(httpStatement01);
        configuration.addMapper(httpStatement02);

        // 2. 基于配置信息构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3. 创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory);

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            logger.info("netty server gateway start Ing ...");
            Thread.sleep(500);
        }
        logger.info("netty server gateway start Done! {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);
    }

}