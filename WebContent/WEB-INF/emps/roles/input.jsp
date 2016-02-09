<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="${ctp }css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp }css/input.css">

<script type="text/javascript" src="${ctp }script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${ctp }script/jquery.validate.js"></script>

<script type="text/javascript" src="${ctp }script/messages_cn.js" ></script>
<script type="text/javascript" src="${ctp }script/jquery.blockUI.js" ></script>

<script type="text/javascript">
	$(function(){
		$("#parentAuthroities").change(function(){
			$("p[class^='parentAuthority-']").hide();
			var val = $(this).val();
			$(".parentAuthority-" + val).show();
		});
		
		$(":checkbox").click(function(){
			var flag = $(this).is(":checked");
			
			if(flag){
				var relatedAuthorites = $(this).attr("class");
				var ids = relatedAuthorites.split(",");
				
				for(var i = 0; i < ids.length; i++){
					if(ids[i] != ""){
						$(":checkbox[value='" + ids[i] + "']").attr("checked", true);
					}
				}
			}else{
				var val = $(this).val();
				$(":checkbox[class*='," + val + ",']").attr("checked", false);
			}
		});
	})
</script>

</head>
<body>

	<br>
	<s:form action="/role-save" id="employeeForm" cssClass="employeeForm">	
		<fieldset>
			<p>
				<label for="name">角色名(必填*)</label>
				<s:textfield name="roleName" cssClass="required"></s:textfield>
			</p>
			
			<p>
				<label for="authority">授予权限(必选)</label>
				<input type="checkbox" name="authorities2" class="required" style="display:none"/>
			</p>
			
			<p>
				<label>权限名称(必填)</label>
				<!-- 父权限 -->
				<s:select name="parentAuthroities" list="#request.parentAuthroities"
					listKey="id" listValue="displayName"
					headerKey="" headerValue="请选择..."
					id="parentAuthroities"></s:select>
			</p>
			
			<s:checkboxlist list="#request.authorities"
				name="authorities"
				listKey="id" listValue="displayName"
				cssStyle="border:none"
				templateDir="/ems/template"
				listCssClass="relatedAuthorites"></s:checkboxlist>
				
			<p>
				<input class="submit" type="submit" value="Submit"/>
			</p>
			
		</fieldset>
		
	</s:form>

</body>
</html>