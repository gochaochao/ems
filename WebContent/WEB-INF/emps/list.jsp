<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="${ctp }css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp }css/list.css">
<link rel="stylesheet" type="text/css"
	href="${ctp }script/thickbox/thickbox.css">

<script type="text/javascript" src="${ctp }script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${ctp }script/thickbox/thickbox.js"></script>

<script type="text/javascript">
	$(function(){
		//AJAX删除：
		/*
		需求：
		1.点击"删除"的超链接时，弹出一个confirm：确定要删除XXX的信息吗？
		2.若点击确定，则发送AJAX请求到服务端
		3.先查询该Employee是否为一个Department的Manager，若是，则alert不能删除
		4.若不是Manager，则把对应的isDeleted字段修改为1
		5.页面上需要：
			5.1超链接变为一个普通的文本
			5.2删除的状态项由正常变为删除
		*/
		$(".delete").click(function(){
			var loginName=$(this).prev(":hidden").val();
			var flag=confirm("确定要删除"+loginName+"吗？");
			if(!flag){
				return false;
			}
			var url="${ctp}emp-delete";
			var id=$(this).next(":hidden").val();
			var args={"id":id,"time":new Date()};
			
			var $span=$(this).parent();
			//data返回值是一个标记位就可以了，例如：1,2,3
			$.post(url,args,function(data){
				if(data=="1"){
					alert("是Manager，不能被删除！");
				}else if(data=="2"){
					alert("删除成功");
					$span.html("删除");
					$("#delete-"+id).text("删除");
				}else{
					alert("删除失败");
				}
			});
			return true;
		});
		
		//页面跳转的JS
		$(".logintxt").change(function(){
			var val=this.value;
			val=$.trim(val);
			var reg=/^\d+$/g;
			if(!reg.test(val)){
				alert("输入的页码不合法！");
				this.value="";
				return;
			}
			var pageNumber=parseInt(val);
			if(pageNumber<1 || pageNumber>parseInt("${page.totalPages}")){
				alert("输入的页码不合法！");
				this.value="";
				return;
			}
			window.location.href="${ctp}emp-list?page.pageNumber="+val;
		});
	});
</script>

</head>
<body>

<br><br>
	<center>
		<br><br>
		
		<a id="criteria" href="${ctp}emp-criteriaInput?height=300&width=320&${queryString}"  class="thickbox"> 
	   		增加(显示当前)查询条件	   		
		</a> 
		
		<a href="" id="delete-query-condition">
		   	删除查询条件
		</a>
		
		<span class="pagebanner">
			共 ${page.totalRecord } 条记录
			&nbsp;&nbsp;
			共 ${page.totalPages } 页
			&nbsp;&nbsp;
			当前第 ${page.pageNumber } 页
		</span>
		
		<span class="pagelinks">
			
			<s:if test="page.hasPrev">
				[
				<a href="emp-list3?page.pageNumber=1&${queryString }">首页</a>
				/
				<a href="emp-list3?page.pageNumber=${page.pageNumber - 1 }&${queryString }">上一页</a>
				] 
			</s:if>
			
			<span id="pagelist">
				转到 <input type="text" name="pageNumber" size="1" height="1" class="logintxt"/> 页
			</span>
			
			<s:if test="page.hasNext">
								[
				<a href="emp-list3?page.pageNumber=${page.pageNumber + 1 }&${queryString }">下一页</a>
				/
				<a href="emp-list3?page.pageNumber=${page.totalPages }&${queryString }">末页</a>
				] 
			</s:if>
			
		</span>
		
		<table>
			<thead>
				<tr>
					<td><a id="loginname" href="">登录名</a></td> 
					<td>姓名</td>
					
					<td>登录许可</td>
					<td>部门</td>
					
					<td>生日</td>
					<td>性别</td>
					
					<td><a id="email" href="">E-Mail</a></td>
					<td>手机</td>
					
					<td>登录次数</td>
					<td>删除</td>
					<td>角色</td>
					
					<td>操作</td>
				</tr>
			</thead>
			
			<tbody>
			<s:iterator value="page.content">
				<tr>
					<td><a id="loginname" href="">${loginName }</a></td> 
					<td>${employeeName }</td>
					
					<td>${enabled == 1 ? '允许':'禁止' }</td>
					<td>${department.departmentName}</td>
					
					<td>
						<s:date name="birth" format="yyyy-MM-dd"/>
					</td>
					<td>${gender == 1 ? '男':'女' }</td>
					
					<td><a id="email" href="">${email }</a></td>
					<td>${mobilePhone }</td>
					
					<td>${visitedTimes }</td>
					<td id="delete-${employeeId}">${isDeleted == 1 ? '删除':'正常' }</td>
					
					<td>${roleNames}</td>
					<td>
						<a href="emp-input?id=${employeeId }">修改</a>
						&nbsp;
						<span>
							<s:if test="isDeleted == 1">删除</s:if>
							<s:else>
								<!-- 没有name的hidden隐藏域标签，就是给js用的 -->
								<input type="hidden" value="${loginName }"/>
								<a href="#" class="delete">删除</a>
								<input type="hidden" value="${employeeId }"/>
							</s:else>
						</span>
						
					</td>
				</tr>
			</s:iterator>	
			</tbody>
		</table>
		
		<a href="${ctp }emp-downToExcel">下载到 Excel 中</a>
		&nbsp;&nbsp;&nbsp;&nbsp;
	</center>

</body>
</html>