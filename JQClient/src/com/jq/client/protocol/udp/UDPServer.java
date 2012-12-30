package com.jq.client.protocol.udp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.JOptionPane;

import com.jq.client.gui.chat.ChatFrame;
import com.jq.client.gui.mainframe.FriendsManager;
import com.jq.client.gui.mainframe.MyList;
import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.Friend;
import com.jq.util.Param;

/**
 * [信息处理类]
 * UDPServer
 * 此为UDP协议的服务器端类.
 * 用于处理UDP客户端信息处理核心类.
 * 这个类是每个客户端唯一的.
 * 用于处理全部消息的发送和接收. 继承Thread.
 * 
 * 接收和发送好友发送的信息,包括:
 * 		0-----普通聊天信息.
 *     	1-----好友上线信息.
 *      2-----好友下线信息.
 *      3-----好友添加信息.
 *      4-----好友删除信息.
 *      5-----发送文件信息.
 *      6-----拒收文件信息.
 * 
 * 接收到的信息包括:发送者的ID和信息体.
 * 		其具体格式为 :[类型_ID_信息体]
 * 
 * UDP协议可占用端口为用户设置端口.每个客户端只能占有一个,
 * 
 * */

public class UDPServer extends Thread {

	private int localPort = 0; // 本地占用端口.
	private byte[] sendBuff = null; // 发送信息的字节缓冲.
	private byte[] receiveBuff = null; // 接收信息的字节缓冲.
	private DatagramPacket sendPacket = null; // 发送的数据报包
	private DatagramPacket receivePacket = null; // 接收的数据报包
	private DatagramSocket socket = null; // UDP套接字
	public FriendsManager friendsManager = null;
	public Friend hostUser = null;
	public ChatFrame temp = null;
	private MyList myList = null;

	/** UDPServer的构造函数
	 * @param localPort 本地占用端口
	 * @param friendsManager 好友管理
	 * @param hostUser 好友*/
	public UDPServer(int localPort, FriendsManager friendsManager,
			Friend hostUser) {
		this.friendsManager = friendsManager;
		this.friendsManager.setUDP(this);
		this.hostUser = hostUser;
		initServer(localPort);
		/* 向在线好友发送上线消息 */
		sendLoginMessage(hostUser);
	}

	/** 向好友发送上线通告 
	 * msg的格式：消息Type+ID_IP_Port_Status*/
	private void sendLoginMessage(Friend f) {
		final String msg = MsgFactory.ONLINEMSG + f.getID() + Param.SPACE
				+ f.getIP() + Param.SPACE + f.getPort() + Param.SPACE
				+ f.getStatus();

		runMsg(msg);
	}

	/** 发送下线消息
	 * msg的格式：Type[OUTLINEMSG]+用户ID*/
	public void sendLogoutMessage() {
		final String msg = MsgFactory.OUTLINEMSG + hostUser.getID(); 
		runMsg(msg);
	}

	/**执行消息
	 * @msg 包含的消息*/
	private void runMsg(String msg) {
		Map<String, Friend> map = getAllFriends();
		Friend friend = null;

		for (String id : map.keySet()) {
			friend = map.get(id);

			// 好友在线发送上线通告,不给自己发
			if (friend.getStatus() != 0
					&& !friend.getID().equals(hostUser.getID()))
				sendMessage(msg, friend.getIP(), friend.getPort());
		}
	}

	/**通过FriendsManager获取好友
	 * Map<ID,Friend>*/
	public Map<String, Friend> getAllFriends() {
		return friendsManager.getAllFriends();
	}

	/**
	 * 启动相关服务和准备.
	 * 
	 * @param localPoat
	 *            要设置的本地端口.
	 * */
	@SuppressWarnings("unchecked")
	private void initServer(int localPort) {
		try {
			socket = new DatagramSocket(localPort);
		} catch (SocketException e) {
			/* 当端口被占用时的异常处理 */
			while (true) {
				try {
					/* 启用设置端口的下一个端口 */
					socket = new DatagramSocket(++localPort);
					break;
				} catch (SocketException e1) {
					JOptionPane.showMessageDialog(null, "客户端端口被占用，请查看.", "错误", JOptionPane.ERROR_MESSAGE);
					//端口被占用退出后，要进行注销操作
					Service<String, String> service = (Service<String, String>) ServiceFactory
					.getService(ServiceFactory.TASK_LOGOUT, TCPServer.SERVER_IP, TCPServer.PORT);

					service.service(hostUser.getID());
					System.exit(1);
				}
			}
		}

		this.localPort = localPort;
		this.hostUser.setIP(getLocalAddress());
		this.hostUser.setPort(localPort);
		/* 发送接收消息的buff,限制字数总字节在1024之内. */
		sendBuff = new byte[Param.DATA_SIZE];
		receiveBuff = new byte[Param.DATA_SIZE];

		/* 启动服务线程 */
		this.start();
	}

	@Override
	public void run() {
		receivePacket = new DatagramPacket(receiveBuff, receiveBuff.length);

		while (!socket.isClosed()) {
			try {
				socket.receive(receivePacket);
				String message = new String(receivePacket.getData(), 0,
						receivePacket.getLength());
				/* 处理消息 */
				MsgFactory.getMsg(message).update(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 消息发送函数
	 * 
	 * @param msg
	 *            发送的消息.
	 * @param IP
	 *            接收端的IP地址
	 * @param port
	 *            接收端的监听端口.
	 * */
	public void sendMessage(String msg, String IP, int port) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(IP);
			sendBuff = msg.getBytes();

			sendPacket = new DatagramPacket(sendBuff, sendBuff.length, addr,
					port);
			/* 发送信息 */
			socket.send(sendPacket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收到信息后显示.
	 * 
	 * @param id
	 *            发送信息的好友id
	 * @param msg
	 *            接收到的信息
	 * */
	public void receiveMsg(String id, String msg) {
		friendsManager.addClientFrame(id).setReceiveMsg(msg);
	}

	/**发送文件*/
	public void sendFile(String msg, File file, String IP, int port) {
		try {
			InputStream is = new FileInputStream(file);
			InetAddress addr = InetAddress.getByName(IP);
			/* 发送接收消息的buff,限制字数总字节在1024之内. */
			//sendBuff = new byte[Param.DATA_SIZE];
			sendBuff = msg.getBytes();
			sendPacket = new DatagramPacket(sendBuff,is.read(sendBuff),addr,port);
			socket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加聊天窗口.
	 * 
	 * @param id
	 *            要添加的好友的id
	 * */
	public void addChatFrame(String id) {
		friendsManager.addClientFrame(id);
	}

	/**
	 * 关闭聊天窗口后移除窗口资源.
	 * */
	public void removeChatFrame(String id, ChatFrame frame) {
		friendsManager.removeClientFrame(id, frame);
	}

	/**
	 * 静态方法.获取本机IP地址.
	 * 
	 * @return String 本机IP地址的字符串.
	 * */
	public static String getLocalAddress() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
		return ip;
	}

	/**
	 * 返回当前客户端当前占用的端口.
	 * 
	 * @return int 返回当前客户端占用的端口号.
	 * */
	public int getLocalPort() {
		return localPort;
	}

	/**
	 * 关闭UDP的socket.
	 * */
	public void close() {
		if (socket != null)
			socket.close();
	}

	/** 设置好友列表 */
	public void setMyList(MyList mylist) {
		this.myList = mylist;
	}

	/** 获得好友列表 */
	public MyList getMyList() {
		return myList;
	}

}