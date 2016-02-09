package com.atguigu.ems.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

//日期转换器类
public class EMSDateConverter extends StrutsTypeConverter {
	private SimpleDateFormat simpleDateFormat;
	{
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public Object convertFromString(Map arg0, String[] arg1, Class arg2) {
		if (arg1 != null && arg1.length > 0) {
			try {
				return simpleDateFormat.parseObject(arg1[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String convertToString(Map arg0, Object arg1) {
		if (arg1 != null && arg1 instanceof Date) {
			return simpleDateFormat.format((Date) arg1);
		}
		return null;
	}
}
