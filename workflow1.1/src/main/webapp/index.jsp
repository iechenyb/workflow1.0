<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
<%  
String basePath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";  
Object object = request.getSession().getAttribute("username");
String username = "";
if (object != null) {
	username = object.toString();
}
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>欢迎</title>  
</head>  
<body>  
<h2>welcome to use spring mvc!</h2>  
<form action="test/login.htm">  
    用户名：<input id="username" name="username" type="text"></input><br>  
    密  码：<input id="password" name="password" type="password"></input><br>  
    <input type="submit">  
</form>  
<a href="<%=basePath%>jbpm/lst1.htm" target="_blank"><b>Go to Study WorkFlow</b></a>
</body>  
</html>  