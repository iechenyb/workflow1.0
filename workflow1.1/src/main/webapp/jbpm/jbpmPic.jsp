<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*,org.jbpm.api.*,org.jbpm.api.task.*,org.jbpm.api.model.*" %>
<%  
String basePath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";  
String picName="process.png";
if(request.getParameter("picName")!=null){
	picName = request.getParameter("picName")+".png"; 
}
%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看流程图</title>
</head>
<body>
<a href="<%=basePath%>jbpm/list.jsp" target="_self"><b>refresh</b></a>
<div style="z-index:2;position:absolute;border:0px solid red;left:10px;top:4px;"><font size=5 color=red><b>processid=${id}</b></font></div>
<img src="<%=basePath%>jbpm/<%=picName%>" style="position:absolute;left:0px;top:0px;" alt=''/>
<div style="z-index:99;position:absolute;border:2px solid red;left:${x}px;top:${y}px;width:${w}px;height:${h}px;"></div>
<div style="z-index:2;position:absolute;border:0px solid red;left:10px;top:600px;"><font size=5 color=red><b>当前节点名称 :${taskName},处理人:${person}</b></font></div>
</body>
</html>