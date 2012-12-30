package com.jq.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.jq.util.Param;

/**
 * [配置文件读取类] 
 * Properties对象读取数据库配置文件, 通过PropertyFile对象可以获得详细内容.
 * 包括mySQL数据库用户名称,密码.JDBC驱动程序,URL等 
 * 实际情况中应对账号,密码等敏感信息进行加密****
 * 
 */
public class PropertyFile {
	
	private Properties prop = null;
	
	private String url; // url
	private String user; // mySQL用户名user
	private String password; // mySQL用户密码
	private String driver; // mySQL连接驱动
	private int max; // 连接池中最大Connection数目

	/**
	 *@param filePath Properties文件的路径
	 */
	public PropertyFile(String filePath) {	
		prop = new Properties();

		try {
			prop.load(new FileInputStream(new File(filePath)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 读取Properties详细信息
		driver = prop.getProperty(Param.DRIVER);
		url = prop.getProperty(Param.URL);
		user = prop.getProperty(Param.USER);
		password = prop.getProperty(Param.PASSWORD);
		max = Integer.parseInt(prop.getProperty(Param.POOLMAX));
	}

	/** 返回URL */
	public String getURL() {
		return url;
	}

	/** 返回账号User */
	public String getUser() {
		return user;
	}

	/** 返回密码Password	 */
	public String getPassword() {
		return password;
	}
	
	/** 返回mySQL驱动 driver */
	public String getDriver() {
		return driver;
	}

	/** 返回连接池最大连接数目	 */
	public int getMax() {
		return max;
	}
}