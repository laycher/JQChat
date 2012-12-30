package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.sql.DBSource;

/** 重新获取密码,更改密码具体服务 */
public class PasswordService extends AbstractService {

	private static final String SQL_CHECK = "SELECT REGISTEREMAIL FROM USERINFO WHERE USERID = '?';";

	private static final String SQL_UPDATE = "UPDATE USERLOGIN SET PASSWORD = '?' WHERE USERID = '?';";

	/** 修改密码的构造函数 */
	public PasswordService(String msg) {
		super(msg);
	}

	/** 修改密码的service函数 */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		Statement stat = null;
		try {
			db.setAutoCommit(false);
			stat = db.getStatement();
			if (msg.length == 3) // 3----密码丢失,获取密码
				update(stat);
			else {
				changePassword(stat, msg[0], msg[1]);// 2----更改密码
			}
			db.commit();
		} catch (SQLException e) {
			db.rollback();
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			// 释放数据库连接资源
			db.releaseConnection();
			// 关闭socket资源
			close();
		}
	}

	/** 密码丢失,用于重新获取并更改密码 */
	// 格式:
	// ID_password_RegisterEmail
	private void update(Statement stat) throws SQLException {
		try {
			ResultSet rs = stat.executeQuery(SQL_CHECK.replaceFirst("\\?",
					msg[0]));
			rs.next();

			if (rs.getString(1) == null || !rs.getString(1).equals(msg[2]))
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			else {
				changePassword(stat, msg[0], msg[1]);
				clientOutputStream.writeObject(ServiceFactory.SUCCESS);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 更改密码 */
	// 格式:
	// ID_password
	private void changePassword(Statement stat, String ID, String password)
			throws SQLException {
		String sql = SQL_UPDATE.replaceFirst("\\?", password);
		sql = sql.replaceFirst("\\?", ID);
		stat.executeUpdate(sql);
		try {
			clientOutputStream.writeObject(ServiceFactory.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
