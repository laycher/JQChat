package com.jq.server.sql;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jq.server.sql.DBSource;
import com.jq.server.sql.SQLConnection;
import com.jq.util.PropertyFile;

/** [数据库连接池类] 
 * 此类实现一个数据库连接池 增强对数据库的使用效率.
 * 类似线程池. */
public class SQLPoolServer {

	private PropertyFile prop = null; // 读取的配置文件
	private BlockingQueue<DBSource> SQLPool = null; // 数据库连接池
	private int maxPool = 0; // 连接池最大数目

	/** 传递进来数据库配置文件对象PropertyFile	，读取里面的线程池的最大数目
	 * 创建maxPool个连接（DBSource），并存放到SQLPool连接池中（Queue） */
	public SQLPoolServer(PropertyFile properFile) {
		prop = properFile;
		SQLPool = new LinkedBlockingQueue<DBSource>();
		maxPool = prop.getMax();

		System.out.println("启动数据库池");
		// 生成连接池
		for (int i=1; i<=maxPool; i++) {
			DBSource db = new SQLConnection(prop, this );
			try {
				SQLPool.put(db);
				System.out.println("创建第# "+i+"个连接，并放到连接池中");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        System.out.println("启动数据库池完成，共"+maxPool+"个");	
	}

	/** 使用阻塞队列 
	 * 获得SQLConnection对象用来执行用户对数据库的操作. 
	 * 若阻塞队列中没有元素,则阻塞等待. */
	public synchronized DBSource getSQLServer() {
		DBSource db = null;
		try {
			db = SQLPool.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			db = null;
		}
		return db;
	}

	/** 使用完后向数据库连接池归还SQLServer */
	public void returnSQLServer(DBSource db) {
		try {
			SQLPool.put(db);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** 关闭连接池	 */
	public void closePoolServer() throws SQLException {
		DBSource db = null;
		for (int i = 0; i < SQLPool.size(); i++) {
			try {
				db = SQLPool.take();
				db.closeConnection();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// SQLPoolServer s = new SQLPoolServer(new
	// PropertyFile("src/resources/properties/jdbc配置文件.properties"));

}
