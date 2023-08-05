package com.zero.gateway.bind;

import com.zero.gateway.session.GatewaySession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description: 映射代理调用：只完成代理部分，并调用映射器方法完成逻辑处理
 *
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */

public class MapperProxy implements MethodInterceptor {

    private GatewaySession gatewaySession;
    private final String uri;

    public MapperProxy(GatewaySession gatewaySession, String uri) {
        this.gatewaySession = gatewaySession;
        this.uri = uri;
    }

    /**
     * 自定义类实现MethodInterceptor接口的intercet方法
     * 在方法中调用目标对象的方法，并做功能增强
     * 类似JDK动态代理的InvocationHandler的实现类
     *
     * @param obj     代理对象
     * @param method  方法
     * @param args    参数
     * @param proxy   方法代理对象，通过此对象可以调用代理对象的方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        MapperMethod linkMethod = new MapperMethod(uri, method, gatewaySession.getConfiguration());
        // 暂时只获取第0个参数
        return linkMethod.execute(gatewaySession, (Map<String, Object>) args[0]);
    }

}
