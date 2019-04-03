<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%  
String basePath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";  
Object object = request.getSession().getAttribute("username");
String username = "";
if (object != null) {
	username = object.toString();
}
%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="app">
<head>
<link rel="icon" href="<%=basePath%>img/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="<%=basePath%>img/favicon.ico" type="image/x-icon">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=basePath%>amazeui/css/amazeui.min.css">
<link rel="stylesheet" href="<%=basePath%>amazeui/css/app.css">
<link rel="stylesheet" href="<%=basePath%>/css/estock.css">
<input type="hidden" id="basePath" value=<%=basePath%>/>
<input type="hidden" value="<%=username%>" id='username' name='username'/>
<title>workflow list</title>
</head>
<body ng-controller='controller'>
<hr style="border:1px solid red;"/>
<center><font size=5> <b> 工作流模板发布</b></font></center>
<center><form action="<%=basePath%>jbpm/deploy.htm" method='post'>
<input name="file" id='file' value='process.jpdl.xml'/>
<button type="submit" class="am-btn am-btn-primary am-radius">deploy</button>
<a href="<%=basePath%>jbpm/lst1.htm" target="_self"><b>refresh</b></a>
</form>
</center>
 <center>
<hr style="border:1px solid red;"/>
<center><font size=3> <b> 工作流模板</b></font></center>
 <table class="am-table am-table-bd am-table-bdrs am-table-striped">
    <thead>
        <tr>
            <th>id</th>
            <th>key</th>
            <th>deploymentId</th>
            <th>operation</th>
        </tr>
    </thead>
     <tbody>
      <c:forEach var="vo" items="${data}" varStatus="status">
        <tr>
            <td> <b>${vo.id}</b></td>
            <td><b>${vo.key}</b> </td>
             <td> <b>${vo.deploymentId}</b></td>
            <td><a href="<%=basePath%>jbpm/start.htm?id=${vo.deploymentId}" target="_self">发起流程</a>
           <a href="<%=basePath%>jbpm/del1.htm?deploymentId=${vo.deploymentId}" target="_self">删除模板</a>
            </td>
        </tr>
        </c:forEach>
   </tbody>
</table>
<hr style="border:1px solid red;"/>
<center><font size=3> <b>已发起的流程</b></font></center>
 <table class="am-table am-table-bd am-table-bdrs am-table-striped">
    <thead>
        <tr>
            <th>id</th>
             <th>deploymentId</th>
            <th>state</th>
            <th>operation</th>
        </tr>
    </thead>
     <tbody>
      <c:forEach var="vo" items="${data2}" varStatus="status">
        <tr>
            <td> <b>${vo.id}</b></td>
             <td> <b>${vo.processDefinitionId}</b></td>
            <td><b>${vo.state}</b></td>
            <td>
            <a href="<%=basePath%>jbpm/image.htm?id=${vo.id}&picName=process" target="_blank">lookPic</a>          
            </td>
        </tr>
        </c:forEach>
   </tbody>
</table>
<hr style="border:1px solid red;"/>
<center><font size=3> <b>需要我处理的任务</b></font></center>
<hr/>
<table class="am-table am-table-bd am-table-bdrs am-table-striped">
    <thead>
        <tr>
            <th>taskid</th>
             <th>assignee</th>
            <th>taskname</th>
            <th>executionId</th>
            <th>activityName</th>
            <!-- <th>formResourceName</th> -->
            <th>createtime</th>
        </tr>
    </thead>
     <tbody>
     <c:forEach var="vo" items="${data3}" varStatus="status">
        <tr>
            <td> <b>${vo.id}</b></td>
             <td> <b>${vo.assignee}</b></td>
            <td> <b>${vo.name}</b></td>
            <td><b>${vo.executionId}</b></td>
             <td><b>${vo.activityName}</b></td>
              <!-- <td><b>{{vo.formResourceName}}</b></td> -->
              <td><b>${vo.createTime}</b></td>
            <td>
            <a href="<%=basePath%>jbpm/image.htm?id=${vo.executionId}&picName=process" target="_blank">lookPic</a>
            <a href="<%=basePath%>jbpm/handle.htm?id=${vo.id}&cmd=go" target="_self">goForward</a>
            </td>
        </tr>
        </c:forEach>
   </tbody>
</table>
<form action="<%=basePath%>jbpm/handle.htm" method='post'>
taskId :<input name="id" id='id' value=''/>
send cmd :<input name="cmd" id='cmd' value=''/>
<button type="submit" class="am-btn am-btn-primary am-radius">send</button>
</form>
<hr style="border:1px solid red;"/>
</center>
 <script src="<%=basePath%>js/jquery.min.js"></script>
 <script src="<%=basePath%>js/jquery.js"></script>
 <script src="<%=basePath%>angular/angular-1.0.1.min.js"></script>
 <script src="<%=basePath%>amazeui/js/amazeui.min.js"></script>
</body>
</html>