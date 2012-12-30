package com.jq.client.protocol.tcp;

import com.jq.util.Friend;

/** 添加好友具体服务 
 * 继承自AbstractService，实现于Service<Friend,String>接口*/
public class AddtionService extends AbstractService implements Service<Friend, String> {

	/** 添加好友服务的构造函数 */
	public AddtionService(TCPServer TCP) {
		super(TCP); //调用父类的构造函数AbstractService(TCPService TCP)
	}

	/**
	 * 添加好友的具体执行方法
	 * @param msg
	 *            执行操作的信息. 添加方 ID_ID 接收方索取添加好友信息 ID
	 * @return Friend 当添加用户调用此函数时返回null,被添加用户调用则返回Friend
	 * */
	public Friend service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_ADDTION + msg);
			String f = (String) clientInputStream.readObject();

			if (f.equals(TCPServer.SUCCESS))
				return (Friend) clientInputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} // 发送登录资料

		return null;
	}
}
