package com.jq.client.gui.mainframe;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;

import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.MsgFactory;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.Friend;
import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * 自定义List控件. 
 * 实现功能: 可显示好友头像,昵称和好有个性签名. 
 * 	1.添加项. 
 * 	2.删除项. 
 * 	3.查找项(好友上线活下线后更新列表使用).
 * 	4.插入项.
 * */
public class MyList extends JList {

	private static final long serialVersionUID = -1716222153753784579L;

	private DefaultListModel friends = null;
	private UDPServer UDP = null;
	/** 显示好友的方式 */
	private boolean showA = true;
	/** 维护一个显示个人信息的Map */
	private Map<String, InfoFrame> infoMaps = new HashMap<String, InfoFrame>();

	/**
	 * 构造函数.
	 * 
	 * @param friends
	 *            好友列表管理器.
	 * @param UDP
	 *            UDPServer.
	 * */
	public MyList(DefaultListModel friends, final UDPServer UDP) {
		super(friends);

		this.friends = friends;
		this.UDP = UDP;
		UDP.setMyList(this);

		// 设置弹出菜单.
		setComponentPopupMenu(new MyPopupMenu());

		setOpaque(true); /* 必须的 */
		setCellRenderer(new ListRender1()); // 设置渲染项

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					Friend f = (Friend) getSelectedValue();
					showChatFrame(f);
				}
			}
		});
	}

	/**
	 * 显示与好友Friend的聊天窗口
	 * 
	 * @param friend
	 *            好友
	 */
	private void showChatFrame(Friend friend) {
		if (!friend.getID().equals(UDP.hostUser.getID())){
			UDP.addChatFrame(friend.getID());
		}else {
			JOptionPane.showMessageDialog(this, "您不能自己和自己聊天.","提示", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * 在此列表的指定位置处插入指定元素。
	 * 如果索引超出范围（index < 0 || index > size()）， 则抛出
	 * ArrayIndexOutOfBoundsException
	 *
	 * 添加好友时用到，0位置是自己，1以后才是好友。最后面的是不在线的。
	 * @param index
	 *            指定的位置.
	 * @param item
	 *            要插入的元素.
	 * */
	public void add(int index, Friend item) {
		friends.add(index, item);
	}

	/**
	 * 将指定元素添加到此类表的末尾.
	 * 
	 * @param item
	 *            要插入的元素.
	 * */
	public void addItem(Friend item) {
		friends.addElement(item);
	}

	/**
	 * 从此列表中移除参数的第一个（索引最小的）匹配项.
	 * 
	 * @param item
	 *            要移除的元素.
	 * */
	public void removeItem(Friend item) {
		friends.removeElement(item);
	}

	//-------------重写DefaultListModel的方法----------------
	/**
	 * 移除此列表中指定位置处的元素。
	 * 返回从列表中移除的元素。 
	 * 如果索引超出范围（index < 0 || index >= size()）， 则抛出
	 * ArrayIndexOutOfBoundsException。
	 * 
	 * @param index
	 *            指定要移除元素的位置.
	 *
	public void removeItemAt(int index) {
		friends.remove(index);
	} */

	/**
	 * 从此列表中移除所有元素。
	 * 此调用返回后，列表将是空的（除非该调用抛出异常）。 
	 * 
	public void clear() {
		friends.clear();
	}*/

	/**
	 * 测试指定对象是否为此类表中的组件.
	 * 
	 * @return boolean 如果指定对象是此列表中的组件，则返回 true
	 * 
	public boolean contains(Friend item) {
		return friends.contains(item);
	}*/

	/**
	 * 返回列表中指定位置处的元素。 
	 * 如果索引超出范围（index < 0 || index >= size()）， 则抛出
	 * ArrayIndexOutOfBoundsException。
	 * 
	 * @param index
	 *            要返回的元素的索引
	 * 
	public Friend get(int index) {
		return (Friend) friends.get(index);
	}*/

	/**
	 * 搜索 item 的第一次出现 
	 * 此列表中该参数第一次出现时所在位置上的索引；
	 * 	    如果没有找到该对象，则返回 -1
	 * 
	 * @param item
	 *            一个对象
	 * 
	public int indexOf(Friend item) {
		return friends.indexOf(item);
	}*/

	/**
	 * 测试此列表中是否有组件
	 * 
	 * @return boolean 当且仅当此列表中没有组件（也就是说其大小为零）时返回 true；否则返回 false
	 * 
	public boolean isEmpty() {
		return friends.isEmpty();
	}*/

	/**
	 * 返回此列表中的组件数.
	 * 好友数量
	 * @return int 包含元素个数.
	 * 
	public int Size() {
		return friends.size();
	}*/

	/**
	 * 以正确顺序返回包含此列表中所有元素的数组
	 * 
	 * @return Object[]
	 * 
	public Object[] toArray() {
		return friends.toArray();
	}*/
	

	/** 显示friend个人资料 */
	public void showInfoFrame(Friend friend) {
		InfoFrame infoFrame = infoMaps.get(friend.getID());

		if (infoFrame == null) {
			if (friend.getID().equals(UDP.hostUser.getID()))	//如果查看的是自己
				infoFrame = new InfoFrame(null, friend, true);
			else
				infoFrame = new InfoFrame(null, friend, false);	//查看的是好友
			infoMaps.put(friend.getID(), infoFrame);
		}

		infoFrame.setLocation(MyResources.getScreenCenterX() - 200,
				MyResources.getScreenCenterY() - 200);
		infoFrame.setVisible(true);
	}

	/**
	 * [内部类] list的弹出菜单. 
	 * 功能: 
	 * 		1.与好友聊天
	 * 		2.修改显示 
	 * 		3.查看好友资料 
	 * 		4.删除好友
	 * */
	class MyPopupMenu extends JPopupMenu {

		private static final long serialVersionUID = 7059674442129911004L;

		private JMenuItem chatMenuItem, remarkMenuItem, getInfoMenuItem,
				deleteMenuItem;

		public MyPopupMenu() {
			chatMenuItem = new JMenuItem("与好友聊天");
			remarkMenuItem = new JMenuItem("修改显示");
			getInfoMenuItem = new JMenuItem("查看资料");
			deleteMenuItem = new JMenuItem("删除好友");

			chatMenuItem.setIcon(MyResources.getImageIcon(MyResources.ICON
					+ "chat.gif"));
			chatMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Friend friend = (Friend) getSelectedValue();
					if (friend != null)
						showChatFrame(friend);
				}
			});
			add(chatMenuItem);

			remarkMenuItem.setIcon(MyResources.getImageIcon(MyResources.ICON
					+ "tray.gif"));
			remarkMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (showA) {
						setCellRenderer(new ListRender2()); // 设置渲染项
						showA = false;
					} else {
						setCellRenderer(new ListRender1()); // 设置渲染项
						showA = true;
					}
				}
			});
			add(remarkMenuItem);

			getInfoMenuItem.setIcon(MyResources.getImageIcon(MyResources.ICON
					+ "getInfo.gif"));
			getInfoMenuItem.setText("查看资料");
			getInfoMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Friend friend = (Friend) getSelectedValue();
					if (friend != null)
						showInfoFrame(friend);
				}
			});
			add(getInfoMenuItem);

			add(new JSeparator());

			deleteMenuItem.setIcon(MyResources.getImageIcon(MyResources.ICON
					+ "close.gif"));
			deleteMenuItem.setText("删除好友");
			deleteMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Friend friend = (Friend) getSelectedValue();
					if (friend.getID().equals(UDP.hostUser.getID())){
						JOptionPane.showConfirmDialog(MyList.this, "不能删除自己！",
								"警告", JOptionPane.OK_OPTION,
								JOptionPane.WARNING_MESSAGE);
						return;
					}

					int i = JOptionPane.showConfirmDialog(MyList.this,
							"确认删除好友:" + friend.getNickName() + "["
									+ friend.getID() + "]吗?", "提示",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (i == JOptionPane.NO_OPTION){
							return;
					}else if(i == JOptionPane.YES_OPTION){
						removeFriend(friend);
					}
				}
			});
			add(deleteMenuItem);
		}

		/** 删除好友服务 */
		@SuppressWarnings("unchecked")
		private void removeFriend(Friend friend) {
			Service<String, String> service = (Service<String, String>) ServiceFactory
					.getService(ServiceFactory.TASK_REMOVE,
							TCPServer.SERVER_IP, TCPServer.PORT);

			String flag = service.service(UDP.hostUser.getID() + Param.SPACE
					+ friend.getID());
			if (flag.equals(TCPServer.SUCCESS)) {
				// 更新好友列表添加新好友资料,将其删除.
				UDP.friendsManager.getAllFriends().remove(friend.getID());
				UDP.getMyList().removeItem(friend);

				// 若好友在线则向好友通告删除信息
				if (friend.getStatus() != 0){
					UDP.sendMessage(
							MsgFactory.REMOVEMSG + UDP.hostUser.getID(),
							friend.getIP(), friend.getPort());
				}
				JOptionPane.showMessageDialog(this, "删除好友:" + "["
						+ friend.getID() + "]成功!", "提示",
						JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "删除好友:" + "["
						+ friend.getID() + "]失败!", "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * [内部类] List列表的列表项渲染类别A.
	 * */
	class ListRender1 extends JPanel implements ListCellRenderer {
		private static final long serialVersionUID = -7707554732894912210L;

		private JLabel name,face,signedString;
		
		public ListRender1() {
			setOpaque(true); /* 必须的 */

			initComponents();
		}

		/**
		 * 初始化界面.
		 * */
		private void initComponents() {
			signedString = new JLabel();
			name = new JLabel();
			face = new JLabel();

			setLayout(new AbsoluteLayout());

			add(signedString,new AbsoluteConstraints(50, 20, 140, -1));
			add(name, new AbsoluteConstraints(50, 0, 40, -1));
			add(face, new AbsoluteConstraints(2, 1, 44, 44));
		}

		/**
		 * 重写接口函数,实现自定义list项.
		 * */
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Friend selectedFriend = (Friend) value;

			/* 设置好友头像 */
			face.setIcon(selectedFriend.getFaceIcon());

			/* 好友状态 */
			if ((selectedFriend.getStatus() == 0 || selectedFriend.getStatus() == 1))
				name.setIcon(null);	//离线则不现实状态
			else
				name.setIcon(selectedFriend.getStatusIcon());
			name.setText(selectedFriend.getNickName());

			//setToolTipText(selectedFriend.getSignedString());
			signedString.setText(selectedFriend.getSignedString());

			setToolTipText("<html><font color=red>　　　好友信息</font><br>昵称："
					+ selectedFriend.getNickName()
					+ "["
					+ selectedFriend.getID()
					+ "]<br>状态："
					+ (selectedFriend.getStatus() == 0
							|| selectedFriend.getStatus() == 1 ? "离线" : "在线")
					+ "<br>用户IP：" + selectedFriend.getIP() + "<br>个性签名："
					+ selectedFriend.getSignedString() + "<br><br></html>");

			/* 选中后的重绘 */
			if (cellHasFocus) {
				this.setBackground(new Color(232, 236, 251));
				name.setForeground(Color.RED);
				signedString.setForeground(Color.RED);
				setBorder(new javax.swing.border.LineBorder(new Color(
						200, 218, 254), 2, true));
			} else {
				this.setBackground(new Color(241, 247, 254));

				if (selectedFriend.getStatus() != 0
						&& selectedFriend.getStatus() != 1) { // 好友上线
					name.setForeground(new Color(0, 51, 102));
					signedString.setForeground(new Color(0, 51, 102));
				} else {
					name.setForeground(new Color(150, 150, 150)); // 未上线
					signedString.setForeground(new Color(150, 150, 150));
				}
			}
			return this;
		}

	}

	/**
	 * [内部类] List列表的列表项渲染类别B.
	 * */
	class ListRender2 extends JPanel implements ListCellRenderer {

		private static final long serialVersionUID = 3313503743992044426L;

		private JLabel name, signedString;

		public ListRender2() {
			//setOpaque(true);
			initComponents();
		}

		private void initComponents() {

			signedString = new JLabel();
			name = new JLabel();

			setLayout(new AbsoluteLayout());
			add(signedString,new AbsoluteConstraints(110, 0, 110, 25));
			add(name, new AbsoluteConstraints(10, 0, 120, 25));
		}

		/**
		 * 重写接口函数,实现自定义list项.
		 * */
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Friend selectedFriend = (Friend) value;

			/* 设置好友头像 */
			name.setIcon(MyResources.getImageIcon(MyResources.ICON + "tray.gif"));
			/* 好友状态 */
			if ((selectedFriend.getStatus() == 0 || selectedFriend.getStatus() == 1))
				name.setText(selectedFriend.getNickName() + "[离线]");
			else
				name.setText(selectedFriend.getNickName() + "[在线]");

			//setToolTipText(selectedFriend.getSignedString());
			signedString.setText(selectedFriend.getSignedString());

			setToolTipText("<html><font color=red>　　　好友信息</font><br>昵称："
					+ selectedFriend.getNickName()
					+ "["
					+ selectedFriend.getID()
					+ "]<br>状态："
					+ (selectedFriend.getStatus() == 0
							|| selectedFriend.getStatus() == 1 ? "离线" : "在线")
					+ "<br>用户IP：" + selectedFriend.getIP() + "<br>个性签名："
					+ selectedFriend.getSignedString() + "<br><br></html>");

			/* 选中后的重绘 */
			if (cellHasFocus) {
				this.setBackground(new Color(232, 236, 251));
				name.setForeground(Color.RED);
				signedString.setForeground(Color.RED);
			} else {
				this.setBackground(new Color(241, 247, 254));

				if (selectedFriend.getStatus() != 0
						&& selectedFriend.getStatus() != 1) { // 好友上线
					name.setForeground(new Color(0, 51, 102));
					signedString.setForeground(new Color(0, 51, 102));
				} else {
					name.setForeground(new Color(150, 150, 150)); // 未上线
					signedString.setForeground(new Color(150, 150, 150));	//灰色
				}
			}
			return this;
		}
	}
}