package com.jq.client.protocol.udp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.jq.util.IPInfo;
import com.jq.util.Param;

/**发送文件消息类*/
public class SendFileMsg extends AbstractMessage{

	/*
	String fileName = "";
	String fileContent = "";
	*/
	private String fileName = "";
	private String fileSendID;
	private String fileSendIP;
	private int fileSendPort;
	
	/**发送文件的构造函数
	 * @param msg*/
	public SendFileMsg(String msg) {
		super(msg);
		/*
		int i = msgBody.indexOf(Param.SPACE);
		if(i == -1){
			fileName = msgBody;
			fileContent = "";
		}else{
			fileName = msgBody.substring(0, i);
			fileContent = msgBody.substring(i+1);
		}*/
		String[] mess = msgBody.split(Param.SPACE);
		//System.out.println(mess);
		fileSendID = ID;
		fileName = mess[0];
		fileSendIP = mess[1];
		fileSendPort = Integer.parseInt(mess[2]);
		
	}

	/** 重写update方法 */
	@Override
	public void update(UDPServer UDP) {
		ServerSocket ss;
		Socket s;
		try {
			ss = new ServerSocket(IPInfo.getFilePort());
			OutputStream os = null;
			InputStream is = null;
			byte[] buffer = new byte[Param.DATA_SIZE];
			int cnt = 0;
			int i = JOptionPane.showConfirmDialog(null, "用户[" + fileSendID
					+ "]发来文件\"" + fileName + "\",\n 是否接收?", "接收文件",
					JOptionPane.YES_NO_OPTION);
			if (i == JOptionPane.YES_OPTION) {
				
				s = ss.accept();
				is = s.getInputStream();
				s.close();
				try {
					File f = new File(Param.CURRENTPATH + "/JQFiles/"
							+ UDP.hostUser.getID());
					if (!f.exists()) // 检查有没有JQFiles目录
						f.mkdirs();

					fileName = f.getAbsolutePath() + "/" + fileName;
					os = new FileOutputStream(fileName);
					f = new File(fileName);
					if (!f.exists()) // 检查文件有无
						try {
							f.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					else { // 如果已存在该文件则覆盖

					}
					// OutputStream out=new FileOutputStream(f);
					// out.write(fileContent.getBytes("UTF-8"));
					// out.close();

					while ((cnt = is.read(buffer)) >= 0) {
						os.write(buffer, 0, cnt);
					}
					os.flush();
					JOptionPane.showConfirmDialog(null,
							"文件传送成功，保存在" + fileName, "文件传送成功",
							JOptionPane.INFORMATION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {// 拒绝接受
				UDP.sendMessage(MsgFactory.REJECTMSG + UDP.hostUser.getID()
						+ Param.SPACE + fileName, fileSendIP, fileSendPort);
			}
			
			os.close();
			is.close();
			ss.close();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
