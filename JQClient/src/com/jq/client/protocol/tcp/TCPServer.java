package com.jq.client.protocol.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * [TCP服务器连接类]
 * TCPServer 
 * 执行客户端与服务器端的TCP信息通信. 
 * 此类执行多项操作:
 * 			[1].注册新用户.
 * 			[2].登陆任务[包括获取好友列表,更改在线状态和用户IP,端口信息等]
 * 			[3].注销任务.
 * 			[4].更改用户资料任务.
 * 			[5].更新好友资料任务.
 * 			[6].搜索好友任务.
 * 			[7].添加好友任务.
 * 			[8].删除好友任务.
 * 			[9].发送离线消息任务.
 * 
 * 与服务器连接成功返回与之连接的输入输出流.
 * 失败则弹出"连接服务器出错,请稍候再试...!"提示框并关闭应用程序.
 * */
public class TCPServer {
	private Socket socket = null;
	private OutputStream clientOutputStream = null;
	private InputStream clientInputStream = null;
	
	public static JFrame owner = null;
	public static String SERVER_IP = null;
	public static int PORT = 0;

	/** 成功标示符 */
	public static final String SUCCESS = "SUCCESS";
	/** 失败标示符 */
	public static final String ERROR = "ERROR";
	/** 用户已登录标示符 */
	public static final String LOGINED = "9";

	/**
	 * 构造函数.
	 * 
	 * @param IP
	 *            服务器IP地址.
	 * @param port
	 *            服务器监听端口.
	 * */
	public TCPServer(String IP, int port) {
		if (null == IP) {
			JOptionPane.showMessageDialog(owner, "连接服务器出错,请在设置里设置服务器地址及端口!",
					"错误", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		try {
			socket = new Socket(IP, port);

			clientOutputStream = socket.getOutputStream();
			clientInputStream = socket.getInputStream();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(owner, "连接服务器出错,请稍候再试...!", "错误",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * 返回InputStream用于从服务器读取流.
	 * 
	 * @param InputStream
	 *            连接服务器Socket返回的InputStream.
	 * 
	 * */
	public InputStream getInputStream() {
		System.out.println("TCP:连接服务器Socket返回的InputStream");
		return clientInputStream;
	}

	/**
	 * 返回InputStream用于向服务器写入流.
	 * 
	 * @param OutputStream
	 *            连接服务器Socket返回的OutputStream.
	 * */
	public OutputStream getOutputStream() {
		System.out.println("TCP:连接服务器Socket返回的OutputStream");
		return clientOutputStream;
	}

	/**
	 * 退出时对资源的清除.
	 * */
	public void close() {
		try {
			if (socket != null)
				socket.close();
			if (clientOutputStream != null)
				clientOutputStream.close();
			if (clientInputStream != null)
				clientInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}