package com.cyb.jbpm.vo;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import com.cyb.jbpm.DateUtil;

public class YwInfor {
  String name;//任务名称
  String time;//操作时间
  String person;//操作员或者角色
  String note;//备忘
  Map<String, Object> variables;
  public YwInfor(String name,String person,Map<String, Object> variables){
	  this.name = name;
	  this.person = person;
	  this.time = DateUtil.date2long14(new Date())+"";
	  this.note=new Random().nextInt(1000)+"";
	  this.variables = variables;
  }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
  public Map<String, Object> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
public String toString(){
	  return person+" at time "+ time+" execute "+name+" note is "+note+", param is"+variables;
  }
}
