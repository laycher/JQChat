package com.jq.client.protocol.udp;

import java.util.Map;

import com.jq.client.gui.common.NotifyFrame;
import com.jq.util.Friend;
import com.jq.util.Param;
import com.jq.util.SoundPlayer;

/**
 * 	好友上线消息处理类.
 * */
public class OnlineMsg extends AbstractMessage{
	
	public OnlineMsg(String msg) {
		super(msg);
	}

	/**重写update方法
	 * msg的格式：IP_Port_Status*/
	@Override
	public void update(UDPServer UDP) { 
		String[] msgs = msgBody.split(Param.SPACE);
		Friend friend = updataList(ID,msgs,UDP);
		
		UDP.getMyList().add(1,friend);
		new NotifyFrame(friend);
		SoundPlayer.play(SoundPlayer.ONLINE);
	}
	
	/**更新好友列表*/
	private Friend updataList(String ID,String[] info,UDPServer UDP){
		Map<String,Friend> map = UDP.getAllFriends();
		Friend friend = map.get(ID);
		
		if (null != friend ){
			friend.setIP(info[0]);
			friend.setPort(Integer.parseInt(info[1]));
			friend.setStutas(Integer.parseInt(info[2]));
		}
		
		/** 删除离线的那个好友，增加一个新的在线状态的好友*/
		UDP.getMyList().removeItem(friend);
		return friend;
	}
}
