package com.zero.gateway.datasource;

import com.zero.gateway.session.Configuration;

/**
 * @description: 数据源工厂
 * @author: ZeroYiAn
 * @time: 2023/8/5
 */
public interface DataSourceFactory {

    void setProperties(Configuration configuration, String uri);

    DataSource getDataSource();

}