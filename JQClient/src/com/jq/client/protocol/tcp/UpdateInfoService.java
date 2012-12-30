package com.jq.client.protocol.tcp;

import com.jq.util.FriendInfo;

/**更新用户资料具体服务
 * 继承自AbstractService
 * 实现于Service<String,String>接口*/
public class UpdateInfoService  extends AbstractService implements Service<String,FriendInfo>{

	/**更新用户资料具体服务构造函数*/
	public UpdateInfoService(TCPServer TCP) {
		super(TCP);
	}
	/**
	 * 更新用户资料的具体方法
	 * @param myInfo
	 * 		Nickname  face  sex  age Academy department Provence City Email Homepage Description UserID
	 * @return 标志，成功或者失败 
	 * */
	public String service(FriendInfo myInfo) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_UPDATAINFO + "无标识");
			
			clientOutputStream.writeObject(myInfo);
			return (String)clientInputStream.readObject();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

}
