package com.jq.client.gui.mainframe;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

import com.jq.client.gui.chat.ChatFrame;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.Friend;

/**
 * 用于对好友列表进行统一管理.
 * */
public class FriendsManager {

	private DefaultListModel list = new DefaultListModel();
	private UDPServer UDP = null;
	/* 维护一个被关闭了的好友聊天窗口的map. */
	private Map<String, ChatFrame> closedChatFrame = new HashMap<String, ChatFrame>();
	/* 维护一个当前已打开好友聊天窗口的map. */
	private Map<String, ChatFrame> activeChatFrame = new HashMap<String, ChatFrame>();
	/* 维护一个所有好友的map */
	private Map<String, Friend> friendsInfo = new HashMap<String, Friend>();

	/** FriendManager 构造函数 */
	public FriendsManager(Friend[] friends) {
		/* 将全部好友填充到map里 */
		for (Friend friend : friends) {
			friendsInfo.put(friend.getID(), friend); // <ID,friend(信息)>全部好友
			list.addElement(friend); // 显示列表
		}
	}

	/** 设置UDP协议.*/
	public void setUDP(UDPServer UDP) {
		this.UDP = UDP;
	}

	/** 打开新对话窗口,添加到activeChatFrame中,并显示窗口. */
	public ChatFrame addClientFrame(String id) {
		Friend f = friendsInfo.get(id);

		if (f == null)
			return null;
		ChatFrame frame = activeChatFrame.get(id);
		// 如果聊天窗口为不显示状态
		if (frame == null) {
			//如果没有已打开的与该好友聊天的窗口
			// 则从已关闭map中搜索是否包含此窗口,有则显示,无则新建一个
			frame = closedChatFrame.get(f.getID());

			if (frame == null) {
				frame = new ChatFrame(UDP, f);
				activeChatFrame.put(f.getID(), frame);
			} else {
				frame.normalStatus();
				frame.setVisible(true);
			}
		}

		return frame;
	}

	/**
	 * 当关闭对话框时候,从activeChatFrame中移除frame
	 * 加入到closedChatFrame的Map中
	 * @param id
	 *            要移除的好友ID.
	 * @param frame
	 *            移除的窗口
	 * */
	public void removeClientFrame(String id, ChatFrame frame) {
		if (activeChatFrame.containsKey(id)) {
			activeChatFrame.remove(id);

			if (!closedChatFrame.containsKey(id))
				closedChatFrame.put(id, frame);
		}
	}

	/** 获取显示列表DefaultListModel */
	public DefaultListModel getDefaultListModel() {
		return list;
	}

	/** 获取全部好友map */
	public Map<String, Friend> getAllFriends() {
		return friendsInfo;
	}

	/** 获取当前活动好友map */
	public Map<String, ChatFrame> getActiveFriends() {
		return activeChatFrame;
	}

	/** 获取已关闭窗口好友map */
	public Map<String, ChatFrame> getClosedFriends() {
		return closedChatFrame;
	}

}
