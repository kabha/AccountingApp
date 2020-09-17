package com.account.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.account" })
@EntityScan({ "com.account.modal" })
@EnableJpaRepositories({ "com.account.dao" })
public class AppMainRunner extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(AppMainRunner.class, args);
		System.out.println("--------AppMainRunner-Started--------");
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(new Class[] { AppMainRunner.class });
	}

}
