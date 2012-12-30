package com.jq.client.gui.login;


import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import com.jq.client.protocol.tcp.AbstractService;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.MD5;
import com.jq.util.MyResources;
import com.jq.util.Param;

import java.util.regex.*;

/**
 * 新用户注册窗口
 * 
 * 运行模式:
 * 		点击后请求服务器获取新ID号码.
 * 		服务器返回新ID号码
 * 		用户填写注册信息.
 * 		成功后向服务器发送基本资料,服务器回传成功/失败标示.
 * 		若用户中途取消注册则向服务器发送取消标示.
 * */

public class Register extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private AbstractService service = null;
	
	JTextField jtf_id, jtf_name, jtf_email;
	JTextArea jta_comment;
	JPasswordField password1, password2;
	JButton register, reset, back;

	public Register(final AbstractService service) {
		this.service = service;
		setTitle("注册");
		setAlwaysOnTop(true);
		setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		setLayout(null);
		
		int width = 220;
		int height = 30;

		//布局是30个像素一行，三列是10 、 90、315开始
		String label1[] = { "注册提示：请按要求仔细填写！", "账　　号：", "昵　　称：", "邮　　箱：",
				"密　　码：", "重复密码：","个人说明：" };
		JLabel tag[] = new JLabel[label1.length];		//将JLabel通过setBounds放在容器中
		for (int i = 0; i < label1.length; i++) {
			tag[i] = new JLabel(label1[i]);
			tag[i].setBounds(10, 5 + i * 30, width, height);
			add(tag[i]);
		}
		
		String label2[] = { "账号必须唯一", "昵称可任意填写", "找回密码的凭据", "密码需6位以上", "请再次输入密码" };
		JLabel explain[] = new JLabel[label2.length];		//将jlabel通过setBounds放在容器中
		for (int i = 0; i < label2.length; i++) {
			explain[i] = new JLabel(label2[i]);
			explain[i].setBounds(315, 35 + i * 30, width, height);
			add(explain[i]);
		}

		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jtf_name = new JTextField();
		jtf_email = new JTextField();
		jtf_id.setBounds(80, 35, width, height);
		jtf_name.setBounds(80, 65, width, height);
		jtf_email.setBounds(80, 95, width, height);

		password1 = new JPasswordField();
		password2 = new JPasswordField();
		password1.setEchoChar('●');
		password2.setEchoChar('●');
		password1.setBounds(80, 125, width, height);
		password2.setBounds(80, 155, width, height);
		
		jta_comment = new JTextArea();
		jta_comment.setBounds(80, 185, width+90, height*2);
		
		add(jtf_id);
		add(jtf_name);
		add(jtf_email);
		add(password1);
		add(password2);
		add(jta_comment);
		
		register = new JButton("注册");
		reset = new JButton("重填");
		back = new JButton("返回");
		register.setBounds( 40, 250, 100, 30);
		reset.setBounds(160, 250, 100, 30);
		back.setBounds(280, 250, 100, 30);
		
		add(register);
		add(reset);
		add(back);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				//取消注册
				try {
					service.getObjectOutputStream().writeObject(TCPServer.ERROR);
				} catch (IOException ep) {
					ep.printStackTrace();
				}
			}});
		register.addActionListener(this);
		reset.addActionListener(this);
		back.addActionListener(this);
		
		setSize(420, 320);
		setLocationRelativeTo(null);		//居中显示
		//setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	/*public static void main(String args[]){
		JFrame.setDefaultLookAndFeelDecorated( true ); 
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("微软雅黑", Font.PLAIN, 12));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		new Register();
	}*/
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back) {
			//取消注册
			try {
				service.getObjectOutputStream().writeObject(TCPServer.ERROR);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			setVisible(false);
		} else if (e.getSource() == register) {
			if(!check())
				return;
			
			try {
				service.getObjectOutputStream().writeObject(getInfos());
				String flag = (String)service.getObjectInputStream().readObject();
				
				if(flag.equals(TCPServer.SUCCESS))
					JOptionPane.showMessageDialog(null, "["+ jtf_id.getText() + "]注册成功!",
																					"提示", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "["+ jtf_id.getText() + "]注册失败!",
							"提示", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}else if(e.getSource() == reset ){
			
			jtf_name.setText(null);
			jtf_email.setText(null);
			password1.setText(null);
			password2.setText(null);
			jta_comment.setText(null);
		}
	}
	
	/** 设置ID */
	public void setID(String id){
		jtf_id.setText(id);
	}
	
	/** 获取信息： 
	 * ID_Password_nickname_Email */
	public String getInfos(){
		String ID = jtf_id.getText().trim();
		String PW = MD5.code(String.valueOf(password1.getPassword()));
		String nickName = jtf_name.getText().trim();
		String email = jtf_email.getText().trim();
		return ID + Param.SPACE + PW + Param.SPACE + nickName + Param.SPACE
				+ email;
	}

	private boolean check() {
		if (jtf_id.getText().isEmpty() || jtf_email.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "请填写必填信息。", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (!Pattern.matches("^([a-z0-9A-Z]+[_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", jtf_email.getText())) {
			//6~18个字符，包括字母、数字、下划线，以字母开头，字母或数字结尾
			JOptionPane.showMessageDialog(this, "邮箱填写不正确。\n 格式不正确！", "警告",
					JOptionPane.WARNING_MESSAGE);	
			return false;
		}
		if (!String.valueOf(password1.getPassword()).equals(
				String.valueOf(password2.getPassword()))) {
			JOptionPane.showMessageDialog(this, "两次输入的密码不同。", "警告",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (String.valueOf(password1.getPassword()).length() < 6) {
			JOptionPane.showMessageDialog(this, "您的密码过短。\n 至少要六位。", "警告",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

}
