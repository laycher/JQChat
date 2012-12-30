
package com.jq.client.protocol.tcp;

/** 离线消息具体服务
 * 继承自AbstractService
 * 实现于Service<String,String>接口*/
public class LeftInfoService extends AbstractService implements Service<String,String>{

	/** 离线消息的构造函数 */
	public LeftInfoService(TCPServer TCP) {
		super(TCP);
	}

	/**
	 * 离线消息具体方法
	 * @param msg 给好友发送的信息.
	 * */
	public String service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_LEFTINFO + msg);//发送登录资料
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		
		return null;
	}

}
