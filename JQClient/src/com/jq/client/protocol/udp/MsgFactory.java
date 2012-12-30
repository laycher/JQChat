package com.jq.client.protocol.udp;

import java.lang.reflect.Constructor;

/**
 * 	信息工厂类.用于产生特定类型的消息处理类.
 * */
public class MsgFactory {
	
	/** 信息服务加载类 */
	private static final String[] TYPE = {
			"com.jq.client.protocol.udp.NomalMsg",
			"com.jq.client.protocol.udp.OnlineMsg",
			"com.jq.client.protocol.udp.OutlineMsg",
			"com.jq.client.protocol.udp.AddtionMsg",
			"com.jq.client.protocol.udp.RemoveMsg",
			"com.jq.client.protocol.udp.SendFileMsg",
			"com.jq.client.protocol.udp.RejectMsg"};

	/**正常消息类型*/
	public static final int NOMALMSG = 0;
	/**上线消息类型*/
	public static final int ONLINEMSG = 1;
	/**下线消息类型*/
	public static final int OUTLINEMSG = 2;
	/**好友添加消息类型*/
	public static final int ADTIONMSG = 3;
	/**好友删除消息类型*/
	public static final int REMOVEMSG = 4;
	/**发送文件消息类型*/
	public static final int SENDFILEMSG = 5;
	/**拒绝接收文件消息类型*/
	public static final int REJECTMSG = 6;
	
	/**
	 * 获取特定信息类来处理消息.
	 * 
	 * @param msg
	 *            接收到的消息
	 *            消息格式[类型_ID_信息体].
	 *            类型:
	 *            			0-----普通聊天信息.
	 *            			1-----好友上线信息.
	 *            			2-----好友下线信息.
	 *            			3-----好友添加信息.
	 *            			4-----好友删除信息.
	 *            			5-----发送文件信息.
	 *            
	 * @return AbstractMessage 返回用于处理消息的消息处理类,如果失败返回null.
	 * @throws ClassNotFoundException
	 * */
	public static AbstractMessage getMsg(String msg){
		/*解析--->消息类型*/
		int type = Integer.parseInt(msg.substring(0, 1));
		/*解析--->ID_信息体*/
		msg = msg.substring(1);

		/*使用反射机制加载相关类*/
		String className = TYPE[type];
		Class<?> c;
		@SuppressWarnings("rawtypes")
		Constructor constructor;
		try {
			c = Class.forName(className);
			/*带参数的构造器.*/
			constructor = c.getConstructor(String.class);
			return (AbstractMessage) constructor.newInstance(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
