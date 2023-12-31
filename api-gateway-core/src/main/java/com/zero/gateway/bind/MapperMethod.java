package com.zero.gateway.bind;

import com.zero.gateway.mapping.HttpCommandType;
import com.zero.gateway.session.Configuration;
import com.zero.gateway.session.GatewaySession;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description:  映射绑定调用方法
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */

public class MapperMethod {

    private String uri;
    /**
     * http请求类型：get、post、put、delete
     * HTTP 调用指令
     */
    private final HttpCommandType command;

    private String methodName;

    public MapperMethod(String uri, Method method, Configuration configuration) {
        this.methodName = configuration.getHttpStatement(uri).getMethodName();
        this.command = configuration.getHttpStatement(uri).getHttpCommandType();
    }

    /**
     * 基于网关接口注册时的方法，get、post、put、delete做不同的逻辑处理
     */
    public Object execute(GatewaySession session, Map<String, Object> params) {
        Object result = null;
        switch (command) {
            case GET:
                result = session.get(methodName, params);
                break;
            case POST:
                result = session.post(methodName, params);
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command);
        }
        return result;
    }


}
