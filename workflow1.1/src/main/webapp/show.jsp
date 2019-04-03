<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*,org.jbpm.api.model.*" %>
<%
/* String id=request.getParameter("id");
ProcessEngine processEngine=Configuration.getProcessEngine(); 
RepositoryService repositoryService = processEngine.getRepositoryService();
ExecutionService executionService=processEngine.getExecutionService();
ProcessInstance processInstance=executionService.findProcessInstanceById(id);
Set<String> activityName=processInstance.findActiveActivityNames();
ActivityCoordinates ac=repositoryService.getActivityCoordinates(processInstance.getProcessDefinitionId(), activityName.iterator().next());
 */%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看流程图</title>
</head>
<body>
<img src="process.png" style="position:absolute;left:0px;top:0px;"/>
<div style="z-index:2;position:absolute;border:2px solid red;left:488px;top:167px;width:50px;height:50px;"></div>
</body>
</html>