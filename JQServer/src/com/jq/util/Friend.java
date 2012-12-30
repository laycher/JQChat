package com.jq.util;

import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.jq.util.Friend;
import com.jq.util.MyResources;

/**
 * 	[用户基本资料类.]
 * 	Friend
 * 	用于构造好友的基本数据对象.
 *  基本数据包括用户ID,在线状态,登陆IP,端口,个性签名等
 *  即时性资料.
 *  
 * 	实现序列化,可用于网络传输对象.
 * 
 * */
public class Friend implements Serializable {

	private String	ID = null; 			// 好友JQ号码
	private String 	nickName = null; 	// 好友昵称
	private	String	remark;				// 好友备注
	private String 	IP = null; 	// 好友当前IP地址
	private int 	port = 0; 			// 好友当前客户端占用端口
	private int 	face = 1; 			// 好友当前头像
	private int 	status = 0; 		// 好友当前状态,0:不在线 1:在线.....
	private String 	signedString = null;// 好友个性签名
	
	private ImageIcon	faceImage = null;	
	
	private static final long serialVersionUID = -5250532791652145335L;

	/**
	 * 构造函数.
	 * @param ID
	 *            用户JQ号码
	 * @param nickName
	 *            用户昵称
	 * @param remark
	 * 			  好友备注
	 * @param IP
	 *            用户当前IP地址
	 * @param port
	 *            用户当前客户端占用端口
	 * @param face
	 *            用户头像
	 * @param status
	 *            用户当前状态
	 * @param signedString
	 *            用户个性签名
	 * */
	public Friend(String ID, String nickName, String remark,String IP, int port, int face,
												int status, String signedString) {
		this.ID = ID;
		this.nickName = nickName;
		this.remark = remark;
		this.IP = IP;
		this.port = port;
		this.face = face;
		this.status = status;
		this.signedString = signedString;
		
	}

	/**
	 * 	返回好友ID号码.
	 * 	@return String 返回好友ID号码.
	 * */
	public String getID() {
		return ID;
	}
	
	/**
	 * 	返回好友昵称.
	 * 	@return String 返回好友昵称.
	 * */
	public String getNickName() {
		return nickName;
	}
	
	/**
	 * 	获取好友备注
	 * 	@return String 好友备注
	 * */
	public String getRemark() {
		return remark;
	}

	/**
	 * 	返回好友当前IP地址.
	 * 	@return String 返回好友当前IP地址.
	 * */
	public String getIP() {
		return IP;
	}
	
	/**
	 * 	返回好友当前客户端端口.
	 * 	@return int 返回当前好友客户端端口.
	 * */
	public int getPort() {
		return port;
	}
	
	/**
	 * 	获取在头像编号.
	 *  @return int 返回头像的编号
	 * */
	public int getFace(){
		return face;
	}
	
	/**
	 * 	返回好友当前状态.
	 *  0:不在线;1:在线
	 * 	@return int 返回好友当前状态.
	 * */
	public int getStutas() {
		return status;
	}
	
	/**
	 * 	返回好友的个性签名.
	 * 	@return String 返回好友个性签名.
	 * */
	public String getSignedString() {
		return signedString;
	}
	
	/**
	 * 	此方法应在客户端调用,用于返回此用户的头像ImageIcon.
	 * 	@return ImageIcon 返回当前用户头像.
	 * */
	public ImageIcon getFaceIcon() {
		if (status == 0 || status == 1)		/*未上线或隐身*/
			faceImage = MyResources.getImageIcon(MyResources.USERFACE + face + "-1.gif");
		else
			faceImage = MyResources.getImageIcon(MyResources.USERFACE + face + ".gif");
		return faceImage;
	}
	
	/**
	 * 	状态图标
	 * 	状态说明:
	 * 			0-未登录(服务器使用)
	 * 			1-隐身.
	 * 			2-离开.
	 * 			3-忙碌.
	 * 			4-在线.
	 * */
	public ImageIcon getStutasIcon(){
		if (status == 1)
			return MyResources.getImageIcon(MyResources.ICON + "hide.gif");
		else if (status == 2)
			return MyResources.getImageIcon(MyResources.ICON + "leave.gif");
		else if (status == 3)
			return MyResources.getImageIcon(MyResources.ICON + "nobother.gif");
		else if (status == 4)
			return MyResources.getImageIcon(MyResources.ICON + "online.gif");
		return null;
	}

	/**
	 * 	设置好友昵称.
	 * 	@param name 好友昵称.
	 * */
	public void setNickName(String name) {
		nickName = name;
	}
	
	/**
	 * 	设置好友备注
	 * 	@param remark 设置好友备注
	 * */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 	设置IP地址
	 * */
	public void setIP(String IP){
		this.IP = IP;
	}
	
	/**
	 * 	设置端口
	 * */
	public void setPort(int port){
		this.port = port;
	}

	/**
	 * 	设置好友头像.
	 * 	@param i 好友头像.
	 * */
	public void setFace(Icon i) {
		faceImage = (ImageIcon) i;
	}
	
	/**
	 * 	设置头像编号.
	 * */
	public void setFace(int i){
		face = i;
	}
	
	/**
	 * 	设置好友在线状态.
	 * */
	public void setStutas(int i){
		status = i;
	}
	
	/**
	 * 	设置好友个性签名.
	 * 	@param s 好友个性签名.
	 * */
	public void setSignedString(String s) {
		signedString = s;
	}
	
	public String toString() {
		return ID + "[" + nickName + "]";
	}
	
	
	/**
	 * 	重写equals.很重要.
	 * 	在MyList中查找项时使用.
	 * */
	@Override
	public boolean equals(Object arg0) {
		Friend f = (Friend)arg0;
		return ID == f.ID;
	}

	@Override
	public int hashCode() {
		return ID.hashCode();
	}

}
