package com.jq.client.gui.login;

import java.awt.event.*;
import java.util.regex.Pattern;

import javax.swing.*;

import com.jq.util.IPInfo;
import com.jq.util.MyResources;
import com.jq.util.Param;

public class Setup implements ActionListener {
	private static final long serialVersionUID = 1L;

	JFrame jf;
	JButton ok,cancel;
	static JTextField ip,sPort, cPort;

	public Setup(){
		jf = new JFrame("JQ 高级设置");
		jf.setLayout(null);
		jf.setAlwaysOnTop(true);
		jf.setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		
		JLabel client,clientPort;
		client = new JLabel("客户端设置:");
		clientPort = new JLabel("端口号：");
		
		cPort = new JTextField(String.valueOf(IPInfo.getClientPort()));
		
		JLabel IP,PORT,server;
		IP   = new JLabel("IP地址：");
		PORT = new JLabel("端口号： ");
		server = new JLabel("服务器端设置：");
		
		ip = new JTextField(IPInfo.getIP());
		sPort = new JTextField(String.valueOf(IPInfo.getServerPort()));
		ok = new JButton("确定");
		cancel = new JButton("取消");
		
		client.setBounds(10, 10, 200, 30);
		clientPort.setBounds(20, 50, 120, 30);
		cPort.setBounds(80, 50, 200, 30);
		server.setBounds(10, 90, 200, 30);
		IP.setBounds(20, 130, 120, 30);
		PORT.setBounds(20, 170, 120, 30);
		ip.setBounds(80, 130, 200, 30);
		sPort.setBounds(80, 170, 200, 30);
		ok.setBounds(10, 210, 120, 30);
		cancel.setBounds(160, 210, 120, 30);
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		jf.add(client);
		jf.add(clientPort);
		jf.add(cPort);
		jf.add(server);
		jf.add(IP);
		jf.add(PORT);
		jf.add(ip);
		jf.add(sPort);
		jf.add(ok);
		jf.add(cancel);
		
		jf.setSize(290,270);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cancel){
			jf.setVisible(false);
			//new Login();
		}else if(e.getSource()==ok){
			if(check()){
				IPInfo.setClientPort(Integer.valueOf(cPort.getText().trim()));
				IPInfo.setIP(ip.getText().trim());
				IPInfo.setServerPort(Integer.valueOf(sPort.getText().trim()));
				
				
				//IPInfo.cPort = Integer.valueOf(cPort.getText().trim());
				//IPInfo.IP = ip.getText().trim();
				//IPInfo.sPort = Integer.valueOf(sPort.getText().trim());
				jf.setVisible(false);
			}
			//IPInfo.IP = ip.getText();
			//IPInfo.PORT = Integer.valueOf(port.getText());
			//jf.setVisible(false);
			//new Logon();
		}	
	}

	/**检查IP地址和端口是否正确
	 * @return true 正确， false 错误*/
	private boolean check() {
		
		if (!Pattern.matches(Param.IP_REGEX,ip.getText().trim())){
			JOptionPane.showMessageDialog(null, "服务器IP地址格式错误!\n IP地址必须以纯数字组成！\n 举例：127.0.0.1", "错误",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		try{
			int clientPort = Integer.parseInt(cPort.getText().trim());
		    int serverPort = Integer.parseInt(sPort.getText().trim());
		
			if ((clientPort < 1 || clientPort > 65535) || (serverPort < 1 || serverPort > 65535)){
				JOptionPane.showMessageDialog(null, "无效端口范围(端口范围1~65535)\n 举例：6000", "错误",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "无效端口格式！\n 端口必须为纯数字！\n 举例：6000", "错误",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
}
