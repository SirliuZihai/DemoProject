package com.zihai.h2Client.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
// 因为这个对象的扫描，需要在MyBatisConfig的后面注入，所以加上下面的注解
//@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisMapperScannerConfig {
	private final static Logger logger = LoggerFactory.getLogger(MyBatisMapperScannerConfig.class);
	
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		// 获取之前注入的beanName为sqlSessionFactory的对象
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		// 指定xml配置文件的路径
		mapperScannerConfigurer.setBasePackage("com.zihai.h2Client.dao");
		logger.info("进来设置MapperScannerConfigurer");
		return mapperScannerConfigurer;
	}
	
}