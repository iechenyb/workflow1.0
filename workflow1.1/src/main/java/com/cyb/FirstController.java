package com.cyb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
@RequestMapping("/test")
@Controller  
public class FirstController {  
	/*@Resource(name="processEngine")
	ProcessEngine processEngine;*/
	
    @RequestMapping("/login") //��������ǰ̨��login����  
    @ResponseBody
    private  String hello(  
            @RequestParam(value = "username", required = false)String username,  
            @RequestParam(value = "password", required = false)String password  
            )
    {  
    	Constants.ROLE = username;
//    	StaticLoggerBinder y;
//    	PropertyImpl x; 
//    	ResolvableTypeProvider ll;
//    	MappingJacksonHttpMessageConverter ss;
//    	System.out.println("processEngine="+processEngine);
        return "the message from spingmvc"
        		+ ": Hello "+username+",Your password is: "+password;  
    }  
    @RequestMapping("/map") //��������ǰ̨��login����  
    @ResponseBody
    private  JSONArray map(){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("name", "chenyb");
    	return JSONArray.fromObject(map);
    } 
    @RequestMapping("/retmap") //��������ǰ̨��login����  
    @ResponseBody
    private  Map retmap(){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("name", "chenyb");
    	return map;
    } 
} 