package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Statement;

import com.jq.server.sql.DBSource;
import com.jq.util.FriendInfo;

/** 更新用户资料具体服务 */
public class UpdateInfoService extends AbstractService {

	private static final String SQL_UPDATA = "Update UserInfo set Nickname = '?' , face = ? , sex = '?', age = ?, Academy = '?', department = '?', "
			+ "Provence = '?', City = '?', Email = '?', Homepage = '?', Description = '?' where UserID = '?';";

	/** 更新用户资料的构造方法 */
	public UpdateInfoService(String msg) {
		super(msg);
	}

	/** 更新用户资料的具体方法  */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		try {
			FriendInfo myInfo = (FriendInfo) clientInputStream.readObject();
			updateInfo(db, myInfo);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// 释放数据库连接资源
			db.releaseConnection();
			// 关闭socket资源
			close();
		}
	}

	/** 更新用户资料的数据库操作方法 */
	private void updateInfo(DBSource db, FriendInfo myInfo) {
		String sql = SQL_UPDATA;

		sql = sql.replaceFirst("\\?", myInfo.nickname);
		sql = sql.replaceFirst("\\?", myInfo.face);
		sql = sql.replaceFirst("\\?", myInfo.sex);
		sql = sql.replaceFirst("\\?", "" + myInfo.age);
		sql = sql.replaceFirst("\\?", myInfo.academy);
		sql = sql.replaceFirst("\\?", myInfo.department);
		sql = sql.replaceFirst("\\?", myInfo.province);
		sql = sql.replaceFirst("\\?", myInfo.city);
		sql = sql.replaceFirst("\\?", myInfo.mail);
		sql = sql.replaceFirst("\\?", myInfo.homePage);
		sql = sql.replaceFirst("\\?", myInfo.signedString.toString());
		sql = sql.replaceFirst("\\?", myInfo.id);

		try {
			Statement stat = db.getStatement();
			db.setAutoCommit(false);
			// 执行数据库更改
			stat.executeUpdate(sql);
			db.commit();

			clientOutputStream.writeObject(ServiceFactory.SUCCESS);
		} catch (Exception e) {
			db.rollback();

			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
			}
		}
	}

}
