package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import com.jq.server.gui.Interface;
import com.jq.server.service.ServiceFactory;
import com.jq.server.sql.DBSource;
import com.jq.server.sql.SQLPoolServer;
import com.jq.util.GC;
import com.jq.util.PropertyFile;

/**
 * [服务器核心类.] 用于处理各种客户端要求执行的操作.
 * 
 * 服务器处理类,使用数据库连接池和线程池增加其吞吐量和服务效率. 线程池采用 java.util.concurrent.ExecutorService.
 * java.util.concurrent.Executors. 数据库操作全部使用事务处理.可以避免突然事故导致的不一致性.
 * 
 * 服务器处理用户请求模式: 用户发送任务标实,服务器接收启动新线程并确认标实后执行相关操作.
 * 线程执行用户要求的操作,如果执行操作完成,先发送成功标实Success 之后在发送给用户要求的结果,若执行失败或无执行结果,返回Error标实.
 * 不要发送null之类的东西. [关于数据加密]数据的通信未进行封装加密.由于数据间的交流通行使用
 * ObjectInputStream和ObjectOuputStream,可更容易的进行该井,只要 将数据封装成自定义类, 在自定义类中对数据加密解密即可.
 * 
 * [注:]此版本的服务器比较简陋.下一版本中将添加服务器GUI界面,并添加对数据库 执行相关操作的功能.
 * 服务器和客户端并不一直保持连接,当客户端要求使用服务器数据库时才进行 连接,效率不是很高.
 * 
 * */
public class SQLServerProcess extends Thread {

	/** 数据库配置文件 */
	private PropertyFile prop = null;
	/** 数据库连接池 */
	static SQLPoolServer sqlPool = null; 
	/** 线程池 */
	private ExecutorService threadPool = null;
	/** 服务器服务 */
	private ServerSocket serverSocket = null; 
	/** 端口号*/
	public static final int PORT = 6000;	

	public SQLServerProcess(PropertyFile propertyFile) {
		prop = propertyFile;
		sqlPool = new SQLPoolServer(prop);
		/* 获取动态线程池. */
		threadPool = Executors.newCachedThreadPool();
		setUserOutline();

		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {	//端口若是占用
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, PORT + "端口被占用,请停止此端口的服务再重新启动",
					"错误", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		/* 启动垃圾处理，守护进程 */
		new GC();
		
		/*启动界面*/
		new Interface(sqlPool, threadPool, serverSocket);
		start();
	}

	/** 静态方法.获取本机IP地址. */
	public static String getLocalAddress() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
		return ip;
	}

	/** 服务器启动时. 将所有用户设置为不在线状态. */
	private void setUserOutline() {
		/* 获取数据库连接资源 */
		DBSource db = sqlPool.getSQLServer();
		String SQL = "UPDATE USERIPINFO SET STATUS = 0;";
		try {
			Statement stat = db.getStatement();

			db.setAutoCommit(false);
			stat.executeUpdate(SQL);
			db.commit();
		} catch (SQLException e) {
			db.rollback();
			e.printStackTrace();
		} finally {
			/* 释放资源 */
			db.releaseConnection();
		}
	}

	@Override
	public void run() {
		while (!serverSocket.isClosed()) {
			try { /* 监听服务器端口 */
				threadPool.execute(new ProcessThread(serverSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * [内部类.] 任务处理类. 用于处理用户对服务器的请求. 用户的任务请求包括:
	 * [1].注册新用户.
	 * [2].登陆任务[包括获取好友列表,更改在线状态和用户IP,端口信息等] 
	 * [3].注销. 
	 * [4].更改用户资料.
	 * [5].更新好友资料. 
	 * [6].搜索好友. 
	 * [7].添加好友. 
	 * [8].删除好友. 
	 * [9].发送离线消息. 
	 * 用户和好友的相关信息使用Friend类传送. 查询好友详细资料使用FriendInfo类传送.
	 * */
	public class ProcessThread implements Runnable {

		private Socket socket = null;
		/**用户输入流*/
		private ObjectInputStream clientInputStream = null;
		/**用户输出流*/
		private ObjectOutputStream clientOutputStream = null;
		
		public ProcessThread(Socket socket) {
			System.out.println("服务器:接受任务!");
			this.socket = socket;
			/* 获取数据库连接 */
			try {
				clientOutputStream = new ObjectOutputStream(
						socket.getOutputStream());
				clientInputStream = new ObjectInputStream(
						socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("服务器:初始化完毕!");
		}

		public void run() {
			try {
				/* 获取要求执行任务的用户ID等信息. */
				String msg = (String) clientInputStream.readObject();
				DBSource db = sqlPool.getSQLServer();
				ServiceFactory.getService(msg).service(db,clientInputStream, clientOutputStream);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
