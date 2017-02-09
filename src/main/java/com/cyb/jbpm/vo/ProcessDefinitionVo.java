package com.cyb.jbpm.vo;

public class ProcessDefinitionVo {
   private String id ;
   private String name="";
   private String key="";
   private String version="";
   private String deploymentId="";
   private String picName = "";
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public String getDeploymentId() {
	return deploymentId;
}
public void setDeploymentId(String deploymentId) {
	this.deploymentId = deploymentId;
}
public String getPicName() {
	return picName;
}
public void setPicName(String picName) {
	this.picName = picName;
}
}
