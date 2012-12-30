package com.jq.util;

import java.net.InetSocketAddress;

/** 存取IP信息的类，包括服务器的IP地址和端口以及客户端的端口*/
public class IPInfo {
	private static int cPort = 8000;
	private static String IP = "127.0.0.1";
	private static int sPort = 6000;
	private static int fPort = 7000;
	
	/** 返回IP地址 */
	public static String getIP() {
		return IP;
	}
	public static void setIP(String iP) {
		IP = iP;
	}
	
	/** 返回客户端占用端口.*/
	public static int getClientPort() {
		return cPort;
	}
	public static  void setClientPort(int pORT) {
		cPort = pORT;
	}
	
	/** 返回服务器端占用端口.*/
	public static int getServerPort() {
		return sPort;
	}
	public static void setServerPort(int pORT) {
		sPort = pORT;
	}
	
	/**
	 * 	返回服务器信息.
	 * */
	public static InetSocketAddress getServerInetAddress(){
		return new InetSocketAddress(IP,sPort);
	}
	
	public static void setFilePort(int fPort) {
		IPInfo.fPort = fPort;
	}
	
	public static int getFilePort() {
		return fPort;
	}
}
