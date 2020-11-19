package com.zihai.h2Client.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * @author Carter
 */
@Configuration
// 加上这个注解，使得支持事务
@EnableTransactionManagement
public class MyBatisConfig implements TransactionManagementConfigurer {
	private final static Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);
	
	@Autowired
	private DataSource dataSource;

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		logger.info("设置PlatformTransactionManager");
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception{
		//解决myBatis下 不能嵌套jar文件的问题
		//VFS.addImplClass(SpringBootVFS.class);
		
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		 PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		bean.setDataSource(dataSource);
		bean.setTypeAliasesPackage("com.zihai");
		bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
		try {
			logger.info("设置sqlSessionFactory");
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		logger.info("进来设置SqlSessionTemplate");
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}