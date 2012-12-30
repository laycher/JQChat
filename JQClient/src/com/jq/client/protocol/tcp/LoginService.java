package com.jq.client.protocol.tcp;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jq.util.Friend;

/**
 * 	[用户登录服务类]
 * 	LoginService
 * 	登陆服务器使用类.
 * 	此类通过传进来的InputStream和OutPutStream参数负责
 * 	用户的登陆操作.
 * 	
 * 	执行一系列登陆准备活动.
 *  向服务器发送信息包括:
 * 				用户账号,密码,IP,端口,状态.
 * 	登陆任务[包括获取好友列表,更改在线状态和用户IP,端口信息等]
 *  登陆成功:
 * 				服务器返回好友列表和离线消息
 * 				Object[]{result,leftInfo}.
 * 	失败返回null.
 *  继承自AbstractService
 *  实现于Service<Object[],String>接口
 * */
public class LoginService extends AbstractService implements Service<Object[],String>{
	
	/** 离线消息,String[0]是result，String[1]是消息*/
	private String[] leftInfo = new String[1];
	/** 弹出窗口的父窗口 */
	public static JFrame owner = null;
	
	/** 登录服务的构造函数 */
	public LoginService(TCPServer TCP) {
		super(TCP);
	}
	
	/**
	 * 连接服务器操作.
	 * 说明:
	 * 	    登陆成功后返回好友列表,同时msg数组将改变为用户接受到的离线消息.
	 * @param msg[] 要操作的信息.
	 * */
	public Object[] service(String msg) {
		//接受一个String.格式为[账号_密码_IP_端口_状态]
		Friend[] result = null;
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_LOGIN + msg);		//发送登录资料
			
			String f = (String)clientInputStream.readObject();	
			//接收返回信息.
			if (f.equals(TCPServer.ERROR)){																		//账号密码错误
				JOptionPane.showMessageDialog(owner, "账号或密码错误!", "错误",
						JOptionPane.ERROR_MESSAGE);
				return null;
			}else if (f.equals(TCPServer.LOGINED)) { // 用户已登录.
				JOptionPane.showMessageDialog(owner, "该用户已登录!", "提示",
						JOptionPane.INFORMATION_MESSAGE);
				return null;
			}else{	/*登陆成功,接收到自己和好友信息.*/
				result = getFriend();	//result返回的是好友数组
			}
			getLeftInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			TCP.close();
		}
		return new Object[]{result,leftInfo};
	}

	@SuppressWarnings("unchecked")
	private void getLeftInfo() {
		/* 登陆成功,接收该用户接收到的离线消息 */
		try {
			leftInfo[0] = (String) clientInputStream.readObject();
			if (leftInfo[0].equals(TCPServer.SUCCESS)) {
				ArrayList<String> left = (ArrayList<String>) clientInputStream
					.readObject();
				leftInfo = left.toArray(new String[1]);
			}
			else
				leftInfo = null;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private Friend[] getFriend() {
		ArrayList<Friend> list = null;
		Friend[] friendsInfo = new Friend[1];

		try {
			list = (ArrayList<Friend>) clientInputStream.readObject();
			//friendsInfo = list.toArray(new Friend[1]);
			friendsInfo = list.toArray(friendsInfo);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return friendsInfo;
	}

	
}
