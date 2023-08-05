package com.zero.gateway.session.defaults;

import com.zero.gateway.datasource.DataSource;
import com.zero.gateway.datasource.DataSourceFactory;
import com.zero.gateway.datasource.unpooled.UnpooledDataSourceFactory;
import com.zero.gateway.session.Configuration;
import com.zero.gateway.session.GatewaySession;
import com.zero.gateway.session.GatewaySessionFactory;

/**
 * @description: 默认网关会话工厂，基于工厂模式创建会话
 * @author: ZeroYiAn
 * @time: 2023/8/4
 */

public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession(String uri) {
        // 获取数据源连接信息：这里把 Dubbo、HTTP 抽象为一种连接资源
        DataSourceFactory dataSourceFactory = new UnpooledDataSourceFactory();
        dataSourceFactory.setProperties(configuration, uri);
        DataSource dataSource = dataSourceFactory.getDataSource();

        return new DefaultGatewaySession(configuration, uri, dataSource);
    }

}
