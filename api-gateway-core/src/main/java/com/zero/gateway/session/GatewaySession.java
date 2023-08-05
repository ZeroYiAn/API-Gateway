package com.zero.gateway.session;

import com.zero.gateway.bind.IGenericReference;

import java.util.Map;

/**
 * @description: 用户处理网关 HTTP 请求
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */
public interface GatewaySession {

    Object get(String methodName, Map<String, Object> params);

    Object post(String methodName, Map<String, Object> params);

    IGenericReference getMapper();

    Configuration getConfiguration();
}