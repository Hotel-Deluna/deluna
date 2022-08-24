package com.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) // DataSourceAutoConfiguration = JDBC 설정끝나면 지워야됨. 임시조치
public class DelunaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DelunaApplication.class, args);
	}

}
