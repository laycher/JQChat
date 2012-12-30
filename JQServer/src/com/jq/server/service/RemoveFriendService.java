
package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.sql.DBSource;

/**删除好友具体服务*/
public class RemoveFriendService  extends AbstractService{
	
	private static final String SQL_REMOVE = "DELETE FROM USERFRIENDS WHERE USERID = '?' AND FRIENDID = '?';";

	/** 删除好友构造类 */
	public RemoveFriendService(String msg) {
		super(msg);
	}

	/** 删除好友具体运行函数 */
	public void service(DBSource db, ObjectInputStream clientInputStream, ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;
		
		try {
			if (updata(db))
				clientOutputStream.writeObject(ServiceFactory.SUCCESS);
			else
				clientOutputStream.writeObject(ServiceFactory.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {}
		} finally {
			//释放数据库连接资源
			db.releaseConnection();
			//关闭socket资源
			close();
		}
	}
	
	/** 更新数据库，双方用户的好友关系都要解除 
	 * @return ture 成功 false 失败*/
	private boolean updata(DBSource db){
		String sql1 = SQL_REMOVE.replaceFirst("\\?", msg[0]);
		sql1 = sql1.replaceFirst("\\?", msg[1]);
		
		String sql2 = SQL_REMOVE.replaceFirst("\\?", msg[1]);
		sql2 = sql2.replaceFirst("\\?", msg[0]);
		
		try {
			Statement stat = db.getStatement();
			db.setAutoCommit(false);
			stat.executeUpdate(sql1);
			stat.executeUpdate(sql2);
			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			db.rollback();
			return false;
		}
		
		return true;
	}
	

}
