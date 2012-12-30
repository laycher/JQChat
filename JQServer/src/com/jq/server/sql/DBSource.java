package com.jq.server.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/** DBSource接口,用于规范获得Connection的方法. 可以获得,关闭取得的Connection对象.*/
public interface DBSource {
	/**
	 * 将此连接的自动提交模式设置为给定状态。
	 * @param autoCommit : true 表示启用自动提交模式； false 表示禁用自动提交模式.
	 * */
	public void setAutoCommit(boolean autoCommit);
	
	/**
	 * 	使所有上一次提交/回滚后进行的更改成为持久更改，
	 *  并释放此 Connection 对象当前持有的所有数据库锁。
	 *  此方法只应该在已禁用自动提交模式时使用。
	 * */
	public void commit();
	
	/**
	 *  取消在当前事务中进行的所有更改，
	 *  并释放此 Connection 对象当前持有的所有数据库锁。
	 *  此方法只应该在已禁用自动提交模式时使用。
	 * */
	public void rollback();
	
	/**
	 * 	获取PrepearedStatement.
	 * @param SQLstr 预编译的sql指令
	 * */
	public PreparedStatement getPreparedStatement(String SQLstr) throws SQLException;
	
	/**
	 * 	获取Statement.
	 *  静态Statement
	 * */
	public Statement getStatement() throws SQLException;

	/**
	 * 关闭Connection对象,实现同步机制
	 */
	public void closeConnection() throws SQLException;
	
	/**
	 * 	Connection使用完毕后向数据库连接池归还Connection
	 * */
	public void releaseConnection();
}