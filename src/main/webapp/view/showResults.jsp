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
<link rel="stylesheet" href="css/results.css">
<script src="js/jquery-1.12.3.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/js/jasny-bootstrap.js"></script>

</head>  
<body>
	<div class="navigation">
		<span>SUR Log Search</span>
		<a href="index">Home</a>
	</div>
	
	<form class="fileinput" action="/logsearch/uploadlog" method="post" enctype="multipart/form-data">
		<div class="fileinput fileinput-new" data-provides="fileinput">
			<span class="btn btn-primary btn-file">
				<span>Choose file</span>
				<input type="file" name="file">
			</span>
        	<input class="btn btn-primary upload" type="submit" value="Upload" />
        	<span class="fileinput-filename">${filename}</span>
        	<!-- <input class="fileinput-new" type="text" placeholder="No file chosen">-->
        </div>
    </form>
    
    <c:choose>
    	<c:when test="${searchResult.size() > 0}">
		    <ol>
		    <c:forEach items="${ searchResult }" var="searchResult">
		    	<li class="errors">
		    	<p class="bg-info">ERROR: ${searchResult.key}</p>
		    		<ul>
			    	<c:forEach items="${searchResult.value}" var="hitDocs">
			    		<li class="solutions">
			    		<p>${hitDocs.get("title") }</p>
			    		<!-- <p>${hitDocs.get("description") }</p>
			    		<p>${hitDocs.get("answer") }</p>-->    		
			    		</li>
			    	</c:forEach>
			    	</ul>
		    	</li>
		    	<hr />
		    </c:forEach>
		    </ol>
		</c:when>
		<c:otherwise>
			<p class="bg-warning">There are no errors in this log.</p>
		</c:otherwise>
    </c:choose>

</body>  
</html>