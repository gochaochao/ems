<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="${ctp }css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp }css/list.css">
<link rel="stylesheet" type="text/css" href="${ctp }script/thickbox/thickbox.css">

<script type="text/javascript" src="${ctp }script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${ctp }script/thickbox/thickbox.js"></script>

<script type="text/javascript">
	$(function(){
		//若当前页面是第一页, 则隐藏: pageLink1
		if("${page.pageNo}" == "1"){
			$("#pageLink1").hide();
		}
		
		if("${page.pageNo}" == "${page.totalPages}"){
			$("#pageLink2").hide();
		}
		
		//若当前页面是最后一页, 则隐藏: pageLink2
		
		//ajax 分页
		$(".pagelinks a").click(function(){
			var url = $(this).attr("href");
			var args = {"time":new Date()}
			$.post(url, args, function(data){
				//返回的是一个 Page 对象对应的 JSON 对象. 
				//需要更新如下位置: 
				//pagebanner 部分的内容
				$(".pagebanner").html("共 " + data.totalElements + " 条记录&nbsp;&nbsp;"
					+ "共 " + data.totalPages + " 页&nbsp;&nbsp;"
					+ "当前第 " + data.pageNo + " 页");
				
				//修改页面链接的 href 属性
				$("#nextPage").attr("href", "${ctp}emp-list2?page.pageNo=" + (data.pageNo + 1));
				$("#prevPage").attr("href", "${ctp}emp-list2?page.pageNo=" + (data.pageNo - 1));
				//编写其他页码的 href 属性.
				
				//首页, 上一页; 下一页, 末页的超链接有可能需要被隐藏. 也有可能被显示.
				if(data.pageNo != 1){
					$("#pageLink1").show();
				}else{
					$("#pageLink1").hide();
				}
				
				if(data.pageNo != data.totalPages){
					$("#pageLink2").show();
				}else{
					$("#pageLink2").hide();
				}
				
				//table 中的内容都需要被更新. 
				$("#tbody").empty();
				
				for(var i = 0; i < data.content.length; i++){
					var $deleteSpan = $("<span></span>");
					if(data.content[i].isDeleted == 1){
						$deleteSpan.html("删除");
					}else{
						$deleteSpan.html("<input type='hidden' value='" + data.content[i].loginName + "'/>" 
								+ "<a href=''>删除</a>"
								+ "<input type='hidden' value='" + data.content[i].employeeId + "'/>");
						//使新添加的 a 节点也能响应 deleteEmp 响应函数
						$deleteSpan.find("a").click(function(){
							deleteEmp(this);
							return false;
						});
					}
					
					
					var $editTd = $("<td></td>").append("<a href=''>编辑</a> &nbsp;")
					                            .append($deleteSpan);
					
					var $tr = $("<tr></tr>");
					$tr.append("<td>" + data.content[i].loginName + "</td>")
					   .append("<td>" + data.content[i].employeeName + "</td>")
					   .append("<td>" + (data.content[i].enabled == 1 ? '允许':'禁止') + "</td>")
					   .append("<td>" + data.content[i].departmentName + "</td>")
					   .append("<td>" + data.content[i].birth2 + "</td>")
					   .append("<td>" + data.content[i].gender + "</td>")
					   .append("<td>" + data.content[i].email + "</td>")
					   .append("<td>" + data.content[i].mobilePhone + "</td>")
					   .append("<td>" + data.content[i].visitedTimes + "</td>")
					   .append("<td id='delete-" + data.content[i].employeeId + "'>" + data.content[i].isDeleted + "</td>")
					   .append("<td>" + data.content[i].roleNames + "</td>")
					   .append($editTd);
					
					$tr.appendTo($("#tbody"));
				}
				
			});
			
			return false;
		});
		
		//Ajax 删除:
		/*
		需求: 
		1. 点击 "删除" 的超链接时, 弹出一个 confirm: 确定要删除 xxx 的信息吗 ?
		2. 若点击确定, 则发送 Ajax 请求到服务端
		3. 先查询该 Employee 是否为一个 Manager. 即该 Employee 是某一个 Department 的 manager. 
		若是一个 Manager, 则 alter: 是 Manager! 不能删除!
		4. 若不是 Manager, 则把对应的 isDeleted 字段的值修改为 1.
		5. 页面上需要:
		5.1 超链接变为一个普通的文本
		5.2 状态项由 "正常" 修改为 "删除".
		*/
		function deleteEmp(aNode){
			var loginName = $(aNode).prev(":hidden").val();
			var flag = confirm("确定要删除" + loginName + "的信息吗?");
			if(!flag){
				return false;
			}
			
			var url = "${ctp}emp-delete";
			var id = $(aNode).next(":hidden").val();
			var args = {"id":id,"time":new Date()};
			
			var $span = $(aNode).parent();
			
			//有时候, 返回值是一个标记位就可以了. 例如: 1,2,3
			$.post(url, args, function(data){
				if(data == "1"){
					alert("是 Manager! 不能被删除!");
				}else if(data == "2"){
					alert("删除成功!");
					$span.html("删除");
					$("#delete-" + id).text("删除");
				}else{
					alert("删除失败!");
				}
			});
			
			return false;
		}
		$(".delete").click(function(){
			deleteEmp(this);
			return false;
		});
		
		//页面跳转的 JS 代码. 
		$(".logintxt").change(function(){
			var val = this.value;
			val = $.trim(val);
			
			var reg = /^\d+$/g;
			if(!reg.test(val)){
				alert("输入的页码不合法!");
				this.value = "";
				return;
			}
			
			var pageNo = parseInt(val);
			if(pageNo < 1 || pageNo > parseInt("${page.totalPages}")){
				alert("输入的页码不合法!");
				this.value = "";
				return;
			}
			
			window.location.href = "${ctp}emp-list?page.pageNo=" + val;
		});
	})
</script>

</head>
<body>
	
	<br><br>
	<center>
		<br><br>
		
		<a id="criteria" href="${ctp }emp-criteriaInput?height=300&width=320"  class="thickbox"> 
	   		增加(显示当前)查询条件	   		
		</a> 
		
		<a href="" id="delete-query-condition">
		   	删除查询条件
		</a>
		
		<span class="pagebanner">
			共 ${page.totalElements } 条记录
			&nbsp;&nbsp;
			共 ${page.totalPages } 页
			&nbsp;&nbsp;
			当前第 ${page.pageNo } 页
		</span>
		
		<span class="pagelinks">
			<span id="pageLink1">
				[
				<a id="firstPage" href="emp-list2?page.pageNo=1">首页</a>
				/
				<a id="prevPage" href="emp-list2?page.pageNo=${page.pageNo - 1 }">上一页</a>
				] 
			</span>
			
			<span id="pagelist">
				转到 <input type="text" name="pageNo" size="1" height="1" class="logintxt"/> 页
			</span>
			
			<span id="pageLink2">
				[
				<a id="nextPage" href="emp-list2?page.pageNo=${page.pageNo + 1 }">下一页</a>
				/
				<a id="lastPage" href="emp-list2?page.pageNo=${page.totalPages }">末页</a>
				] 
			</span>			
			
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
			
			<tbody id="tbody">
			<s:iterator value="page.content">
				<tr>
					<td><a id="loginname" href="">${requestScope.loginName }</a></td> 
					<td>${employeeName }</td>
					
					<td>${enabled == 1 ? '允许':'禁止' }</td>
					<td>${department.departmentName }</td>
					
					<td>
						<s:date name="birth" format="yyyy-MM-dd"/>
					</td>
					<td>${gender == 1 ? '男':'女' }</td>
					
					<td><a id="email" href="">${email }</a></td>
					<td>${mobilePhone }</td>
					
					<td>${visitedTimes }</td>
					<td id="delete-${employeeId }">${isDeleted == 1 ? '删除':'正常' }</td>
					
					<td>${roleNames }</td>
					<td>
						<a href="emp-input?id=${employeeId }">修改</a>
						&nbsp;
						<span>
							<s:if test="isDeleted == 1">删除</s:if>
							<s:else>
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