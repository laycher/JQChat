package com.jq.util;

/** 配置文件中的默认参数 */
public class Param {

	public static String DRIVER = "driver";
	public static String URL = "url";
	public static String USER = "user";
	public static String PASSWORD = "password";
	public static String POOLMAX = "poolmax";

	public static String SPACE = " ";
	/** 平台无关--换行 */
	public static final String NEWLINE = System.getProperty("line.separator");
	/** 当前路径 */
	public static final String CURRENTPATH = System.getProperties()
			.getProperty(("user.dir")) + "/properties/";	//user.dir 用户的当前工作目录 
	/** 数据库配置文件名称 */
	public static final String FILENAME = "jdbc config.properties";
	/** 默认数据库配置文件内容 */
	public static final String PROPERTYFILE = "driver=com.mysql.jdbc.Driver"
			+ NEWLINE + "url=jdbc:mysql://localhost:3306/jq_database" 
			+ NEWLINE + "user=root" 
			+ NEWLINE + "password=123456" 
			+ NEWLINE + "poolmax=10";

}
