package com.zero.gateway.bind;

import com.zero.gateway.mapping.HttpStatement;
import com.zero.gateway.session.Configuration;
import com.zero.gateway.session.GatewaySession;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 泛化调用注册器
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */

public class MapperRegistry {

    private final Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    // 泛化调用静态代理工厂
    private final Map<String, MapperProxyFactory> knownMappers = new HashMap<>();

    /**
     * 获取泛化调用的映射关系
     * @param uri
     * @param gatewaySession
     * @return
     */
    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        final MapperProxyFactory mapperProxyFactory = knownMappers.get(uri);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Uri " + uri + " is not known to the MapperRegistry.");
        }
        try {
            //TODO 这里使用cglib创建了泛化调用服务的代理对象
            return mapperProxyFactory.newInstance(gatewaySession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public void addMapper(HttpStatement httpStatement) {
        String uri = httpStatement.getUri();
        // 如果重复注册则报错
        if (hasMapper(uri)) {
            throw new RuntimeException("Uri " + uri + " is already known to the MapperRegistry.");
        }
       //TODO 这里根据uri获取MapperProxyFactory对象
        knownMappers.put(uri, new MapperProxyFactory(uri));
        // 保存接口映射信息
        configuration.addHttpStatement(httpStatement);
    }

    public <T> boolean hasMapper(String uri) {
        return knownMappers.containsKey(uri);
    }

}
