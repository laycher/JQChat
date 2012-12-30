package com.jq.client.protocol.tcp;

import java.util.ArrayList;

import com.jq.util.Friend;

/**搜索好友具体实现
 * 继承自AbstractService
 * 实现于Service<Friend[],String>接口*/
public class SearchService extends AbstractService implements Service<Friend[], String>{

	/**搜索好友的构造方法*/
	public SearchService(TCPServer TCP) {
		super(TCP);
	}

	/** 搜索好友的具体方法
	 * @param msg 查询语句SQL */
	@SuppressWarnings("unchecked")
	public Friend[] service(String msg) {
		String flag;
		ArrayList<Friend> list = null;
		
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_SEARCH + msg);
			
			flag = (String)clientInputStream.readObject();
		
			if (flag.equals(TCPServer.SUCCESS)){
				list = (ArrayList<Friend>) clientInputStream.readObject();
				return list.toArray(new Friend[1]);
			}
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
