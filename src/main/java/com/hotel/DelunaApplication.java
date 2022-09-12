package com.hotel;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication // DataSourceAutoConfiguration = JDBC 설정끝나면 지워야됨.
//@MapperScan(basePackages = "com.hotel.mapper")
public class DelunaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DelunaApplication.class, args);
	}

}
