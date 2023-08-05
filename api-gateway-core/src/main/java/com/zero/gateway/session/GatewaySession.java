package com.zero.gateway.session;

import com.zero.gateway.bind.IGenericReference;

/**
 * @description: 用户处理网关 HTTP 请求
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */
public interface GatewaySession {

    Object get(String uri, Object parameter);

    IGenericReference getMapper();

    Configuration getConfiguration();

}