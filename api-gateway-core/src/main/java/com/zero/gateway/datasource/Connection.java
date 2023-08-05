package com.zero.gateway.datasource;

/**
 * @description: 连接接口
 * @author: ZeroYiAn
 * @time: 2023/8/5
 */
public interface Connection {

    Object execute(String method, String[] parameterTypes, String[] parameterNames, Object[] args);

}
