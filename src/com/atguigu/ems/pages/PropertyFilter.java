package com.atguigu.ems.pages;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class PropertyFilter {
	// GT大于，GE大于等于，LT小于，LE小于等于，EQ等于，LIKE相似，ISNULL不为空
	public enum MatchType {
		EQ, LT, LE, GT, GE, LIKE, ISNULL;
	}

	public enum PropertyType {
		// 枚举的对象必须放在第一行
		// 调用枚举类带参数的构造器.
		I(Integer.class), L(Long.class), S(String.class), D(Date.class);

		private Class propertyType;

		private PropertyType(Class propertyType) {
			this.propertyType = propertyType;
		}

		public Class getPropertyType() {
			return propertyType;
		}
	}

	private String propertyName;
	private Object propertyVal;

	private MatchType matchType;
	private Class propertyType;

	public String getPropertyName() {
		return propertyName;
	}

	public Object getPropertyVal() {
		return propertyVal;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public Class getPropertyType() {
		return propertyType;
	}

	public PropertyFilter(String propertyName, Object propertyVal,
			MatchType matchType, Class propertyType) {
		this.propertyName = propertyName;
		this.propertyVal = propertyVal;
		this.matchType = matchType;
		this.propertyType = propertyType;
	}

	public static List<PropertyFilter> parseParamsToFilters(
			Map<String, Object> params) {
		List<PropertyFilter> filters = new ArrayList<>();

		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> param : params.entrySet()) {
				String key = param.getKey(); // LIKES_loginName
				Object propertyValue = param.getValue();

				String str1 = StringUtils.substringBefore(key, "_"); // LIKES
				String matchTypeCode = str1.substring(0, str1.length() - 1); // LIKE
				String propertyTypeCode = str1.substring(str1.length() - 1); // S

				MatchType matchType = Enum.valueOf(MatchType.class,
						matchTypeCode);
				Class propertyType = Enum.valueOf(PropertyType.class,
						propertyTypeCode).getPropertyType();

				String propertyName = StringUtils.substringAfter(key, "_"); // loginName

				PropertyFilter propertyFilter = new PropertyFilter(
						propertyName, propertyValue, matchType, propertyType);
				filters.add(propertyFilter);
			}
		}

		return filters;
	}

	@Override
	public String toString() {
		return "\nPropertyFilter [propertyName=" + propertyName
				+ ", propertyVal=" + propertyVal + ", matchType=" + matchType
				+ ", propertyType=" + propertyType + "]";
	}
}
