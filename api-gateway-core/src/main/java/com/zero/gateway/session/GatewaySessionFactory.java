package com.zero.gateway.session;

/**
 * @description: 网关会话工厂接口
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */
public interface GatewaySessionFactory {
    GatewaySession openSession(String uri);
}
