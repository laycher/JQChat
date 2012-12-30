package com.jq.client.protocol.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 服务抽象类 */
public abstract class AbstractService {
	protected ObjectInputStream clientInputStream = null;
	protected ObjectOutputStream clientOutputStream = null;
	protected TCPServer TCP = null;

	/** 根据TCPServer类构造服务抽象类
	 * 获取InputStream和OutputStream
	 * @param TCP 获取连接的socket */
	public AbstractService(TCPServer TCP) {
		this.TCP = TCP;
		try {
			clientInputStream = new ObjectInputStream(TCP.getInputStream());
			clientOutputStream = new ObjectOutputStream(TCP.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ObjectInputStream getObjectInputStream() {
		return clientInputStream;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return clientOutputStream;
	}

}
