package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jq.server.sql.DBSource;
import com.jq.util.FriendInfo;

/** 获取好友详细资料服务 */
public class FriendInfoService extends AbstractService {

	private static final String SQL_GETINFO = "SELECT Sex,Provence, City, Email, Homepage, Age, Academy,Department From UserInfo Where UserID = '?'";

	public FriendInfoService(String msg) {
		super(msg);
	}

	/** 获取好友信息服务*/
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		Statement stat = null;

		try {
			stat = db.getStatement();
			getFriendInfo(stat);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放数据库连接资源
			db.releaseConnection();
			// 关闭socket资源
			close();
		}
	}

	/** 获取好友信息方法 */
	private void getFriendInfo(Statement stat) {
		String sql = SQL_GETINFO.replaceFirst("\\?", msg[0]);
		try {
			ResultSet rs = stat.executeQuery(sql);

			while(rs.next()){
				FriendInfo f = new FriendInfo(rs.getString(1),
					rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getString(5),
					rs.getString(6), rs.getString(7),
					rs.getString(8));
			
				clientOutputStream.writeObject(ServiceFactory.SUCCESS);
				clientOutputStream.writeObject(f);
			}
		} catch (Exception e) {
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
				e.printStackTrace();
			}
			e.printStackTrace();
		}
	}

}
