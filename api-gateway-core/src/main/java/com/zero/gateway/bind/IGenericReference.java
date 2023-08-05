package com.zero.gateway.bind;

import java.util.Map;

/**
 * @description: 一种自定义出来的接口： 统一泛化调用接口，专门给通信层做编码调用使用的
 * 类似于Mybatis中的DAO接口，MapperProxyFactory创建出的代理类的type，就是该接口类型
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */
public interface IGenericReference {

    String $invoke(Map<String, Object> params);

}