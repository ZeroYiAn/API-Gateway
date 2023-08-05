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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description: 测试
 * @author: ZeroYiAn
 * @time: 2023/8/3
 */

public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    /**
     * 测试：http://localhost:7397/wg/activity/sayHi
     */
    @Test
    public void test_gateway() throws InterruptedException, ExecutionException {
        //调用流程
        // 1. 创建配置信息加载注册  实例化Configuration配置，加载应用配置、HTTP声明（目前是硬编码方式）
        Configuration configuration = new Configuration();
        HttpStatement httpStatement = new HttpStatement(
                "api-gateway-test",
                "cn.bugstack.gateway.rpc.IActivityBooth",
                "sayHi",
                "/wg/activity/sayHi",
                HttpCommandType.GET);
        configuration.addMapper(httpStatement);

        // 2. 基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3. 创建网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory);
        // 4. 启动网关网络服务
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            logger.info("netty server gateway is starting ...");
            Thread.sleep(500);
        }
        logger.info("netty server gateway start Done! {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);
    }

}