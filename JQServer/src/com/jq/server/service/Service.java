package com.jq.server.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jq.server.sql.DBSource;

/**
 * 	服务接口,提供各种服务器服务.
 * @param msg 接收到的信息.
 * @param clientInputStream 对象输入流
 * @param clientOutputStream 对象输出流
 * */
public interface Service {
	public void service(DBSource db,ObjectInputStream clientInputStream,ObjectOutputStream clientOutputStream);
}
