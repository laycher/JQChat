
package com.jq.client.protocol.tcp;

import java.io.IOException;

/**
 *  用户下线后向服务器发送下线消息.
 *  自动发送,自动关闭流.
 *  继承自AbstractService
 *  实现于Service<String,String>接口
 * */
public class LogoutService extends AbstractService implements Service<String, String>{
	
	/** 下线的构造函数 */
	public LogoutService(TCPServer TCP) {
		super(TCP);
	}
	
	/**
	 * 下线的具体方法
	 * @param msg 用户ID
	 * */
	public String service(String msg) {
		//注销格式 ID
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_LOGOUT + msg);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally{
			TCP.close();
		}
		
		return null;
	}
}
