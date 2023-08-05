package com.zero.gateway.executor;

import com.zero.gateway.executor.result.GatewayResult;
import com.zero.gateway.mapping.HttpStatement;

import java.util.Map;

/**
 * @description: 执行器接口  ，所有会话请求都通过执行器完成
 * @author: ZeroYiAn
 * @time: 2023/8/5
 */
public interface Executor {

    GatewayResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;

}