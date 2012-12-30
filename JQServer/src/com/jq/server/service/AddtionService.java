package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jq.server.sql.DBSource;
import com.jq.util.Friend;

/** 服务器添加好友具体服务 */
public class AddtionService extends AbstractService {

	private static final String SQL_UPDATA = "INSERT INTO USERFRIENDS VALUES ('?','?');";

	private static final String SQL_GETINFO = "SELECT UserInfo.UserID, UserInfo.NickName,UserInfo.Remark, IP, Port, "
			+ "UserInfo.Face, Status, UserInfo.Description FROM UserIPInfo, UserInfo "
			+ "WHERE UserIPInfo.UserID = UserInfo.UserID AND UserInfo.UserID = '?';";

	public AddtionService(String msg) {
		super(msg);
	}

	/** 添加好友服务*/
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		Statement stat = null;

		try {
			if (msg.length == 2)
				update(db);
			else {
				stat = db.getStatement();
				getFriendInfo(stat, msg[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放数据库连接资源
			db.releaseConnection();
			// 关闭socket资源
			close();
		}
	}

	/** 获取好友信息 */
	private void getFriendInfo(Statement stat, String id) {
		String sql = SQL_GETINFO.replaceFirst("\\?", id);
		try {
			ResultSet rs = stat.executeQuery(sql);
			rs.next();

			clientOutputStream.writeObject(ServiceFactory.SUCCESS);
			clientOutputStream.writeObject(new Friend(rs.getString(1),
					rs.getString(2), rs.getString(3), rs
							.getString(4), rs.getInt(5), rs.getInt(6),
					rs.getInt(7), rs.getString(8)));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
			}
		}
	}

	/** 更新好友列表，加入好友ID。*/
	private void update(DBSource db) {
		//把SQL_UPDATA的问号换成(msg[0],msg[1])的形式
		String sql1 = SQL_UPDATA.replaceFirst("\\?", msg[0]);
		sql1 = sql1.replaceFirst("\\?", msg[1]);

		//把SQL_UPDATA的问号换成(msg[1],msg[0])的形式
		String sql2 = SQL_UPDATA.replaceFirst("\\?", msg[1]);
		sql2 = sql2.replaceFirst("\\?", msg[0]);

		try {
			Statement stat = db.getStatement();
			db.setAutoCommit(false);
			// 执行数据库更改
			stat.executeUpdate(sql1);
			stat.executeUpdate(sql2);
			db.commit();
			// 说明:添加用户不需要返回Friend,故返回Error
			getFriendInfo(stat, msg[1]);
		} catch (Exception e) {
			//若出错则回滚数据库
			db.rollback();
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
			}
		}
	}

}
