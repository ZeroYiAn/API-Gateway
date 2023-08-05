package com.zero.gateway.datasource;

/**
 * @description: 数据源接口，RPC、HTTP 都当做连接的数据资源使用
 * @author: ZeroYiAn
 * @time: 2023/8/5
 */
public interface DataSource {
    Connection getConnection();
}
