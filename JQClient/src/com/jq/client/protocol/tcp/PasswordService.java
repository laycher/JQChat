package com.jq.client.protocol.tcp;

/**获取更改密码具体服务
 * 继承自AbstractService
 * 实现于Service<String,String>接口*/
public class PasswordService  extends AbstractService implements Service<String,String>{

	/**获取更改密码具体服务*/
	public PasswordService(TCPServer TCP) {
		super(TCP);
	}

	/**更改密码的具体方法
	 * @param msg 格式：ID_PW
	 * @return flag 标志是否成功*/
	public String service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_PASSWORD + msg);
			return (String)clientInputStream.readObject();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

}
