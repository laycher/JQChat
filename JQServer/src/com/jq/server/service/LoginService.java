package com.jq.server.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jq.server.sql.DBSource;
import com.jq.util.Friend;

/**
 * 具体用户登陆服务. . 
 * 登陆成功后返回 用户资料、好友资料和用户接受到的离线消息.
 * 状态说明: 0-未登录(服务器使用) 1-隐身. 2-离开. 3-忙碌. 4-在线.
 * 
 * */
public class LoginService extends AbstractService {
	/** 执行查询数据库得到的结果 */
	private ResultSet rs = null;

	/** 登陆账号密码核对 */
	private static final String SQL_LOGIN = "SELECT * FROM USERLOGIN WHERE USERID = '?' AND PASSWORD = '?';";
	/** 获取用户自己信息 */
	private static final String SQL_INFOS_ME = "SELECT UserInfo.UserID, UserInfo.NickName,UserInfo.Remark,"
			+ "UserInfo.Face, UserInfo.Description, status FROM UserInfo, UserIPInfo "
			+ "WHERE UserIPInfo.UserID = UserInfo.UserID AND UserInfo.UserID = '?';";
	/** 获取好友信息，按状态排序 */
	private static final String SQL_INFOS_FRIENDS = "SELECT UserInfo.UserID, UserInfo.NickName,UserInfo.Remark, IP, Port, "
			+ "UserInfo.Face, Status, UserInfo.Description FROM UserIPInfo, UserInfo "
			+ "WHERE UserIPInfo.UserID = UserInfo.UserID AND UserInfo.UserID in "
			+ "(SELECT FriendID FROM UserFriends WHERE UserID = '?') ORDER BY Status DESC;";
	/** 获取离线信息 */
	private static final String SQL_LEFTINFO = "SELECT Content From LeftInfo WHERE IDTo = '?';";
	/** 删除离线消息 */
	private static final String SQL_DELETELEFT = "DELETE FROM LEFTINFO WHERE IDTo = '?';";
	/** 更新用户IP信息 */
	private static final String SQL_UPDATA = "UPDATE USERIPINFO SET IP = '?', PORT = '?' ,STATUS = '?' WHERE USERID = '?';";

	/** 登录服务(LoginService)的构造方法
	 * @param msg 的格式是：用户ID+密码+IP+端口+状态 */
	public LoginService(String msg) {
		//调用父类(AbstractService)的构造方法
		super(msg);
	}

	/** 登录服务的service运行的主要方法 */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		Statement stat = null;
		try {
			stat = db.getStatement();
			if (check(stat)) {
				/* 账号密码正确,返回好友信息ResultSet和自己的信息(其中自己的信息在第一). */
				// 添加自己的信息.
				//stat = db.getStatement();
				rs = getInfos(stat, true);

				// 获取好友列表
				if (null != rs) {
					/* 发送登陆成功消息 */
					clientOutputStream.writeObject(ServiceFactory.SUCCESS);
					stat = db.getStatement();
					/* 获取好友列表 */
					getInfos(stat, false);
					/* 发送好友信息 */
					clientOutputStream.writeObject(friendslist);
					List<String> leftInfo = getLeftInfo(db);
					if (leftInfo != null) {
						// 发送离线信息标志.
						clientOutputStream.writeObject(ServiceFactory.SUCCESS);
						clientOutputStream.writeObject(leftInfo);
					} else {
						clientOutputStream.writeObject(ServiceFactory.ERROR);
					}
					// 更新用户IP信息.
					updataIpInfo(db);
				} else
					return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
			// 释放数据库连接资源
			db.releaseConnection();
			// 关闭socket资源
			close();
		}
	}

	/** 查看账号密码是否正确 
	 * @return true 正确  false 错误 */
	private boolean check(Statement stat) {
		try {
			String SQL = SQL_LOGIN.replaceFirst("\\?", msg[0]);
			SQL = SQL.replaceFirst("\\?", msg[1]);
			rs = stat.executeQuery(SQL);

			// 账号密码错误 
			if (!rs.next()) {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** 获取用户自己及用户好友资料
	 * @return 结果集ResultSet */
	private ResultSet getInfos(Statement stat, boolean flag) {
		try {
			String SQL = null;
			if (flag)
				SQL = SQL_INFOS_ME.replaceFirst("\\?", msg[0]);
			else
				SQL = SQL_INFOS_FRIENDS.replaceFirst("\\?", msg[0]);
			
			rs = stat.executeQuery(SQL);

			if (flag) { // 获取用户自己的资料
				rs.next();
				if (rs.getInt(6) != 0) { // 已登录.0为未登录.
					// 向客户端发送错误标示
					clientOutputStream.writeObject(ServiceFactory.LOGINED);
					return null;
				}
				friendslist.add(new Friend(rs.getString(1), rs
						.getString(2), rs.getString(3), null, 0, rs
						.getInt(4), 1, rs.getString(5)));
			} else {
				// 获取好友的资料
				while (rs.next()) {
					friendslist.add(new Friend(rs.getString(1), rs
							.getString(2), rs.getString(3), rs
							.getString(4), rs.getInt(5), rs.getInt(6),
							rs.getInt(7), rs.getString(8)));
				}
			}
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取登陆用户的接收到的离线消息.
	 * 
	 *            离线信息接收方的ID.
	 * */
	private List<String> getLeftInfo(DBSource db) {
		Statement stat = null;
		try {
			stat = db.getStatement();
			String SQL = SQL_LEFTINFO.replaceFirst("\\?", msg[0]);
			rs = stat.executeQuery(SQL);

			List<String> left = null;
			while (rs.next()) {
				if (left == null)
					left = new ArrayList<String>();

				left.add(rs.getString(1));
			}

			return left;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			removeLeftMsg(db, stat);
		}
		return null;
	}

	/** 删除离线消息 */
	private void removeLeftMsg(DBSource db, Statement stat) {
		try {
			/* 将读取的离线信息删除 */
			//stat = db.getStatement();
			String SQL = SQL_DELETELEFT.replaceFirst("\\?", msg[0]);
			/* 使用事务 */
			db.setAutoCommit(false);
			// 执行操作
			stat.executeUpdate(SQL);
			/* 确认提交 */
			db.commit();
		} catch (SQLException e) {
			/* 失败回滚 */
			db.rollback();
			e.printStackTrace();
		}
	}

	/** 更新用户IP信息 */
	private void updataIpInfo(DBSource db) {
		try {
			Statement stat = db.getStatement();

			String SQL = SQL_UPDATA.replaceFirst("\\?", msg[2]);
			SQL = SQL.replaceFirst("\\?", msg[3]);
			SQL = SQL.replaceFirst("\\?", msg[4]);
			SQL = SQL.replaceFirst("\\?", msg[0]);

			// 执行操作
			/* 使用事务 */
			db.setAutoCommit(false);
			// 执行操作
			stat.executeUpdate(SQL);
			/* 确认提交 */
			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
