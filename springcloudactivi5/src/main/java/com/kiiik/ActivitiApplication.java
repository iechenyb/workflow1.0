package com.kiiik;

import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.kiiik.config.HomeProperties;
import com.kiiik.utils.PropertiesUtil;

//http://www.mossle.com/docs/activiti/index.html api官方
//https://blog.csdn.net/weixin_40542512/article/details/83657615 表介绍！
@SpringBootApplication
/*@EnableAutoConfiguration(exclude = {
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})*/
@Configuration
public class ActivitiApplication implements CommandLineRunner{
	@Autowired
    private HomeProperties homeProperties;
	/**
	 * tomcat服务启动之后再执行run方法！
	 */
    @Override
    public void run(String... strings) throws Exception {
        System.out.println(">>>>>>>>>>>>>>  "+homeProperties.toString());
    }
    public static void main(String[] args) {
    	
    	
        SpringApplication
        .run(ActivitiApplication.class, args);
    }
    @Autowired
    private Environment environment;
    /**
     * 在数据库创建之前启动h2服务
     */
    @PostConstruct
    public  void test(){
    	/*PropertiesUtil prop = new PropertiesUtil("application.properties");
    	if(!"mysql".equals(prop.get("spring.profiles.active").toString())){
    		startH2Server();
    	}*/
        String property = environment.getProperty("spring.profiles.active");
        System.out.println("test1="+property);
        if(!"mysql".equals(property)){
    		startH2Server();
    	}
    }
    //@ConditionalOnProperty(name="",havingValue="true")
    public void startH2Server() {
        try {
        	/*System.out.println("当前数据库环境是H2，正在启动TCP服务！");
            Server h2Server = //Server.createTcpServer().start();
            Server.createTcpServer(
                    new String[] { 
                    		"-tcp", 
                    		"-tcpAllowOthers",
                    		"-tcpPort",
                             "9092" }).start();//url为tcp协议连接端口，非sever端口8082
            if (h2Server.isRunning(true)) {//如果已经启动，则不再启动！
                System.out.println("H2 server was started and is running.");
            } else {
                throw new RuntimeException("Could not start H2 server.");
            }*/
        } catch (Exception e) {
            throw new RuntimeException("Failed to start H2 server: ",e);
        }
    }
}