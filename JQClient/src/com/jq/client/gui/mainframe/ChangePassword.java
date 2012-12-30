package com.jq.client.gui.mainframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.Friend;
import com.jq.util.MD5;
import com.jq.util.MyResources;
import com.jq.util.Param;

/**修改密码窗口*/
public class ChangePassword extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 6188437101191317559L;
	JButton ok,cancel;
	JLabel title_Label,userID_Label,pwd1_Label,pwd2_Label;
	JTextField userID;
	JPasswordField pwd1,pwd2;

	public ChangePassword(Friend host){
		setTitle("修改密码");
		setAlwaysOnTop(true);
		setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		setLayout(null);
		
		title_Label = new JLabel("修 改 密 码");
		userID_Label = new JLabel("用户帐号：");
		pwd1_Label = new JLabel("新 密 码 ：");
		pwd2_Label = new JLabel("确认密码：");
		
		userID = new JTextField();
		userID.setText(host.getID());
		userID.setEditable(false);
		pwd1 = new JPasswordField();
		pwd1.setEchoChar('●');
		pwd2 = new JPasswordField();
		pwd2.setEchoChar('●');
		
		ok = new JButton("确认");
		cancel = new JButton("取消");
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		title_Label.setBounds(80, 10, 60, 30);
		userID_Label.setBounds(10, 50, 60, 30);
		userID.setBounds(80, 50, 150, 30);
		pwd1_Label.setBounds(10, 90, 60, 30);
		pwd1.setBounds(80, 90, 150, 30);
		pwd2_Label.setBounds(10, 130, 60, 30);
		pwd2.setBounds(80, 130, 150, 30);
		ok.setBounds(20, 170, 80, 30);
		cancel.setBounds(140, 170, 80, 30);
		
		add(title_Label);
		add(userID_Label);
		add(userID);
		add(pwd1_Label);
		add(pwd1);
		add(pwd2_Label);
		add(pwd2);
		add(ok);
		add(cancel);
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(240,240);
		setLocationRelativeTo(null);	//居中显示
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== ok){
			if(check()){
				TCPServer.owner = this;
				Service<String,String> service = (Service<String, String>) ServiceFactory.getService( 
						ServiceFactory.TASK_PASSWORD, TCPServer.SERVER_IP,TCPServer.PORT);

				String flag = service.service(getInfos());	//此处字符串无用
				if (flag.equals(TCPServer.SUCCESS))
					JOptionPane.showMessageDialog(null, "更改密码成功!",
							"提示", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "更改密码失败!",
							"提示", JOptionPane.ERROR_MESSAGE);
				dispose();
			}else{
				return;
			}
		}else if(e.getSource() == cancel){
			dispose();
		}
		
	}
	
	/**获取更改信息
	 * 格式：ID_PW*/
	private String getInfos() {
		return userID.getText().trim() + Param.SPACE + MD5.code(String.valueOf(pwd1.getPassword()).trim());
	}

	/**密码格式检测*/
	private boolean check(){
		if(pwd1.getPassword().length == 0 || pwd2.getPassword().length == 0 ){
			JOptionPane.showMessageDialog(null, "请填写新密码！", "警告",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (!String.valueOf(pwd1.getPassword()).equals(
				String.valueOf(pwd2.getPassword()))) {
			JOptionPane.showMessageDialog(null, "两次输入的密码不同。\n 请重新输入。", "警告",
					JOptionPane.WARNING_MESSAGE);
			clean();
			return false;
		}
		if (String.valueOf(pwd1.getPassword()).length() < 6) {
			JOptionPane.showMessageDialog(null, "您的密码过短。\n 至少要六位。", "警告",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		return true;
	}


	/** 清除文本 */
	public void clean() {
		pwd1.setText(null);
		pwd2.setText(null);
	}
	
	/*
	public static void main(String[] args) {
		try {
		    UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		    UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("微软雅黑", Font.PLAIN, 12));
		}catch (Exception e) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}catch (Exception e2) {}
			JOptionPane.showMessageDialog(null, "加载皮肤失败，使用默认界面。","警告", JOptionPane.WARNING_MESSAGE);
		}
		new ChangePassword();

	}*/

}
