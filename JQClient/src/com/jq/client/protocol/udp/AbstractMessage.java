package com.jq.client.protocol.udp;

import com.jq.util.Param;

/**
 * 接收Message后对其进行的处理抽象类. 
 * 此类为抽象基类,其子类包括 
 * AddtionMsg,
 * NomalMsg,
 * OnlineMsg, 
 * OutlineMsg,
 * RemoveMsg,
 * SendFileMsg.
 * 子类将重写update函数以实现不同的更新功能.
 * */
public abstract class AbstractMessage {
	
	/** 发送者ID */
	protected String ID;
	/** 信息体 */
	protected String msgBody = null;
	
	/**
	 * 构造函数.
	 * 
	 * @param msg
	 *            接收到的消息. 消息格式 [ID_信息体]
	 * */
	public AbstractMessage(String msg) {
		/** 空格位置 */
		int i = msg.indexOf(Param.SPACE); 
		
		if(i == -1){
			ID = msg;
			msgBody = "";
		}else{
			ID = msg.substring(0, i);
			msgBody = msg.substring(i + 1);
		} 			
	}

	/**
	 * 子类改写的更新方法. 不同类型信息改写成不同的方法.
	 * */
	public abstract void update(UDPServer UDP);

	
}
