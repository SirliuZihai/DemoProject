package com.zihai.h2Client.bean;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages ="com.zihai.h2Client.dao.ds1", sqlSessionTemplateRef  = "ds1SqlSessionTemplate")
public class MybaticConfigDs1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(MybaticConfigDs1.class);

    //主数据源配置 ds1数据源
    @Primary
    @Bean(name = "ds1DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.s1")
    public DataSourceProperties ds1DataSourceProperties() {
        LOGGER.info("ds1 init DataSourceProperties");
        return new DataSourceProperties();
    }

    //主数据源 ds1数据源
    @Primary
    @Bean(name ="ds1DataSource")
    public DataSource ds1DataSource(@Qualifier("ds1DataSourceProperties") DataSourceProperties dataSourceProperties) {
        LOGGER.info("ds1 init DataSource");
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "ds1TransactionManager")
    public DataSourceTransactionManager ds1TransactionManager(@Qualifier("ds1DataSource") DataSource dataSource) {
        LOGGER.info("ds1 init ds1TransactionManager");
        return new DataSourceTransactionManager(dataSource);
    }

    //主数据源 ds1数据源
    @Primary
    @Bean("ds1SqlSessionFactory")
    public SqlSessionFactory ds1SqlSessionFactory(@Qualifier("ds1DataSource") DataSource dataSource) throws Exception {
        LOGGER.info("ds1 init SqlSessionFactory");
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:mapper/ds1/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Primary
    @Bean(name = "ds1SqlSessionTemplate")
    public SqlSessionTemplate ds1SqlSessionTemplate(@Qualifier("ds1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        LOGGER.info("ds1 init SqlSessionTemplate");
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}