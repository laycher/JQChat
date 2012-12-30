package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.sql.DBSource;
import com.jq.util.Param;

/** 离线消息具体服务 */
public class LeftInfoService implements Service {

	protected Socket socket = null;
	protected ObjectOutputStream clientOutputStream = null;
	protected ObjectInputStream clientInputStream = null;

	/**用户的ID*/
	private String ID;
	/**用户发送的消息*/
	private String msg;

	private static final String SQL_LEFTINFO = "INSERT INTO LEFTINFO VALUES('?','?');";

	/** 离线消息类 
	 * @param msg 消息流*/
	public LeftInfoService(String msg) {
		//int index = msg.indexOf(Param.SPACE);
		//ID = msg.substring(0, index).trim();
		//this.msg = msg.substring(index + 1).trim();
		String[] temp = msg.split(Param.SPACE);
		ID = temp[0].trim();
		this.msg = temp[1].trim();
	}

	/** 离线消息的service方法 */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		Statement stat = null;

		try {
			stat = db.getStatement();
			db.setAutoCommit(false);
			//把离线消息写入数据库
			update(stat);
			db.commit();
		} catch (Exception e) {
			e.printStackTrace();
			//如果出问题，数据库回滚
			db.rollback();
		} finally {
			// 释放数据库连接资源--->必须释放!
			db.releaseConnection();
			// 关闭socket资源
			close();
		}
	}

	/** 更新数据库，把离线消息写入数据库 */
	private void update(Statement stat) throws SQLException {
		String sql = SQL_LEFTINFO.replaceFirst("\\?", ID);
		sql = sql.replaceFirst("\\?", msg);
		stat.executeUpdate(sql);
	}

	/** 关闭连接 */
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
