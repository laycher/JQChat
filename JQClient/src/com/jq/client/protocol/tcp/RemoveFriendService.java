package com.jq.client.protocol.tcp;

/**移除好友具体服务
 * 继承自AbstractService
 * 实现于Service<String,String>接口*/
public class RemoveFriendService  extends AbstractService implements Service<String,String>{

	/** 删除好友的构造函数 */
	public RemoveFriendService(TCPServer TCP) {
		super(TCP);
	}

	/**向服务器请求移除好友
	 * @param msg 要移除的好友ID
	 * @return String 移除成功返回Success失败返回Error
	 * */
	public String service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_REMOVE + msg);
			return (String)clientInputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return TCPServer.ERROR;
		}		
	}
}
