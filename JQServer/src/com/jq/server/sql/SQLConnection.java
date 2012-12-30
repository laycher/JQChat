package com.jq.server.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import com.jq.server.sql.DBSource;
import com.jq.server.sql.SQLPoolServer;
import com.jq.util.PropertyFile;

/**
 * [数据库操作类] 
 * 此类实现用户所需要的数据库操作.实现DBSource接口. 
 * 最终实现数据库连接池中的数据连接. 功能全部同步. 实现功能包括:
 * [1]数据的查询. 
 * [2]数据的更新. 
 * [3]数据的操作.
 * 
 */
public class SQLConnection implements DBSource {
	private static int num = 0; // 存储连接过的Connection数目
	private PropertyFile props = null; // jdbc config.properties
	private Connection conn = null; // 数据库连接Connection
	private Statement stmt = null; // 用于执行静态 SQL 语句并返回它所生成结果的对象
	private SQLPoolServer SQLPool = null; // 数据库连接池
	private int n = 0; // 连接编号

	/**
	 * @param PropertyFile  数据库配置文件对象.
	 * @param SQLPool 数据库连接池
	 */
	public SQLConnection(PropertyFile prop, SQLPoolServer SQLPool) {
		this.props = prop;
		this.SQLPool = SQLPool;

		try {
			//加载MySQL数据库驱动
			//Class.forName(props.getDriver()).newInstance(); 修改的
			Class.forName(props.getDriver());
			// 获取Connection连接. 
			//conn = DriverManager.getConnection(props.getURL(), props.getUser(),
			//		props.getPassword());
			conn = DriverManager.getConnection(props.getURL()+"?user="+props.getUser()+"&password="+props.getPassword()+"&useUnicode=true&characterEncoding=GBK" );
			//创建语句块对象
			stmt = conn.createStatement();
			//获取结果集对象是在具体操作时创建
			System.out.println("数据库" + (++num) + "连接...");
			n = num;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "<html>数据库配置文件错误,请重新配置<br>"
					+ e.getMessage() + "</html>", "错误",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/**
	 * 重写DBSource接口的commit()方法
	 * 使所有上一次提交/回滚后进行的更改成为持久更改， 并释放此 Connection 对象当前持有的所有数据库锁。
	 * 此方法只应该在已禁用自动提交模式时使用。
	 * */
	public void commit() {
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写DBSource接口的rollback()方法
	 * 取消在当前事务中进行的所有更改， 并释放此 Connection 对象当前持有的所有数据库锁。
	 * 此方法只应该在已禁用自动提交模式时使用。
	 * */
	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写DBSource接口的setAutoCommit()方法
	 * 将此连接的自动提交模式设置为给定状态。
	 * 如果连接处于自动提交模式下，则它的所有 SQL 语句将被执行并作为单个事务提交。
	 * 否则，它的 SQL 语句将聚集到事务中，直到调用 commit 方法或 rollback 方法为止。
	 * 默认情况下，新连接处于自动提交模式。
	 * 
	 * @param autoCommit  true 表示启用自动提交模式；为 false 表示禁用自动提交模式.
	 * */
	public void setAutoCommit(boolean autoCommit) {
		try {
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写DBSource接口的colseConnection()方法
	 * 关闭Connection连接.加上同步.
	 */
	public synchronized void closeConnection() {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("数据库" + (num--) + "关闭...");
	}

	/**
	 * 重写DBSource接口的getPrearedStatement()方法
	 * 获取PrepearedStatement.
	 * @param SQLstr
	 *            预编译的sql指令
	 * */
	public PreparedStatement getPreparedStatement(String SQLstr)
			throws SQLException {
		return conn.prepareStatement(SQLstr);
	}

	/**
	 * 返回连接过的Connection数目
	 */
	public int getNum() {
		return n;
	}

	public String toString() {
		return "数据库连接第 #" + n;
	}

	/**
	 * 重写DBSource接口的getStatement()方法
	 * 获取Statement.
	 * @return Statement 静态Statement
	 * */
	public Statement getStatement() throws SQLException {
		if (stmt == null)
			stmt = conn.createStatement();
		return stmt;
	}

	/**
	 * 重写DBSource接口的releaseConnection()方法
	 * Connection使用完毕后向数据库连接池归还Connection
	 * */
	public void releaseConnection() {
		SQLPool.returnSQLServer(this);
		System.out.println("释放:" + getNum());
	}
}
