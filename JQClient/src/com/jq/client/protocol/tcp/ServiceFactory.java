
package com.jq.client.protocol.tcp;

import java.lang.reflect.Constructor;

/**获取对应服务的服务工厂的代码.*/
public class ServiceFactory {
	/**登陆*/
	public static final String TASK_LOGIN = "0";
	/**注销*/
	public static final String TASK_LOGOUT = "1";
	/**新用户注册*/
	public static final String TASK_NEWUSER = "2";
	/**更新用户资料*/
	public static final String TASK_UPDATAINFO = "3";
	/**查看好友资料*/
	public static final String TASK_GETINFO = "4";
	/**搜索好友*/
	public static final String TASK_SEARCH = "5";
	/**添加好友*/
	public static final String TASK_ADDTION = "6";
	/**删除好友*/
	public static final String TASK_REMOVE = "7";
	/**离线消息*/
	public static final String TASK_LEFTINFO = "8";
	/**获取更改密码*/
	public static final String TASK_PASSWORD = "9";
	/**其他更新，待扩充*/
	//public static final String TASK_OTHER= "10";
	
	private static final String[] TYPE = {
			"com.jq.client.protocol.tcp.LoginService",
			"com.jq.client.protocol.tcp.LogoutService",
			"com.jq.client.protocol.tcp.RegisterService",
			"com.jq.client.protocol.tcp.UpdateInfoService",
			"com.jq.client.protocol.tcp.FriendInfoService",
			"com.jq.client.protocol.tcp.SearchService",
			"com.jq.client.protocol.tcp.AddtionService",
			"com.jq.client.protocol.tcp.RemoveFriendService",
			"com.jq.client.protocol.tcp.LeftInfoService",
			"com.jq.client.protocol.tcp.PasswordService" };

	/** 利用反射使用指定带参构造方法创建指定名称类的对象 
	 * @return Service */
	public static Service<?,?> getService(String serviceType,String IP,int port){
		Class<?> c;
		Constructor<?> constructor;
		/* 使用反射机制加载相关类 */
		String className = TYPE[Integer.parseInt(serviceType)];
		try {
			//加载指定名称的类，获取相应的Class对象
			c = Class.forName(className);
			/*带参数的构造器.*/
			//获取具有指定参数类型的构造方法 
			constructor = c.getConstructor(TCPServer.class);
			//给指定的构造方法传入参数值，创建出一个对象
			return (Service<?,?>) constructor.newInstance(new TCPServer(IP,port));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
