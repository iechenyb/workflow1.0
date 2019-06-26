package com.kiiik;

import java.sql.SQLException;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://www.mossle.com/docs/activiti/index.html api官方
//https://blog.csdn.net/weixin_40542512/article/details/83657615 表介绍！
@SpringBootApplication
/*@EnableAutoConfiguration(exclude = {
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})*/
@Configuration
public class ActivitiApplication{
    public static void main(String[] args) {
    	startH2Server();
        SpringApplication
        .run(ActivitiApplication.class, args);
    }
    //@ConditionalOnProperty(name="",havingValue="true")
    public static void startH2Server() {
        try {
        	System.out.println("当前数据库环境是H2，正在启动TCP服务！");
            Server h2Server = Server.createTcpServer().start();
            if (h2Server.isRunning(true)) {//如果已经启动，则不再启动！
                System.out.println("H2 server was started and is running.");
            } else {
                throw new RuntimeException("Could not start H2 server.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 server: ",e);
        }
    }
    
    @Bean
    public ProcessDefinitionEntity aaa(){
    	return new ProcessDefinitionEntity();
    }
}