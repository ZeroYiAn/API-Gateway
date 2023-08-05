package com.zero.gateway.session.defaults;

import com.zero.gateway.bind.IGenericReference;
import com.zero.gateway.datasource.Connection;
import com.zero.gateway.datasource.DataSource;
import com.zero.gateway.executor.Executor;
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
    private Executor executor;

    public DefaultGatewaySession(Configuration configuration, String uri, Executor executor) {
        this.configuration = configuration;
        this.uri = uri;
        this.executor = executor;
    }

    /**
     * 在会话方法中调用执行器提供的方法，返回结果
     */
    @Override
    public Object get(String methodName, Map<String, Object> params) {
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        try {
            return executor.exec(httpStatement, params);
        } catch (Exception e) {
            throw new RuntimeException("Error exec get. Cause: " + e);
        }
    }

    @Override
    public Object post(String methodName, Map<String, Object> params) {
        return get(methodName, params);
    }

    /**
     * 获取泛化调用映射关系
     * @return 返回的是IGenericReference接口类型的一个代理对象
     */
    @Override
    public IGenericReference getMapper() {
        return configuration.getMapper(uri, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}