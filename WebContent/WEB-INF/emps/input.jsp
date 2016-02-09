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
<link rel="stylesheet" type="text/css" href="${ctp }css/weebox.css">
 
<link rel="stylesheet" type="text/css" href="${ctp }script/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctp }script/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctp }css/default.css">

<script type="text/javascript" src="${ctp }script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${ctp }script/jquery.validate.js"></script>

<script type="text/javascript" src="${ctp }script/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctp }script/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" src="${ctp }script/messages_cn.js"></script>
<script type="text/javascript" src="${ctp }script/bgiframe.js"></script>
<script type="text/javascript" src="${ctp }script/weebox.js"></script>

<script type="text/javascript">

	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}
	function myparser(s){
		if (!s) return new Date();
		var ss = (s.split('-'));
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	}

	$(function(){
		//使用Weebox显示所有角色
		$("#role_a_id").click(function(){
			$.weeboxs.open('#rolebox', {
				title:'分配角色',
				onopen:function() {
					//把有name属性值的checkbox的选择状态，复制到对应的没有name值的checkbox
					$(":checkbox[name='roles2']").each(function(index){
						var checked=$(this).attr("checked");
						$($(":checkbox[name!='roles2']")[index]).attr("checked",checked);
					});
				},
				onok:function(box){
					//把没有name属性值的checkbox的选择状态，复制到对应的有name值的checkbox
					$(":checkbox[name!='roles2']").each(function(index){
						var checked=$(this).attr("checked");
						$($(":checkbox[name='roles2']")[index]).attr("checked",checked);
					});
					box.close();//增加事件方法后需手动关闭弹窗
				}
			});
			return false;
		});
		
		//jQuery的validation验证框架
		$("#employeeForm").validate();
		
		//AJAX检验
 		$("input[name='loginName']").change(function(){
			var val=this.value;
			val=$.trim(val);
			this.value=val;
			
			var oldLoginName=$("#oldLoginName").val();
			if(val=oldLoginName){
				alert("该用户名可用");
				return;
			}
			
			var reg=/^[a-zA-Z]\w+\w$/g;
			if(val.length<6 || val==null || !reg.test(val)){
				//alert("输入的登录名不合法！");
				//处理完事件，鼠标焦点再回到原处
				//$(this).focus();
				return;
			} 
			
			//进行AJAX验证
			var url="${ctp}emp-validateLoginName";
			var args={"loginName":val,"time":new Date()};
			$.post(url,args,function(data){
				if(data=="1"){
					alert("该登录名可用");
				}else if(data=="0"){
					alert("该用户名不可用");
				}else{
					alert("刷新页面后重试");
				}
			});
		});
	});
</script>

</head>

<!--  
1. 修改 radio 的模板文件, 使其移除 label
2. 通过刷新页面, 检查 Role 和 Department 的二级缓存是否起作用.
3. loginName 可用性的 ajax 检验.
4. 使用 jQuery 的 validation 验证框架完成表单的前端验证.
5. 使用 jQuery 的 weebox 插件来显示角色. 
6. 集合属性的赋值. 
6.1 为 roles 字段添加 roles2 的 setter 和 getter
6.2 手工添加的 addFieldError, 错误消息被保存到 国际化资源文件 中.
6.3 出错时, 使用 chain 结果类型, 且需要修改 chain interceptor 的 copyFieldError 属性为 true
6.4 Service 的 save 方法中检查 loginName 的可用性. 
-->

<!--  
修改.
1. 表单回显的原理
2. 对于修改, 创建人显示部分需要做特殊处理. 
3. 修改状态下, 如何来验证 loginName. 
3.1 Ajax 检验
3.2 服务端检验. 必须把 oldLoginName 也传入到服务端. 
4. 修改时, 需要传递 id. 
5. 提交修改后的 prepareSave 方法中, 需要把 employee 的 roles 集合属性置空, 否则会出现主键重复问题. 
-->

<body>
	
	<br>
	<s:form action="/emp-save" method="POST" id="employeeForm" cssClass="employeeForm">
		<s:if test="employeeId != null">
			<input type="hidden" name="id" value="${employeeId }"/>
			<s:if test="#prameters.oldLoginName==null">
				<input type="hidden" name="oldLoginName" id="oldLoginName" value="${loginName }">
			</s:if>
			<s:else>
				<input type="hidden" name="oldLoginName" id="oldLoginName" value="${param.oldLoginName }">
			</s:else>
		</s:if>
		<fieldset>
		
			<p>
				<label for="message">
					<font color="red">添加员工信息</font>
				</label> 
			</p>
			
			<p>
				<label for="loginName">登录名(必填)</label>
				<s:textfield name="loginName" cssClass="required" minlength="6"></s:textfield>
				<label id="loginlabel" class="error" for="loginname" generated="true">
				<!-- 显示服务器端简单验证的信息 -->
					<s:fielderror fieldName="loginName"></s:fielderror>
				</label>
			</p>
			
			<p>
				<label for="employeeName">姓名 (必填)</label>
				<s:textfield name="employeeName"></s:textfield>
			</p>
			
			<p>
				<label for="logingallow">登录许可 (必填)</label>
				<s:radio list="#{'1':'允许','0':'禁止' }" name="enabled" cssStyle="border:none"></s:radio>
			</p>

			<p>
				<label for="gender">性别 (必填)</label>
				<s:radio list="#{'1':'男','0':'女' }" name="gender" cssStyle="border:none"></s:radio>
			</p>
			
			<p>
				<label for="dept">部门 (必填)</label>
				<s:select list="#request.departments"
					listKey="departmentId" listValue="departmentName"
					name="department.departmentId"></s:select>

				<label class="error" for="dept" generated="true">
					<font color="red">
					<!-- 显示服务器端简单验证的信息 -->
					</font>
				</label>
			</p>
			
			<p>
				<label for="birth">生日 (必填)</label>
				<s:textfield name="birth" cssClass="easyui-datebox" data-options="formatter:myformatter,parser:myparser"></s:textfield>
			</p>
			
			<p>
				<label for="email">Email (必填)</label>
				<s:textfield name="email" cssClass="required email"></s:textfield>
				<label class="error" for="email" generated="true">
					<!-- 显示服务器端简单验证的信息 -->
				</label>
			</p>
			
			<p>
				<label for="mobilePhone">电话 (必填)</label>
				<s:textfield name="mobilePhone"></s:textfield>
			</p>

			<p>
				<label for="role"><a id="role_a_id" href="">分配角色(必选)</a></label>
			</p>
			
			<div style="display:none">
				<!-- 有name属性，用于提交的，同时也是用于保存选择信息的 --> 
				<s:checkboxlist list="#request.roles"
					listKey="roleId" listValue="roleName" name="roles2"></s:checkboxlist>
			</div>
			
			<div style="display:none" id="rolebox">
				<!-- 用于显示的，不需要提交，不设置name属性 -->
				<s:iterator value="#request.roles">
					<input type="checkbox" value="${roleId }" style="border:none"/>${roleName }
					<br>
				</s:iterator>
			</div>
			
			<p>
				<label for="mobilePhone">创建人</label>
				<s:if test="employeeId != null">
					${editor.loginName }
					<s:hidden name="editor.employeeId"></s:hidden>
				</s:if>
				<s:else>
					${sessionScope.employee.loginName }
					<input type="hidden" name="editor.employeeId" value="${sessionScope.employee.employeeId }"/>
				</s:else>
			</p>

			<p>
				<input class="submit" type="submit" value="提交"/>
			</p>
		</fieldset>
		
	</s:form>
	
</body>
</html>