<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/common.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctp}css/menu.css">
<script type="text/javascript"
	src="${ctp}script/jquery-1.3.1.js"></script>
<script type="text/javascript">
	var view_flag = 1;

	$(function() {
		$("body")
				.click(
						function() {
							if (view_flag == 1) {
								window.parent.parent.document
										.getElementById("frame2").cols = "0,10,*";
								$(".navPoint").text(4);
							} else {
								window.parent.parent.document
										.getElementById("frame2").cols = "200,10,*";
								$(".navPoint").text(3);
							}

							view_flag = 1 - view_flag;
						});
	})
</script>
<body>
	<BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>
	<SPAN class=navPoint style="font-size: 9pt;" id=switchPoint
		title=关闭/打开左栏>3</SPAN>
	<BR>
</body>