<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%  
    String path = request.getContextPath();  
    String basePath = request.getScheme() + "://"  
            + request.getServerName() + ":" + request.getServerPort()  
            + path + "/";  
%>  
<!DOCTYPE html>  
<html>  
<head>  
<title>Log Search</title>  
<base href="<%=basePath%>">  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/css/jasny-bootstrap.css">
<link rel="stylesheet" href="css/style.css">
<script src="js/jquery-1.12.3.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/js/jasny-bootstrap.js"></script>

</head>  
<body>
	<div class="navigation">
		<h1>SUR</h1>
	</div>
	
	<div class="slogan">
		<h1>Log Search</h1>
		<p>SUR figures out solutions to errors in openstack logs.</p>
	</div>
	
	<form class="fileinput" action="/logsearch/uploadlog" method="post" enctype="multipart/form-data">
		<div class="fileinput fileinput-new" data-provides="fileinput">
			<span class="btn btn-primary btn-file">
				<span>Choose file</span>
				<input type="file" name="file">
			</span>
        	<input class="btn btn-primary upload" type="submit" value="Upload" />
        	<span class="fileinput-filename"></span>
        	<!-- <input class="fileinput-new" type="text" placeholder="No file chosen">-->
        </div>
    </form>
    
    <div class="footer">
    	Published by IBM & Tongji University @2016
    </div>  
    
    
</body>  
</html>