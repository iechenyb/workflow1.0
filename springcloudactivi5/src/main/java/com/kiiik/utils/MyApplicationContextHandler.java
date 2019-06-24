package com.kiiik.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@Component
public class MyApplicationContextHandler implements ApplicationContextAware {

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		context = arg0;
	}
	
	public static Object getBean(String name){
		return context.getBean(name);
	}
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<?> clss){
		return (T)context.getBean(clss);
	}

}
