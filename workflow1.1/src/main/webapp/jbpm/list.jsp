<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%  
String basePath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";  
Object object = request.getSession().getAttribute("username");
String username = "";
if (object != null) {
	username = object.toString();
}
%> 
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

<form action="<%=basePath%>jbpm/deploy.htm" method='post'>
<input name="file" id='file' value='process.jpdl.xml'/>
<button type="submit" class="am-btn am-btn-primary am-radius">deploy</button>
<a href="<%=basePath%>jbpm/list.jsp" target="_self"><b>refresh</b></a>
</form>
 <center>
 <hr>
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
        <tr ng-repeat="vo in lst">
            <td> <b>{{vo.id}}</b></td>
            <td><b>{{vo.key}}</b> </td>
             <td> <b>{{vo.deploymentId}}</b></td>
            <td><a href="<%=basePath%>jbpm/start.zc?id={{vo.deploymentId}}" target="_self">start</a>
           <a href="<%=basePath%>jbpm/del.zc?deploymentId={{vo.deploymentId}}" target="_self">delete</a>
            </td>
        </tr>
   </tbody>
</table>
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
        <tr ng-repeat="vo in lst2">
            <td> <b>{{vo.id}}</b></td>
             <td> <b>{{vo.processDefinitionId}}</b></td>
            <td><b>{{vo.state}}</b></td>
            <td>
            <a href="<%=basePath%>jbpm/image.zc?id={{vo.id}}" target="_blank">lookPic</a>          
            </td>
        </tr>
   </tbody>
</table>

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
        <tr ng-repeat="vo in lst3">
            <td> <b>{{vo.id}}</b></td>
             <td> <b>{{vo.assignee}}</b></td>
            <td> <b>{{vo.name}}</b></td>
            <td><b>{{vo.executionId}}</b></td>
             <td><b>{{vo.activityName}}</b></td>
              <!-- <td><b>{{vo.formResourceName}}</b></td> -->
              <td><b>{{vo.createTime}}</b></td>
            <td>
            <a href="<%=basePath%>jbpm/image.zc?id={{vo.executionId}}" target="_blank">lookPic</a>
            <a href="<%=basePath%>jbpm/handle.zc?id={{vo.id}}&cmd=go" target="_self">goForward</a>
            </td>
        </tr>
   </tbody>
</table>
<form action="<%=basePath%>jbpm/handle.zc" method='post'>
taskId :<input name="id" id='id' value=''/>
send cmd :<input name="cmd" id='cmd' value=''/>
<button type="submit" class="am-btn am-btn-primary am-radius">send</button>
</form>
<div class="am-modal am-modal-no-btn" tabindex="-1" id="your-modal">
	<div class="am-modal-dialog">
		<div class="am-modal-hd">
			process pic <a href="javascript: void(0)"
				class="am-close am-close-spin" data-am-modal-close>&times;</a>
		</div>
		<div id="longShortRateTrend" style="height: 80%"></div>
	</div>
</div>
</center>
 <script src="<%=basePath%>js/jquery.min.js"></script>
 <script src="<%=basePath%>js/jquery.js"></script>
 <script src="<%=basePath%>angular/angular-1.0.1.min.js"></script>
 <script src="<%=basePath%>amazeui/js/amazeui.min.js"></script>
 <script src="<%=basePath%>/js/my/processList.js"></script>
</body>
</html>