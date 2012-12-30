
package com.jq.util;

/** 系统的各种参数 */
public class Param {
	/**空格*/
	public static String SPACE = " ";
	/**平台无关--换行*/
	public static final String NEWLINE = System.getProperty("line.separator");
	/**IP正则表达式*/
	public static final String IP_REGEX = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])){3}$";
	/**检查字符串包含#dd.gif格式表情的正则表达式*/
	public static final String GIF_REGEX = ".*\\d\\d\\.gif.*";
	
	/**分页每页记录行数*/
	public static final int PAGE_LINES = 50;
	
	/**显示字体，默认字体*/
	public static final String FONT = "微软雅黑";
	/***/
	//public static final String DEFAULT_FONT = "微软雅黑"; 
	/**.GIF*/
	public static final String GIF = ".gif";
	/**默认客户端端口*/
	public static final String CLIENT_PORT = String.valueOf(IPInfo.getClientPort());//"8000";
	/**服务器端口*/
	public static final String SERVER_PORT = String.valueOf(IPInfo.getServerPort());//"6000";
	/**服务器IP地址*/
	public static final String SERVER_IP = IPInfo.getIP();// "127.0.0.1";

	/**数据包字节大小*/
	public static final int DATA_SIZE = 1024;
	/**当前路径*/
	public static final String CURRENTPATH = System.getProperties().getProperty(("user.dir"));
	/**斜体*/
	public static final String ITALIC = "/";
}
