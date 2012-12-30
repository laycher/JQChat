package com.jq.server.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.sql.DBSource;

/**
 * 用户注销登录具体服务.
 * */
public class LogoutService extends AbstractService implements Service {

	private static final String SQL_LOGOUT = "UPDATE UserIPInfo SET Status = 0 WHERE UserID = '?';";

	/** 注销登录的构造函数*/
	public LogoutService(String msg) {
		super(msg);
		System.out.println("关闭");
	}

	/** 注销登录的service 方法 */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		Statement stat = null;
		try {
			stat = db.getStatement();
			String SQL = SQL_LOGOUT.replaceFirst("\\?", msg[0]);

			try {
				db.setAutoCommit(false);
				stat.executeUpdate(SQL);
				db.commit();
			} catch (SQLException e) {
				db.rollback();
				e.printStackTrace();
			} finally {
				// 释放数据库连接资源
				db.releaseConnection();
				// 关闭socket资源
				close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
