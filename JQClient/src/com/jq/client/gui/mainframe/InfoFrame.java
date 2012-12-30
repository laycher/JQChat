package com.jq.client.gui.mainframe;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.Friend;
import com.jq.util.FriendInfo;
import com.jq.util.MyResources;

public class InfoFrame extends JFrame {
	private JTextField ID,name, academy, age, provence, city, department, email,
			homepage;
	private JButton updataButton, closeButton;
	private JLabel title, jl1, jl3, jl4, jl5, jl6, jl7, jl8, jl9, jl10,
			jl11, jl12, jl13;
	public  JLabel face;
	private JPanel jp1, jp2;
	private JScrollPane jsp;
	private JComboBox sex;
	private JTextArea sign;

	private static FacePanel facePanel = null;
	private boolean flag = false;
	private Friend friend = null;

	private static final long serialVersionUID = -1862176744255733041L;

	/** 查看好友资料 或者 个人资料修改 
	 * @param parent 父窗口，用于设置位置 
	 * @param friend 要查看的人
	 * @param flag	ture,修改个人资料；false，查看好友资料		*/
	public InfoFrame(Frame parent, Friend friend, boolean flag) {
		this.flag = flag;
		this.friend = friend;

		initComponents();
		initEvent(flag);
		normal(flag);

		setSize(376, 372);
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	private void initComponents() {

		jp1 = new JPanel();
		jp2 = new JPanel();
		title = new JLabel();
		face = new JLabel();
		jl1 = new JLabel();
		jl3 = new JLabel();
		jl4 = new JLabel();
		jl5 = new JLabel();
		jl6 = new JLabel();
		jl7 = new JLabel();
		jl8 = new JLabel();
		jl9 = new JLabel();
		jl10 = new JLabel();
		jl11 = new JLabel();
		jl12 = new JLabel();
		jl13 = new JLabel();
		ID = new JTextField();
		name = new JTextField();
		jsp = new JScrollPane();
		sign = new JTextArea();
		sex = new JComboBox();
		age = new JTextField();
		city = new JTextField();
		provence = new JTextField();
		email = new JTextField();
		homepage = new JTextField();
		academy = new JTextField();
		department = new JTextField();
		updataButton = new JButton();
		closeButton = new JButton();
		
		getContentPane().setLayout(null);
		setAlwaysOnTop(true);

		jp1.setLayout(null);
		jp2.setLayout(null);

		facePanel = new FacePanel(InfoFrame.this);
		facePanel.setVisible(false);
		facePanel.setBounds(78, 94, 267, 195);
		add(facePanel);
		
		title.setIcon(MyResources.getImageIcon(MyResources.ICON
				+ "personmanage.gif")); // NOI18N
		jp2.add(title);
		title.setBounds(20, 5, 220, 30);

		jp1.add(jp2);
		jp2.setBounds(0, 0, 370, 40);

		face.setIcon(MyResources.getImageIcon(MyResources.USERFACE + "7.gif")); // 默认头像
		face.setText("头像");
		face.setToolTipText("点击修改用户头像");
		face.setIcon(friend.getFaceIcon());
		

		jp1.add(face);
		face.setBounds(300, 46, 46, 46);

		jl3.setText("用户帐户: " );
		jp1.add(jl3);
		jl3.setBounds(20, 40, 130, 30);

		jl4.setText("用户昵称:");
		jp1.add(jl4);
		jl4.setBounds(20, 70, 80, 30);

		jl5.setText("个性签名:");
		jp1.add(jl5);
		jl5.setBounds(20, 100, 70, 30);

		sex.setModel(new DefaultComboBoxModel(new String[] { "男", "女" }));
		//sex.setSelectedItem(new String[] { "男", "女" });
		sex.setEditable(false);
		jp1.add(sex);
		sex.setBounds(90, 160, 50, 30);

		jl6.setText("性    别:");
		jp1.add(jl6);
		jl6.setBounds(20, 160, 63, 30);

		jl7.setText("年  龄:");
		jp1.add(jl7);
		jl7.setBounds(200, 160, 50, 30);

		jl8.setText("省    份:");
		jp1.add(jl8);
		jl8.setBounds(20, 190, 70, 30);

		jl9.setText("城  市:");
		jp1.add(jl9);
		jl9.setBounds(200, 190, 49, 30);

		jl10.setText("Email   :");
		jp1.add(jl10);
		jl10.setBounds(20, 220, 70, 30);

		jl11.setText("主    页:");
		jp1.add(jl11);
		jl11.setBounds(20, 280, 70, 30);

		jl12.setText("学  校:");
		jp1.add(jl12);
		jl12.setBounds(200, 220, 49, 30);

		jl13.setText("专    业:");
		jp1.add(jl13);
		jl13.setBounds(20, 250, 70, 30);

		ID.setEditable(false);
		jp1.add(ID);
		ID.setBounds(90, 40, 100, 30);
		ID.setText(friend.getID());
		
		name.setEditable(false);
		jp1.add(name);
		name.setBounds(90, 70, 100, 30);
		name.setText(friend.getNickName());

		sign.setColumns(20);
		sign.setLineWrap(true);
		sign.setRows(2);
		sign.setBorder(null);
		jsp.setViewportView(sign);
		sign.setText(friend.getSignedString());

		jp1.add(jsp);
		jsp.setBounds(90, 100, 260, 50);

		jp1.add(age);
		age.setBounds(250, 160, 70, 30);

		jp1.add(city);
		city.setBounds(250, 190, 100, 30);

		jp1.add(provence);
		provence.setBounds(90, 190, 100, 30);

		jp1.add(email);
		email.setBounds(90, 220, 100, 30);

		jp1.add(homepage);
		homepage.setBounds(250, 220, 100, 30);

		jp1.add(academy);
		academy.setBounds(90, 250, 260, 30);

		jp1.add(department);
		department.setBounds(90, 280, 260, 30);

		updataButton.setText("更新");

		jp1.add(updataButton);
		updataButton.setBounds(180, 310, 57, 30);

		closeButton.setText("关闭");

		jp1.add(closeButton);
		closeButton.setBounds(270, 310, 57, 30);

		jl1.setText("修改头像");

		jp1.add(jl1);
		jl1.setBounds(230, 73, 70, 30);

		getContentPane().add(jp1);
		jp1.setBounds(0, 0, 370, 345);

		setTitle("资料查看");
		setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		pack();
	}

	private void initEvent(boolean flags) {
		updataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!flag)
					getInfo();
				else
					updateInfo();
			}
		});

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});

		jl1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (!facePanel.isVisible()){
					facePanel.showFace();
				}else{
					facePanel.close();
				}
			}

			public void mouseEntered(MouseEvent evt) {
				jl1.setForeground(Color.RED);
			}

			public void mouseExited(MouseEvent evt) {
				jl1.setForeground(Color.BLACK);
			}
		});

		if (flags)
			face.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					facePanel.showFace();
				}
			});
	}


	@SuppressWarnings("unchecked")
	private void getInfo() {
		Service<FriendInfo, String> service = (Service<FriendInfo, String>) ServiceFactory
				.getService(ServiceFactory.TASK_GETINFO,
				 TCPServer.SERVER_IP,
				 TCPServer.PORT );
				//		"127.0.0.1", 6000);
		FriendInfo friendInfo = service.service(friend.getID());//"1000"
		updataInfo(friendInfo);
	}

	@SuppressWarnings("unchecked")
	private void updateInfo() {
		Service<String, FriendInfo> service = (Service<String, FriendInfo>) ServiceFactory
				.getService(ServiceFactory.TASK_UPDATAINFO,
				 TCPServer.SERVER_IP,
				 TCPServer.PORT );
				//		"127.0.0.1", 6000);
		String m = service.service(myInfo());

		if (m.equals(TCPServer.SUCCESS))
			JOptionPane.showMessageDialog(this, "资料更新成功!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "资料更新失败!", "提示",
					JOptionPane.ERROR_MESSAGE);
	}

	private FriendInfo myInfo() {
		String facename = new File(((ImageIcon) getFaceIcon()).getDescription())
				.getName();
		FriendInfo myInfo = new FriendInfo(sex.getSelectedItem().toString(),
				provence.getText().trim(), city.getText().trim(), email
						.getText().trim(), homepage.getText().trim(), age
						.getText().trim(), academy.getText().trim(), department
						.getText().trim());

		myInfo.id = friend.getID();
		myInfo.nickname = name.getText().trim();
		myInfo.signedString = sign.getText().trim();
		System.out.println(sex.getSelectedItem().toString());
		myInfo.face = facename.substring(0, facename.indexOf(".")).trim();

		return myInfo;
	}

	/** 更新好友资料 */
	private void updataInfo(FriendInfo friend) {
		sex.setSelectedItem(friend.sex);
		age.setText("" + friend.age);
		provence.setText(friend.province);
		city.setText(friend.city);
		email.setText(friend.mail);
		homepage.setText(friend.homePage);
		academy.setText(friend.academy);
		department.setText(friend.department);
	}

	private void normal(boolean flag) {
		academy.setEditable(flag);
		age.setEnabled(flag);
		city.setEditable(flag);
		department.setEditable(flag);
		email.setEditable(flag);
		homepage.setEditable(flag);
		name.setEditable(flag);
		provence.setEditable(flag);
		sex.setEnabled(flag);
		sign.setEditable(flag);

		jl1.setVisible(flag);

		if (!flag) {
			setTitle("查看好友资料");
			title.setText("查看好友资料");
		} else {
			setTitle("更改个人资料");
			title.setText("修改个人资料");
		}
	}

	public Icon getFaceIcon() {
		return face.getIcon();
	}

}