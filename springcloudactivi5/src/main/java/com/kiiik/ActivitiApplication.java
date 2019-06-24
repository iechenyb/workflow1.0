package com.kiiik;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
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
        SpringApplication
        .run(ActivitiApplication.class, args);
        
    }
    @Bean
    public ProcessDefinitionEntity aaa(){
    	return new ProcessDefinitionEntity();
    }
}