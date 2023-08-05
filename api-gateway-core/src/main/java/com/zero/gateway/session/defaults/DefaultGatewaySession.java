package com.zero.gateway.session.defaults;

import com.zero.gateway.bind.IGenericReference;
import com.zero.gateway.datasource.Connection;
import com.zero.gateway.datasource.DataSource;
import com.zero.gateway.mapping.HttpStatement;
import com.zero.gateway.session.Configuration;
import com.zero.gateway.session.GatewaySession;
import com.zero.gateway.type.SimpleTypeRegistry;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;

/**
 * @description: 默认网关会话实现类
 * 通过工厂实例化，用于获取映射器，并构建doubbo服务，真正执行RPC的调用
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */

public class DefaultGatewaySession implements GatewaySession {

    private Configuration configuration;
    private String uri;
    private DataSource dataSource;

    public DefaultGatewaySession(Configuration configuration, String uri, DataSource dataSource) {
        this.configuration = configuration;
        this.uri = uri;
        this.dataSource = dataSource;
    }

    @Override
    public Object get(String methodName, Map<String, Object> params) {
        Connection connection = dataSource.getConnection();
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        String parameterType = httpStatement.getParameterType();

        /*
         * 调用服务
         * 封装参数 PS：为什么这样构建参数，可以参考测试案例；cn.bugstack.gateway.test.RPCTest
         * 01(允许)：java.lang.String
         * 02(允许)：cn.bugstack.gateway.rpc.dto.XReq
         * 03(拒绝)：java.lang.String, cn.bugstack.gateway.rpc.dto.XReq —— 不提供多参数方法的处理
         * */
        return connection.execute(methodName,
                new String[]{parameterType},
                new String[]{"ignore"},
                SimpleTypeRegistry.isSimpleType(parameterType) ? params.values().toArray() : new Object[]{params});
    }

    @Override
    public Object post(String methodName, Map<String, Object> params) {
        return get(methodName, params);
    }


    @Override
    public IGenericReference getMapper() {
        return configuration.getMapper(uri, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}