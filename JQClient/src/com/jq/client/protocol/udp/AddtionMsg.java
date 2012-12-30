package com.jq.client.protocol.udp;

import javax.swing.JOptionPane;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.Friend;
import com.jq.util.SoundPlayer;

/**
 * 	好友添加处理类.
 * */
public class AddtionMsg extends AbstractMessage{

	/** 好友添加构造函数 */
	public AddtionMsg(String msg) {
		super(msg);
	}

	/** 重写update方法 */
	@Override
	public void update(UDPServer UDP) {
		Friend friend = connecting(ID);
		
		if (friend != null){
			UDP.friendsManager.getAllFriends().put(friend.getID(), friend);
			
			//更新好友列表添加新好友资料.
			//如果要添加的好友在线，则添加到自己下面，下线的好友上面
			if (friend.getStatus() != 0 && friend.getStatus() != 1)
				UDP.getMyList().add(1,friend);
			else	//否则加到最后
				UDP.getMyList().addItem(friend);
			
    		JOptionPane.showMessageDialog(null, "[" + ID+"]将您添加为好友!",
					"提示", JOptionPane.INFORMATION_MESSAGE);
    		
    		SoundPlayer.play(SoundPlayer.ADDTION);
		}
	}

	/** 连接服务器更新数据库并获取好友Friend */
	@SuppressWarnings("unchecked")
	private Friend connecting(String ID) {
		Service<Friend, String> service = (Service<Friend, String>) ServiceFactory
				.getService(ServiceFactory.TASK_ADDTION, TCPServer.SERVER_IP,
						TCPServer.PORT);
		// 向服务器请求添加该好友
		return service.service(ID);
	}
}
