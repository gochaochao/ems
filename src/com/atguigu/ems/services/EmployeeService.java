package com.atguigu.ems.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.ems.daos.DepartmentDao;
import com.atguigu.ems.daos.RoleDao;
import com.atguigu.ems.entities.Department;
import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.entities.Role;
import com.atguigu.ems.exceptions.EmployeeUnableException;
import com.atguigu.ems.exceptions.LoginNameException;
import com.atguigu.ems.exceptions.LoginNameNotFoundException;
import com.atguigu.ems.exceptions.LoginNameNotMatchPasswordException;

@Service
public class EmployeeService extends BaseService<Employee> {
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private RoleDao roleDao;

	// 文件上传
	@Transactional
	public List<String[]> upload(File file) throws Exception {
		// 1.解析File为一个Workbook对象
		InputStream inp = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(inp);

		// 2.得到Sheet对象
		Sheet sheet = wb.getSheet("employees");

		// 3.解析数据：解析每一行，每一个单元格，把一行转为一个Employee对象，把整个Excel为一个Employee集合
		// 若出现错误，则把错误返回
		List<Employee> employees = new ArrayList<>();
		List<String[]> errors = parseToEmployeeList(sheet, employees);

		if (errors != null && errors.size() > 0) {
			return errors;
		}
		// 4.批量插入
		baseDao.batchSave(employees);

		// 5.返回错误
		return null;
	}

	// 文件上传解析数据
	private List<String[]> parseToEmployeeList(Sheet sheet,
			List<Employee> employees) {
		List<String[]> errors = new ArrayList<>();

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			Employee employee = parseToEmployee(row, i + 1, errors);

			if (employee != null) {
				employees.add(employee);
			}
		}
		return errors;
	}

	private Employee parseToEmployee(Row row, int i, List<String[]> errors) {
		// 登录名 姓名 性别 登录许可 部门 E-mail 角色
		Employee employee = null;
		// 获取 loginName, 并对其进行验证
		Cell cell = row.getCell(1);
		String loginName = getCellValue(cell);
		boolean flag = validateLoginName(loginName);
		if (!flag) {
			errors.add(new String[] { i + "", "2" });
		}

		cell = row.getCell(2);
		String employeeName = getCellValue(cell);

		cell = row.getCell(3);
		String gender = getCellValue(cell);
		gender = validateGender(gender);
		if (gender == null) {
			errors.add(new String[] { i + "", "4" });
			flag = false;
		}

		cell = row.getCell(4);
		String enabledStr = getCellValue(cell);
		int enabled = validateEnabled(enabledStr);
		if (enabled == -1) {
			errors.add(new String[] { i + "", "5" });
			flag = false;
		}

		cell = row.getCell(5);
		String departmentName = getCellValue(cell);
		Department department = validateDepartment(departmentName);
		if (department == null) {
			errors.add(new String[] { i + "", "6" });
			flag = false;
		}

		cell = row.getCell(6);
		String email = getCellValue(cell);
		if (!valiateEmail(email)) {
			errors.add(new String[] { i + "", "7" });
			flag = false;
		}

		cell = row.getCell(7);
		String roleNames = getCellValue(cell);
		Set<Role> roles = validateRoles(roleNames);
		if (roles == null || roles.size() == 0
				|| roles.size() != roleNames.split(",").length) {
			errors.add(new String[] { i + "", "8" });
			flag = false;
		}

		if (flag) {
			employee = new Employee(loginName, employeeName, roles, enabled,
					department, gender, email);
		}

		return employee;
	}

	// 把 roleNames 转为字符串数组, 并解析返回 Role 的集合
	private Set<Role> validateRoles(String roleNames) {
		Collection<Role> roles = roleDao.getByForSet("roleName",
				roleNames.split(","));
		return new HashSet<>(roles);
	}

	// 验证 email 是否合法
	private boolean valiateEmail(String email) {
		Pattern pattern = Pattern
				.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	// 验证部门是否存在
	private Department validateDepartment(String departmentName) {
		return departmentDao.getBy("departmentName", departmentName);
	}

	// 验证 enbled 是 1 或 0
	private int validateEnabled(String enabledStr) {
		try {
			double d = Double.parseDouble(enabledStr);
			if (d == 1.0d || d == 0.0d) {
				return (int) d;
			}
		} catch (NumberFormatException e) {
		}

		return -1;
	}

	// 验证 gender 是 1 或 0.
	// 在 Excel 中输入的值有可能是一个数字, 也有可能是一个字符串.
	// 即值可能是一个 1 也有可能是 1.0
	private String validateGender(String gender) {
		try {
			double d = Double.parseDouble(gender);
			if (d == 1.0d || d == 0.0d) {
				return (int) d + "";
			}
		} catch (NumberFormatException e) {
		}

		return null;
	}

	private boolean validateLoginName(String loginName) {
		if (loginName != null && !loginName.trim().equals("")
				&& loginName.trim().length() >= 6) {
			// 正则验证
			Pattern p = Pattern.compile("^[a-zA-Z]\\w+\\w$");
			Matcher m = p.matcher(loginName.trim());
			boolean b = m.matches();

			// 验证 loginName 是否合法: 是否在数据表中有对应的记录
			if (b) {
				Employee employee = baseDao
						.getBy("loginName", loginName.trim());
				if (employee == null) {
					return true;
				}
			}
		}
		return false;
	}

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().getString();
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				return dateFormat.format(date);
			} else {
				return cell.getNumericCellValue() + "";
			}
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula() + "";
		}

		return null;
	}

	@Transactional(readOnly = true)
	public void downLoad(String excelFileName) throws Exception {
		Workbook wb = new XSSFWorkbook(); // or new HSSFWorkbook();
		Sheet sheet = wb.createSheet("employees");
		// 1. 填充标题行.
		createTitleRow(sheet);
		// 2. 填充 Employee 信息
		createEmployeeRows(sheet);
		// 3. 设置样式: 包括行高, 列宽, 边框.
		setCellStyle(wb);
		// 4. 把 Excel 保存到磁盘上.
		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		wb.write(fileOut);
		fileOut.close();
	}

	private CellStyle getCellStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());

		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());

		return style;
	}

	private void setCellStyle(Workbook wb) {
		Sheet sheet = wb.getSheet("employees");

		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			row.setHeightInPoints(25);

			for (int j = 0; j < TITLES.length; j++) {
				Cell cell = row.getCell(j);
				cell.setCellStyle(getCellStyle(wb));
			}
		}

		for (int j = 0; j < TITLES.length; j++) {
			sheet.autoSizeColumn(j);
			sheet.setColumnWidth(j, (int) (sheet.getColumnWidth(j) * 1.3));
		}
	}

	private void createEmployeeRows(Sheet sheet) {
		List<Employee> emps = baseDao.getAll();
		for (int i = 0; i < emps.size(); i++) {
			Employee emp = emps.get(i);
			Row row = sheet.createRow(i + 1);

			Cell cell = row.createCell(0);
			cell.setCellValue("" + (i + 1));

			cell = row.createCell(1);
			cell.setCellValue(emp.getLoginName());

			cell = row.createCell(2);
			cell.setCellValue(emp.getEmployeeName());

			cell = row.createCell(3);
			cell.setCellValue(emp.getGender());

			cell = row.createCell(4);
			cell.setCellValue(emp.getEnabled());

			cell = row.createCell(5);
			cell.setCellValue(emp.getDepartment().getDepartmentName());

			cell = row.createCell(6);
			cell.setCellValue(emp.getEmail());

			cell = row.createCell(7);
			cell.setCellValue(emp.getRoleNames());
		}
	}

	// 序号 登录名 姓名 性别 登录许可 部门 E-mail 角色
	private static final String[] TITLES = new String[] { "序号", "登录名", "姓名",
			"性别", "登录许可", "部门", "E-mail", "角色" };

	private void createTitleRow(Sheet sheet) {
		Row row = sheet.createRow(0);
		for (int i = 0; i < TITLES.length; i++) {
			row.createCell(i).setCellValue(TITLES[i]);
		}
	}

	@Transactional
	public void save(Employee employee, String oldLoginName) {
		// save
		if (employee.getEmployeeId() == null) {
			employee.setPassword("123456");
			employee.setVisitedTimes(0);
		}
		// 保存时，检验用户名是否可用
		// Employee employee2 = employeeDao.getByLoginName(employee
		// .getLoginName());
		// if (employee2 != null) {
		// throw new LoginNameException();
		// }
		// update
		if (employee.getEmployeeId() == null
				|| !employee.getLoginName().equals(oldLoginName)) {
			if (employee.getEmployeeId() != null) {
				baseDao.evict(employee);
			}
			Employee employee2 = baseDao.getBy("loginName",
					employee.getLoginName());
			if (employee2 != null) {
				throw new LoginNameException();
			}
		}
		baseDao.save(employee);
	}

	@Transactional
	public int delete(Integer id) {
		// 1.判断id对应的Employee是否是Department的一个Manager
		Employee manager = new Employee();
		manager.setEmployeeId(id);
		Department department = departmentDao.getBy("manager", manager);
		if (department != null) {
			return 1;
		}
		// 2.若不是Manager，则把isDeleted字段修改为1
		Employee employee = baseDao.get(id);
		employee.setIsDeleted(1);
		return 2;
	}

	@Transactional
	public Employee login(String loginName, String password) {
		Employee employee = baseDao.getBy("loginName", loginName);
		if (employee == null) {
			throw new LoginNameNotFoundException("用户名" + loginName + "不存在!");
		}
		if (!employee.getPassword().equals(password)) {
			throw new LoginNameNotMatchPasswordException("用户名" + loginName
					+ "和密码不匹配!");
		}
		if (employee.getEnabled() != 1) {
			throw new EmployeeUnableException("用户" + loginName + "不可用!");
		}
		// 使登录次数+1
		employee.setVisitedTimes(employee.getVisitedTimes() + 1);
		return employee;
	}
}
