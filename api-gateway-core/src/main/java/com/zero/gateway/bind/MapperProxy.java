package com.zero.gateway.bind;

import com.zero.gateway.session.GatewaySession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description: 映射代理调用：只完成代理部分，并调用映射器方法完成逻辑处理
 *
 * 自定义的代理类实现 MethodInterceptor接口 的intercept方法
 * 在方法中调用目标对象的方法，并做功能增强
 * 类似JDK动态代理的InvocationHandler的实现类
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */

public class MapperProxy implements MethodInterceptor {

    private GatewaySession gatewaySession;
    private final String uri;

    /**
     *  动态代理
     * @param gatewaySession
     * @param uri
     */
    public MapperProxy(GatewaySession gatewaySession, String uri) {
        this.gatewaySession = gatewaySession;
        this.uri = uri;
    }

    /**
     * @param obj     代理对象
     * @param method  目标类中的方法
     * @param args    目标类中方法的参数
     * @param proxy   方法代理对象，通过此对象可以调用代理对象的方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        //通过传入的参数映射绑定到目标方法
        MapperMethod linkMethod = new MapperMethod(uri, method, gatewaySession.getConfiguration());
        // 暂时只获取第0个参数
        return linkMethod.execute(gatewaySession, (Map<String, Object>) args[0]);
    }

}
