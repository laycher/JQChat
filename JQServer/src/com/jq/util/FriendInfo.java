package com.jq.util;

import java.io.Serializable;

/** 存储好友资料 */
public class FriendInfo implements Serializable {

	private static final long serialVersionUID = 7524772647377526557L;

	public String id = null;
	public String nickname = null;
	public String signedString = null;
	public String face = "2";

	public String sex = "男";
	public String province = null;
	public String city = null;
	public String mail = null;
	public String homePage = null;
	public int age = 0;
	public String academy = null;
	public String department = null;
	
	/**
	 * 构造函数.
	 * 
	 * @param sex
	 *            性别.
	 * @param province
	 *            省份.
	 * @param city
	 *            城市.
	 * @param mail
	 *            邮箱.
	 * @param homePage
	 *            主页.
	 * @param age
	 *            年龄.
	 * @param Academy
	 *            院系.
	 * @param department
	 *            专业.
	 * */
	public FriendInfo(String sex, String province, String city, String mail,
			String homePage, String age, String academy, String department) {
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.mail = mail;
		this.homePage = homePage;
		if (age.length() != 0)
			this.age = Integer.parseInt(age);
		else
			this.age = 18; // 默认18

		this.academy = academy;
		this.department = academy;
	}
}
