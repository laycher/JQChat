package com.jq.server.sql;

import java.sql.*;

public class Datebase {

	//连接数据库
	public Connection getConnection()	
	{
		Connection con = null;
		//String url = "jdbc:mysql://localhost/JQ";	//定义数据库的URL
		String username="root";	//数据库的用户名
		String password = "123456";	//数据库的密码
		try{
			Class.forName("com.mysql.jdbc.Driver");	
			//con = DriverManager.getConnection(url,username,password);	//登录到建立的数据库上
			con = DriverManager.getConnection("jdbc:mysql://localhost/JQ?user="+username+"&password="+password+"&useUnicode=true&characterEncoding=gb2312" );
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	
	//关闭结果集、语句块、连接
	public static void close(ResultSet rs,Statement st,Connection con){
		close(rs);
		close(st);
		close(con);
	}
	
	//关闭语句块与连接
	public static void close(Statement st,Connection con){
		close(st);
		close(con);
	}
	
	//关闭结果集
	private static void close(ResultSet rs) {
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	//关闭语句块
	private static void close(Connection con) {
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	//关闭连接
	private static void close(Statement st) {
		if(st != null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//查询所有已注册用户
	public void readRegistedUser() throws Exception{
		Datebase db = new Datebase();
		Connection con = db.getConnection();	//得到数据连接
		Statement st = con.createStatement();	//得到语句块
		String sql = "select * from user";	//编写SQL语句块
		ResultSet rs = st.executeQuery(sql);	//得到结果集
		while(rs.next()){
			System.out.print(rs.getString(1)+"\t");	//获取ID	int(8)
			System.out.print(rs.getString(2)+"\t");	//获取name varchar(20)
			System.out.print(rs.getString(3)+"\t");	//获取email varchar(20)
			System.out.print(rs.getString(4)+"\t");	//获取password varchar(20)
			System.out.print(rs.getString(5)+"\t");	//获取comment varchar(80)
			System.out.print(rs.getString(6)+"\t");	//获取head	varchar(10)
			System.out.println("");
		}
		Datebase.close(rs, st, con);
	}
	
	//查询在线用户
	public void readOnlineUser() throws Exception{
		Datebase db = new Datebase();
		Connection con = db.getConnection();	//得到数据连接
		Statement st = con.createStatement();	//得到语句块
		String sql = "select * from user where id in (select id from user_status where status=1)";	//编写SQL语句块
		ResultSet rs = st.executeQuery(sql);	//得到结果集
		while(rs.next()){
			System.out.print(rs.getString(1)+"\t");	//获取ID	int(8)
			System.out.print(rs.getString(2)+"\t");	//获取name varchar(20)
			System.out.print(rs.getString(3)+"\t");	//获取email varchar(20)
			System.out.print(rs.getString(4)+"\t");	//获取password varchar(20)
			System.out.print(rs.getString(5)+"\t");	//获取comment varchar(80)
			System.out.print(rs.getString(6)+"\t");	//获取head	varchar(10)
			System.out.println("");
		}
		Datebase.close(rs, st, con);
	}

	//注册一个新用户 id,name,email,password,comment
	public void addUser(String id,String name,String email,String password,String comment) throws Exception{
		Datebase db = new Datebase();
		Connection con = db.getConnection();
		Statement st = con.createStatement();
		String sql = "insert into user values('"+id+"','"+name+"','"+email+"','"+password+"','"+comment+"','01.png');";//head没有弄，默认是01.png
		//String sql = "insert into user(name,email,password,comment) values('"+name+"','"+email+"','"+password+"','"+comment+"');";//head没有弄，默认是01.png
		//INSERT INTO USER(NAME,email,PASSWORD) VALUES ('Laycher','laycher@live.cn','123456')
		st.executeUpdate(sql);
		System.out.println(sql);
		sql = "insert into user_status values('"+id+"','0')";	//默认是不在线
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//设置在线状态
	public void setUserOnline(String id) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user_status set status=1 where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	//设置为不在线状态
	public void setUserNotOnline(String id) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user_status set status=0 where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//设置昵称
	public void setUserName(String id,String name)throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user set name='"+name+"'where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	} 
	
	//设置新密码
	public void setUserPassword(String id,String password) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user set password='"+password+"'where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//设置头像
	public void setUserHead(String id,String head) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="update user set head='"+head+"'where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//删除用户，这个还是不要用吧
	public void delUser(String id) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="delete from user_status where id='"+id+"'";
		st.executeUpdate(sql);
		sql = "delete from user where id='"+id+"'";
		st.executeUpdate(sql);
		Datebase.close(st, con);
	}
	
	//添加好友
	public void search(String str) throws Exception{
		Connection con = new Datebase().getConnection();
		Statement st = con.createStatement();
		String sql="select distinct id,name,comment from user where id like '%"+str+"%' or name like '%"+str+"%'";
		ResultSet rs =st.executeQuery(sql);
		while(rs.next()){
			System.out.print(rs.getString(1)+"\t");	//获取ID	int(8)
			System.out.print(rs.getString(2)+"\t");	//获取name varchar(20)
			System.out.print(rs.getString(3)+"\t");	//获取comment varchar(80)
			System.out.println("");
		}
		Datebase.close(rs, st, con);
	}
	
	public static void main(String[] args){
		Datebase db = new Datebase();
		try {
			//db.addUser("100006", "Laycher", "laycher@live.cn", "123456", null);
			//db.setUserNotOnline("100005");
			//db.setUserName("100005", "Laycher");
			//db.setUserPassword("100005", "000000");
			//db.setUserHead("100005", "02.png");
			//db.delUser("100006");
			//db.readRegistedUser();
			db.search("2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
