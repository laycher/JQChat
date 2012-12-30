package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jq.server.sql.DBSource;
import com.jq.util.Friend;
import com.jq.util.Param;

/** 提供好友搜索功能 */
public class SearchService extends AbstractService {

	private static final String SQL = "SELECT UserInfo.UserID, UserInfo.NickName,UserInfo.Remark, IP, Port, "
			+ "UserInfo.Face, Status, UserInfo.Description FROM UserIPInfo, UserInfo "
			+ "WHERE UserIPInfo.UserID = UserInfo.UserID AND ";

	/** 搜素用户的构造函数 */
	public SearchService(String msg) {
		super(msg);
		System.out.println("服务器接收查询（SearchService）:" + msg);
	}

	/** 搜索用户的具体运行函数 */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		try {
			Statement stat = db.getStatement();
			String searchString = msg[0];
			if (searchString.contains("="))
				conditionSearch(stat, searchString);
			else
				idSearch(stat, searchString);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
			}
		} finally {
			// stat.close();
			// 释放数据库连接资源
			db.releaseConnection();
			// 关闭socket资源
			close();
		}
	}

	private void idSearch(Statement stat, String id) {
		String sql = SQL + "UserInfo.UserID = '" + id + "';";
		searching(stat, sql);
	}

	private void conditionSearch(Statement stat, String condition) {
		String sql = SQL + condition.replaceAll("_", Param.SPACE); //空格替换下划线 
		searching(stat, sql);
	}

	private void searching(Statement stat, String sql) {
		try {
			ResultSet rs = stat.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				friendslist.add(new Friend(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), 
						rs.getInt(6), rs.getInt(7), rs.getString(8)));
			}

			if (friendslist.size() != 0) {
				clientOutputStream.writeObject(ServiceFactory.SUCCESS);
				clientOutputStream.writeObject(friendslist);
			} else {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
