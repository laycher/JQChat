
package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.jq.util.Friend;
import com.jq.util.Param;

/**
 * 		服务抽象基类.
 * */
public abstract class AbstractService implements Service{
	protected String[] msg = null;
	protected ArrayList<Friend>	friendslist = new ArrayList<Friend>();
	protected Socket socket = null;
	protected ObjectOutputStream clientOutputStream = null;
	protected ObjectInputStream clientInputStream = null;
	
	public AbstractService(String msg) {
		this.msg = msg.split(Param.SPACE);//以空格为分隔符进行分割成数组
	}
	
	/**关闭连接*/
	protected void close() {
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
