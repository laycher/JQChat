package com.jq.client.protocol.tcp;

import com.jq.client.gui.login.Register;

/**用户注册具体服务
 * 继承自AbstractService
 * 实现于Service<String,String>接口*/
public class RegisterService extends AbstractService implements Service<String,String>{

	private Register registerFrame = null;
	
	/** 注册的构造函数 */
	public RegisterService(TCPServer TCP) {
		super(TCP);
		//System.out.println("启动注册");
	}

	/**
	 * 注册实现的方法
	 * 格式:  ID_PW_nickname_Email
	 * @param msg 注册具体信息.
	 * @return 返回一个新的ID号码
	 * */
	public String service(String msg) {
		try {
			registerFrame = new Register(this);
			
			clientOutputStream.writeObject(ServiceFactory.TASK_NEWUSER + msg);
			String newID =  (String)clientInputStream.readObject();
			
			registerFrame.setID(newID);
			registerFrame.setVisible(true);
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
		return null;
	}	
}
