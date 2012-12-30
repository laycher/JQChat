package com.jq.server.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.service.ServiceFactory;
import com.jq.server.sql.DBSource;
import com.jq.util.Param;

/** 用户注册具体服务 */
public class RegisterService extends AbstractService {

	private String ID = null;

	private static final String SQL_MAXID = "SELECT MAX(USERID) FROM USERLOGIN;";

	private static final String SQL_USERIPINFO = "INSERT INTO USERIPINFO VALUES ('?','0.0.0.0',0,0);";

	private static final String SQL_USERLOGIN_INSERT = "INSERT INTO USERLOGIN VALUES ('?','?');";

	private static final String SQL_USERLOGIN_UPDATE = "UPDATE USERLOGIN SET PASSWORD = '?' WHERE USERID = '?';";

	private static final String SQL_USERLOGIN_REMOVE = "DELETE FROM USERLOGIN WHERE USERID = '?';";

	private static final String SQL_USERINFO = "INSERT INTO USERINFO SET USERID = '?', NICKNAME = '?', RegisterEmail = '?' , FACE = 15, AGE = 20;";

	/** 用户注册 构造函数 */
	public RegisterService(String msg) {
		super(msg);
	}

	/** 用户注册 服务函数 */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		try {
			String newID = getNewID(db.getStatement(), db);
			clientOutputStream.writeObject(newID);
			String infos = (String) clientInputStream.readObject();

			update(db, infos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放数据库连接资源
			db.releaseConnection();
			// 关闭socket资源
			close();
		}
	}

	// 格式: ID_Password_nickname_RegisterEmail
	/** 将用户信息 写入数据库 */
	private void update(DBSource db, String info) {
		Statement stat = null;
		String[] infos = null;
		try {
			stat = db.getStatement();

			// 用户取消注册
			if (info.equals(ServiceFactory.ERROR)) {
				// 失败后删除新ID数据
				db.setAutoCommit(false);
				stat.executeUpdate(SQL_USERLOGIN_REMOVE.replaceFirst("\\?",
						String.valueOf(ID)));
				db.commit();
				return;
			}

			infos = info.split(Param.SPACE);

			String sql_ip = SQL_USERIPINFO.replaceFirst("\\?", infos[0]);

			String sql_login = SQL_USERLOGIN_UPDATE.replaceFirst("\\?", infos[1]);
			sql_login = sql_login.replaceFirst("\\?", infos[0]);

			String sql_info = SQL_USERINFO.replaceFirst("\\?", infos[0]);
			sql_info = sql_info.replaceFirst("\\?", infos[2]);
			sql_info = sql_info.replaceFirst("\\?", infos[3]);

			db.setAutoCommit(false);
			stat.executeUpdate(sql_ip);
			stat.executeUpdate(sql_login);
			stat.executeUpdate(sql_info);
			db.commit();

			clientOutputStream.writeObject(ServiceFactory.SUCCESS);
		} catch (Exception e) {
			db.rollback();
			try {
				// 失败后删除新ID数据
				db.setAutoCommit(false);
				stat.executeUpdate(SQL_USERLOGIN_REMOVE.replaceFirst("\\?",
						String.valueOf(infos[0])));
				db.commit();
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (Exception e1) {
			}
			e.printStackTrace();
		}
	}

	/** 新用户注册处理. 返回给用户在数据库中已存在用户中ID号码最大的+1的新号码.
	 * @return 新ID */
	private String getNewID(Statement stat, DBSource db) {
		int newID = 1000;

		/** 设置数据库连接同步,防止多个用户同时申请新用户造成ID号重复 */
		synchronized (SQLServerProcess.sqlPool) {
			try {
				/* 设置事务 */
				ResultSet rs = stat.executeQuery(SQL_MAXID);
				if (rs.next() && rs.getString(1) != null)
					newID = Integer.parseInt(rs.getString(1)) + 1;

				ID = String.valueOf(newID);
				// 将新ID插入数据库,防止多线程冲突
				db.setAutoCommit(false);
				stat.executeUpdate(SQL_USERLOGIN_INSERT.replaceFirst("\\?", ID));
				db.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				return String.valueOf(newID);
			}
		}

		return String.valueOf(newID);
	}

}
