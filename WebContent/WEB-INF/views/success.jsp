<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>教务管理系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>

<frameset rows="50,*,20" cols="*" frameborder="no" border="0"
	framespacing="0" id="frame1">

	<frame name="banner" id="banner" scrolling="no" noresize="noresize"
		src="${ctp }/common/topbar.jsp" frameborder="0">

	<frameset rows="*" cols="200,10,*" frameborder="no" border="0"
		framespacing="0" id="frame2">
		<frame name="lef" id="left" scrolling="no" noresize="noresize"
			src="${ctp }/common/navigation.jsp?a=12" frameborder="0">
		<frame name="menu" id="menu" scrolling="no" noresize="noresize"
			src="${ctp }/common/menu.jsp" frameborder="0">
		<frame name="content" id="content" scrolling="no" frameborder="0"
			src="${ctp }/common/content.jsp">
	</frameset>

	<frame name="status_bar" id="status_bar" scrolling="no"
		noresize="noresize" src="${ctp }/common/status_bar.jsp"
		frameborder="0">

</frameset>

</html>