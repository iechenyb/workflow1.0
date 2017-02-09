package com.cyb;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.jbpm.pvm.internal.id.PropertyImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@RequestMapping("/test")
@Controller  
public class FirstController {  
	/*@Resource(name="processEngine")
	ProcessEngine processEngine;*/
	
    @RequestMapping("/login") //用来处理前台的login请求  
    @ResponseBody
    private  String hello(  
            @RequestParam(value = "username", required = false)String username,  
            @RequestParam(value = "password", required = false)String password  
            )
    {  
//    	StaticLoggerBinder y;
    	PropertyImpl x; 
//    	ResolvableTypeProvider ll;
//    	MappingJacksonHttpMessageConverter ss;
//    	System.out.println("processEngine="+processEngine);
        return "the message from spingmvc"
        		+ ": Hello "+username+",Your password is: "+password;  
    }  
    @RequestMapping("/map") //用来处理前台的login请求  
    @ResponseBody
    private  JSONArray map(){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("name", "chenyb");
    	return JSONArray.fromObject(map);
    } 
    @RequestMapping("/retmap") //用来处理前台的login请求  
    @ResponseBody
    private  Map retmap(){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("name", "chenyb");
    	return map;
    } 
} 