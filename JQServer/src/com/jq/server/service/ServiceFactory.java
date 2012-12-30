package com.jq.server.service;

import java.lang.reflect.Constructor;

import com.jq.server.service.Service;

/**
 * 获取对应服务的服务工厂的代码.
 * 
 * [服务器核心类.] 用于返回处理各种客户端要求执行的操作.
 * 
 * */

public class ServiceFactory {

	/** 登陆  LoginService */
	public static final String TASK_LOGIN = "0";
	/** 注销 LogoutService */
	public static final String TASK_LOGOUT = "1";
	/** 新用户注册 RegisterService */
	public static final String TASK_NEWUSER = "2";
	/** 更新用户资料 UpdateInfoService */
	public static final String TASK_UPDATAINFO = "3";
	/** 查看好友资料 FriendInfoService */
	public static final String TASK_GETINFO = "4";
	/** 搜索好友 SearchService  */
	public static final String TASK_SEARCH = "5";
	/** 添加好友 AddtionService */
	public static final String TASK_ADDTION = "6";
	/** 删除好友 RemoveFriendService */
	public static final String TASK_REMOVE = "7";
	/** 离线消息 LeftInfoService */
	public static final String TASK_LEFTINFO = "8";
	/** 获取更改密码 PasswordService */
	public static final String TASK_PASSWORD = "9";
	/** 成功标示符 */
	public static final String SUCCESS = "SUCCESS";
	/** 失败标示符 */
	public static final String ERROR = "ERROR";
	/** 用户已登录标示符 */
	public static final String LOGINED = "9";

	private static final String[] TYPE = { "com.jq.server.service.LoginService",
			"com.jq.server.service.LogoutService",
			"com.jq.server.service.RegisterService",
			"com.jq.server.service.UpdateInfoService",
			"com.jq.server.service.FriendInfoService",
			"com.jq.server.service.SearchService",
			"com.jq.server.service.AddtionService",
			"com.jq.server.service.RemoveFriendService",
			"com.jq.server.service.LeftInfoService",
			"com.jq.server.service.PasswordService" };

	/** 利用反射使用指定带参构造方法创建指定名称类的对象 
	 * @return Service */
	public static Service getService(String msg) {
		/* 解析--->消息类型 */
		int type = Integer.parseInt(msg.substring(0, 1));
		/* 解析--->ID_信息体 */
		msg = msg.substring(1);
		/* 使用反射机制加载相关类 */
		String className = TYPE[type];
		Class<?> c;
		@SuppressWarnings("rawtypes")
		Constructor constructor;
		try {
			//加载指定名称的类，获取相应的Class对象
			c = Class.forName(className);
			//获取具有指定参数类型的构造方法 
			constructor = c.getConstructor(String.class);// String.class 的类型是
															// Class<String>
			//给指定的构造方法传入参数值，创建出一个对象
			Service s = (Service) constructor.newInstance(msg);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
