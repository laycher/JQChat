package com.jq.client.protocol.tcp;

import com.jq.util.FriendInfo;

/** 查看好友资料具体服务 
 * 继承自AbstractService
 * 实现于Service<FriendInfo,String>接口*/
public class FriendInfoService extends AbstractService implements
		Service<FriendInfo, String> {
	
	/** 查看好友服务的构造函数 */
	public FriendInfoService(TCPServer TCP) {
		super(TCP);
	}

	/**
	 * 查看好友的具体方法
	 * @param msg
	 *            执行操作的信息. ID-->要查看资料的好友ID
	 * @return FriendInfo 返回好友资料类FriendInfo
	 * */
	public FriendInfo service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_GETINFO + msg);
			String f = (String) clientInputStream.readObject();

			if (f.equals(TCPServer.SUCCESS))
				return (FriendInfo) clientInputStream.readObject();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} // 发送登录资料

		return null;
	}

}
