<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet"  type="text/css" href="${ctp }css/content.css">
	<link rel="stylesheet" type="text/css" href="${ctp }css/list.css">
</head>
<body>
	<br><br>
	<center>
		<br><br>
		<a href="emp-uploadTemplateDownload">下载模板</a><br><br>
		
		<font color="red">
			<!-- 显示上传时的错误消息: 例如部门是否存在. 输入的性别是否合法 -->
			<s:fielderror fieldName="file"></s:fielderror>
			<s:actionerror escape="false"/>	
		</font>
		<br><br>
		
		<form action="emp-upload" enctype="multipart/form-data" method="post">
			<input type="file" name="file" />
			<input type="submit" value="上传"/>
		</form>
	</center>
</body>
</html>