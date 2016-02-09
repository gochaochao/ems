<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css"
	href="${ctp}script/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${ctp}script/themes/icon.css">
<script type="text/javascript" src="${ctp}script/jquery.min.js"></script>
<script type="text/javascript" src="${ctp}script/jquery.easyui.min.js"></script>

<script type="text/javascript">
	$(function() {
		$('#tt').tree({
			onClick : function(node) {
				if(node.url){
					window.parent.document.getElementById("content").src=node.url;
				}
			}
		});
	});
</script>

</head>
<body>

	<ul id="tt" class="easyui-tree"
		data-options="url:'tree_data1.json',method:'get',animate:true"></ul>

</body>
</html>