package com.atguigu.ems.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.WebUtils;

import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.exceptions.LoginNameException;
import com.atguigu.ems.exceptions.LoginNameNotFoundException;
import com.atguigu.ems.exceptions.LoginNameNotMatchPasswordException;
import com.atguigu.ems.pages.EmployeeCrieriaFormBean;
import com.atguigu.ems.services.DepartmentService;
import com.atguigu.ems.services.EmployeeService;
import com.atguigu.ems.services.RoleService;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope(value = "prototype")
public class EmployeeAction extends BaseAction<Employee> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DepartmentService departmentService;

	// 文件上传相关
	private File file;

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 如何实现和具体实体类无关的带查询条件的分页 ? 1. 页面的查询条件如何传过来 ? 或者说 传过来是什么样子 ? 通过表单域传递查询条件. 即通过
	 * input 传递。 <input type="text" name="" value=""/> 一般地, name 可以指定和哪个字段相比较.
	 * value 可以指定比较的值是多少.
	 * 
	 * value 不能做任何的修改, 修改 name 属性值. 比较的方式比较的类型_比较的属性的名字. LIKES_loginName EQI_age
	 * GTL_salary
	 * 
	 * 这也意味着需要定义一个类来包装查询条件.
	 * 
	 * class PropertyFilter{
	 * 
	 * private String propertyName; private Object propertyVal;
	 * 
	 * private MatchType matchType; private PropertyType propertyType;
	 * 
	 * enum MatchType{ LIKE, EQ, GT, GE, LT, LE, ISNULL; }
	 * 
	 * enum PropertyType{ I(Ingeger.class), L(Long.class), S(String.class),
	 * F(Float.class), D(Date.class);
	 * 
	 * private Class propertyType;
	 * 
	 * PropertyType(Class propretyType){ this.propertyType = propertyType; }
	 * 
	 * Class getPropertyType(){ return this.propretyType; } } }
	 * 
	 * 2. 在 Action 的方法中, 如何来获取查询条件对应的字段 ? 不能一个一个的调用 getParameter 方法.
	 * 于是把所有查询条件的字段的 name 属性值前添加一个前缀: filter_. 即 <input type="text"
	 * name="filter_LIKES_loginName" value=""/> 在 Action 中获取指定前缀的请求参数即可.
	 * 
	 * 3. 如何把页面传过来的参数转为一个一个的查询条件 ? 页面上的一个 input 标签应该能够对应一个查询条件! 一个查询条件必须的要素有什么 ?
	 * 和哪个字段比较. 即实体类的属性名. 怎么比. 例如 > 还是 < 还是 = 还是 LIKE 获取其他 比较的值是什么. 比较的值的类型是什么.
	 * 因为页面上传入的是字符串, 所以可能需要把字符串转为指定的类型.
	 * 
	 * 4. Dao 方法如何进行分页 ? 步骤: 1). 在 Action 中调用
	 * WebUtils#getParameterStartingWith() 方法来获取指定前缀的请求参数对应的 Map 2). 把 Map 转为
	 * PropertyFilter 的集合. 3). 把 PropertyFilter 的集合转为 Criterion 的集合. 4). 在 Dao
	 * 中使用 QBC 完成分页.
	 * 
	 * 5. 分页实现后, 如何在点击 "上一页" 等连接时能够保持查询条件 ? 在 Action 中把查询条件对应的 Map 转为一个查询字符串,
	 * 例如: filter_LIKES_LoginName=a&filter_GTI_age=23 把该字符串传回到页面上.
	 * 在页面的翻页的超链接后面附着该查询字符串.
	 * 
	 */
	// 查询条件页面获取用户填写的参数
	public String list3() {
		HttpServletRequest req = ServletActionContext.getRequest();
		// 得到的 Map 的键已经 remove 了前缀.
		// {EQI_department.departmentId=1, EQI_enabled=1, EQS_gender=1,
		// LIKES_email=c, LIKES_employeeName=b, LIKES_loginName=a}
		Map<String, Object> params = WebUtils.getParametersStartingWith(req,
				"filter_");
		page = service.getPage(page, params);
		request.put("page", page);
		// 把 params 对应的 Map 再转为一个查询字符串.
		// filter_EQI_department.departmentId=1&filter_EQI_enabled=1&filter_EQS_gender=1
		String queryString = parseParamMapToQueryString(params);
		request.put("queryString", queryString);
		return "list";
	}

	// 解析查询条件的Map为String并发送给页面
	public String parseParamMapToQueryString(Map<String, Object> params) {
		if (params != null && params.size() > 0) {
			StringBuilder queryString = new StringBuilder();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				Object val = entry.getValue();

				queryString.append("filter_").append(key).append("=")
						.append(val).append("&");
			}
			queryString.replace(queryString.length() - 1, queryString.length(),
					"");
			return queryString.toString();
		}
		return "";
	}

	public void prepareCriteriaInput() {
		// 创建一个 EmployeeCrieriaFormBean 对象.
		// 并把其放到值栈的栈顶.
		// 需要手工放. 而不能使用 ModelDriven. 因为 ModelDriven 是放 Model 的
		ActionContext.getContext().getValueStack()
				.push(new EmployeeCrieriaFormBean());
	}

	// 显示查询条件的页面
	public String criteriaInput() {
		request.put("departments", departmentService.getAll());
		return "criteria-input";
	}

	// 下载模板
	public String uploadTemplateDownload() throws Exception {
		// 初始化文件下载的相关属性
		contentType = "application/vnd.ms-excel";
		// 动态获取路径

		String excelFileName = ServletActionContext.getServletContext()
				.getRealPath("/files/employees.xls");
		inputStream = new FileInputStream(excelFileName);
		// inputStream = new FileInputStream("F:\\yezi.xls");
		contentLength = inputStream.available();
		fileName = "employee.xml";
		return "excel-result";
	}

	public String uploadUI() {
		return "emp-upload";
	}

	// Excel文件上传
	public String upload() throws Exception {
		// 解析上传的Excel文件，并把其中的数据传到服务端
		List<String[]> errors = employeeService.upload(file);
		if (errors != null && errors.size() > 0) {
			StringBuilder errorMessages = new StringBuilder();
			for (String[] error : errors) {
				String errorMessage = getText("error.employee.upload", error);
				errorMessages.append(errorMessage);
			}
			addActionError(errorMessages.toString());
			return "upload-error";
		}
		return "upload-success";
	}

	// 下载Excel文档
	public String downToExcel() throws Exception {
		// 初始化文件下载的相关属性
		contentType = "application/vnd.ms-excel";
		// 动态获取路径

		String excelFileName = ServletActionContext.getServletContext()
				.getRealPath("/files/" + System.currentTimeMillis() + ".xls");
		employeeService.downLoad(excelFileName);
		inputStream = new FileInputStream(excelFileName);
		// inputStream = new FileInputStream("F:\\yezi.xls");
		contentLength = inputStream.available();
		fileName = "employee.xml";
		return "excel-result";
	}

	public void prepareSave() {
		if (id == null) {
			model = new Employee();
		} else {
			model = employeeService.get(id);
			// 将角色的集合清空，才能再修改角色
			model.getRoles().clear();
			// 把department置为null，才能修改部门
			model.setDepartment(null);
		}
	}

	public String save() {
		String oldLoginName = "";
		try {
			String[] vals = parameters.get("oldLoginName");
			if (vals != null && vals.length == 1) {
				oldLoginName = vals[0];
			}
			employeeService.save(model, oldLoginName);
		} catch (LoginNameException e) {
			addFieldError(
					"loginName",
					getText("error.employee.save.loginName",
							Arrays.asList(model.getLoginName())));
			return "emp-save-input";
		}
		return "emp-success";
	}

	// 员工录入的登录名AJAX验证
	public void validateLoginName() throws IOException {
		String result = "2";
		String loginName = parameters.get("loginName")[0];
		try {
			employeeService.login(loginName, "");
		} catch (LoginNameNotFoundException e) {
			result = "1";
		} catch (LoginNameNotMatchPasswordException e) {
			result = "0";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(result);
	}

	public void prepareInput() {
		if (id != null) {
			model = employeeService.get(id);
		}
	}

	// 员工信息录入
	public String input() {
		request.put("departments", departmentService.getAll());
		request.put("roles", roleService.getAll());
		return "emp-input";
	}

	// AJAX直接返回标记位，所以该方法没有返回值，直接把结果print回去
	public void delete() throws IOException {
		String result = "0";
		result = employeeService.delete(id) + "";
		// 直接返回标记位:1,2,其他
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(result);
	}

	public String list() {
		this.page = employeeService.getPage(page);
		return "list";
	}

	public void prepareLogin() {
		model = new Employee();
	}

	// 登录验证
	public String login() {
		String loginName = model.getLoginName();
		String password = model.getPassword();
		Employee employee = employeeService.login(loginName, password);
		session.put("employee", employee);
		return "success";
	}
}
