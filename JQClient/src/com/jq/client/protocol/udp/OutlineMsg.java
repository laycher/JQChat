package com.jq.client.protocol.udp;

import java.util.Map;

import com.jq.util.Friend;

/**
 * 	好友下线消息处理类.
 * */
public class OutlineMsg extends AbstractMessage{

	/** 好友下线的构造函数 */
	public OutlineMsg(String msg) {
		super(msg);
	}

	/** 重写update 方法*/
	@Override
	public void update(UDPServer UDP) {
		updataList(ID,UDP);
		
	}

	/**更新好友列表*/
	private void updataList(String ID,UDPServer UDP){
		Map<String,Friend> map = UDP.getAllFriends();
		
		Friend friend = map.get(ID);
		UDP.getMyList().removeItem(friend);
		
		if (null != friend )
			friend.setStutas(0);
	
		UDP.getMyList().addItem(friend);
	}
}
