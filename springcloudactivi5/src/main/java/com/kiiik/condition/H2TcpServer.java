package com.kiiik.condition;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.kiiik.ActivitiApplication;

@Order(1)
@Component
//spring.datasource.driverClassName=org.h2.Driver
@ConditionalOnProperty(
		name="spring.datasource.driverClassName",
		//value="spring.datasource.driverClassName",
		havingValue="org.h2.Driver")
public class H2TcpServer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
    	ActivitiApplication.startH2Server();
    }
}

