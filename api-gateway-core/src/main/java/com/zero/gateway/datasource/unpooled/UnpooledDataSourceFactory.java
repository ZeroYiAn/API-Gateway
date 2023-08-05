package com.zero.gateway.datasource.unpooled;

import com.zero.gateway.datasource.DataSource;
import com.zero.gateway.datasource.DataSourceFactory;
import com.zero.gateway.datasource.DataSourceType;
import com.zero.gateway.session.Configuration;

/**
 * @description: 无池化的数据源工厂
 * @author: ZeroYiAn
 * @time: 2023/8/5
 */

public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected UnpooledDataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Configuration configuration, String uri) {
        this.dataSource.setConfiguration(configuration);
        this.dataSource.setDataSourceType(DataSourceType.Dubbo);
        this.dataSource.setHttpStatement(configuration.getHttpStatement(uri));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

}
