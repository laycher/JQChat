package com.jq.client.protocol.udp;

import javax.swing.JOptionPane;

/**删除好友具体实现类*/
public class RemoveMsg extends AbstractMessage{

	/** 删除好友的构造函数 */
	public RemoveMsg(String msg) {
		super(msg);
	}

	/** 重写update方法 */
	@Override
	public void update(UDPServer UDP) {
		//更新好友列表,将其删除.
		UDP.getMyList().removeItem(UDP.friendsManager.getAllFriends().get(ID));
		UDP.friendsManager.getAllFriends().remove(ID);
		
		JOptionPane.showMessageDialog(null, "[" + ID+"]将您移除好友!",
				"提示", JOptionPane.INFORMATION_MESSAGE);
	}

}
