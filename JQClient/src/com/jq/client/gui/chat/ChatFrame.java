package com.jq.client.gui.chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import com.jq.client.gui.chat.ChatFrame;
import com.jq.client.gui.common.MsgManagerPanel;
import com.jq.client.gui.mainframe.InfoFrame;
import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.MsgFactory;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.FileManager;
import com.jq.util.Friend;
import com.jq.util.IPInfo;
import com.jq.util.MyResources;
import com.jq.util.Param;
import com.jq.util.ScreenCapture;

/**
 * 	ChatFrame
 * 	聊天窗口界面.
 * 	此类实现与好友的聊天界面.界面有多种功能.
 * 	功能:
 * 			1.字体设置
 * 			2.发送表情
 * 			3.发送图片
 * 			4.发送文件
 * 			5.截屏
 * 			6.查看与好友的聊天记录
 * 			7.查看好友资料
 * 
 * 	使用UDP协议接收发送表情,文字信息.
 * 	使用TCP/IP协议实现图像,文件的发送和接收.
 * 	
 * */
public class ChatFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -7267725017357797698L;
	
	private boolean extendFlag = false;	//扩展面板是否启动
	private FileManager	fileManager = null;	//聊天记录文件加载器
	private UDPServer UDP = null;
	private Friend myFriend = null;
	
	private MsgManagerPanel extendPanel;
	private ActionPanel actionPanel = null;;
	private FontPanel fontPanel = null;

	private static final SimpleDateFormat s = new SimpleDateFormat(
			"HH:mm:ss"); // 格式化日期使用，8个字符
	private InfoFrame infoFrame = null;

	JPanel jp1, jp3; // jp1显示对方消息，jp3显示按钮之类的
	JEditPane receiveEditText,sendEditText;	//receiveEditText是聊天窗口，sendEditText是发消息窗口
	JScrollPane jsp2,jsp1;		//添加滚动条
	JToolBar jtb;		//jtb工具条
	JToggleButton font,brow,chatHistory;
	JButton file,catchScreen;
	JButton jb1,jb2,jb3;	//jb1传输文件，jb2取消，jb3发送
	JLabel avator,name,status,sign;
	
	public ChatFrame (UDPServer UDP,Friend friend){
		this.UDP = UDP;
		this.myFriend = friend;
		
		setTitle("与"+myFriend.getNickName()+"聊天中");	
		setIconImage(MyResources.getImage(MyResources.ICON + "chatIcon.png"));
		setLayout(null);
		jp1 = new JPanel();
		jp3 = new JPanel();
		
		avator = new JLabel(myFriend.getFaceIcon());
		name = new JLabel(myFriend.getNickName()+"\t"+"["+myFriend.getID()+"]");	
		//JLabel id = new JLabel(" (100001) ");	//
		status = new JLabel("["+myFriend.getStatusInfo()+"]");	//状态
		sign = new JLabel(myFriend.getSignedString());
		
		name.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt) {
				if (infoFrame == null)
					infoFrame = new InfoFrame(ChatFrame.this,myFriend,false);
				
				infoFrame.setLocationRelativeTo(ChatFrame.this);
				infoFrame.setVisible(true);
			}

			public void mouseEntered(MouseEvent evt) {
				name.setForeground(Color.RED);
			}

			public void mouseExited(MouseEvent evt) {
				name.setForeground(Color.BLACK);
			}
		});
		avator.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt) {
				if (infoFrame == null)
					infoFrame = new InfoFrame(ChatFrame.this,myFriend,false);
				
				infoFrame.setLocation(ChatFrame.this.getX() + 10, ChatFrame.this.getY() + 50);
				infoFrame.setVisible(true);
			}

			public void mouseEntered(MouseEvent evt) {
				name.setForeground(Color.RED);
			}

			public void mouseExited(MouseEvent evt) {
				name.setForeground(Color.BLACK);
			}
		});
		
		avator.setBounds(0, 5, 40, 40);	//头像是40x40的
		name.setBounds(50,5,180,20);		//显示昵称
		//id.setBounds(140,5, 80, 20);		//显示ID
		status.setBounds(230,5,80,20);		//显示状态
		sign.setBounds(50, 25, 300, 20);	//显示个性签名
		jp1.setLayout(null);
		jp1.add(avator);
		jp1.add(name);
		//jp1.add(id);
		jp1.add(status);
		jp1.add(sign);
		jp1.setBounds(10, 2, 430, 45);
		add(jp1);
		
		actionPanel = new ActionPanel(this);
		//actionPanel.setBounds(12, 74, 365, 235);
		actionPanel.setVisible(false);
		//add(actionPanel);
		
		fontPanel = new FontPanel(ChatFrame.this);
		//fontPanel.setBounds(12, 282, 426, 30);
		fontPanel.setVisible(false);
		//add(fontPanel);
		
		receiveEditText = new JEditPane(false);
		receiveEditText.setEditable(false);
		jsp1 = new JScrollPane(receiveEditText);
		jsp1.setBounds(10, 50, 430, 260);
		add(jsp1);
		 
		
		
		jtb = new JToolBar();
		font = new JToggleButton(MyResources.getImageIcon(MyResources.ICON + "font.png"));
		font.setToolTipText("字体");
		font.addActionListener(this);
		brow = new JToggleButton(MyResources.getImageIcon(MyResources.ICON + "brow.png"));
		brow.setToolTipText("表情");
		brow.addActionListener(this);
		file = new JButton(MyResources.getImageIcon(MyResources.ICON + "file.png"));
		file.setToolTipText("传送文件");
		file.addActionListener(this);	
		catchScreen = new JButton(MyResources.getImageIcon(MyResources.ICON + "catchScreen.png"));
		catchScreen.setToolTipText("截屏");
		catchScreen.addActionListener(this);
		chatHistory = new JToggleButton(MyResources.getImageIcon(MyResources.ICON + "chat.png"));
		chatHistory.setToolTipText("聊天记录");
		chatHistory.addActionListener(this);
		
		jtb.setFloatable(false);
		jtb.setRollover(true);
		jtb.add(font);
		jtb.add(brow);
		jtb.add(file);
		jtb.add(catchScreen);
		jtb.add(chatHistory);
		jtb.setBounds(10, 312, 430, 30);
		add(jtb);
		
		sendEditText = new JEditPane(true);
		sendEditText.setEditable(true);
		sendEditText.requestFocus();
		//sendEditText.setLineWrap(true);		//自动换行
		//jsp2 = new JScrollPane(sendEditText,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp2 = new JScrollPane(sendEditText);
		jsp2.setBounds(10, 344, 430, 90);
		add(jsp2);	
		
		jb1 = new JButton("传输文件");
		jb2 = new JButton("关闭");
		jb3 = new JButton("发送");
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		jb1.setBounds(0, 00, 140, 30);
		jb2.setBounds(230, 00, 100, 30);
		jb3.setBounds(330, 00, 100, 30);
		jp3.setLayout(null);
		jp3.add(jb1);
		jp3.add(jb2);
		jp3.add(jb3);
		jp3.setBounds(10, 436, 430, 30);
		add(jp3);
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//JFrame.HIDE_ON_CLOSE
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				close();
		}});
		setResizable(false);
		this.getRootPane().setDefaultButton(jb3);//默认回车是发送按钮
		setSize(450,500);
		setVisible(true);
		setLocationRelativeTo(null);
		//pack();
	}

	protected void close() {
		/*保存聊天内容*/
    	if (receiveEditText.getText().trim().length() != 0){
    		if (fileManager == null)
    			fileManager = new FileManager(UDP.hostUser.getID(),myFriend.getID());
    		
    		receiveEditText.setText("");
    		fileManager.save(receiveEditText.sb.toString());
    	}
    	
    	UDP.removeChatFrame(myFriend.getID(),this);
		dispose();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==brow){
			
			if (!actionPanel.isVisible()){
				actionPanel.showBrow();
			}else{
				actionPanel.close();
			}
		}else if(e.getSource()==font){
			if (!fontPanel.isVisible()){
				fontPanel.showFont();
			}else{
				fontPanel.close();
			}
       	   //JOptionPane.showMessageDialog(ChatFrame.this, "设置字体","字体",JOptionPane.INFORMATION_MESSAGE);	//字体
		}else if(e.getSource()==file){//发送文件
			sendFile();
	
		}else if(e.getSource()==catchScreen){//屏幕截屏，只能保存，没法发送
			try {
				ScreenCapture.getInstance().getImage();
				//sendEditText.insertImage(ScreenCapture.getInstance().getPickedIcon());
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}else if(e.getSource()==chatHistory){//聊天记录
			if(!extendFlag){
				if (extendPanel == null){
					//初始化并显示聊天记录面板
					if (fileManager == null)
						fileManager = new FileManager(UDP.hostUser.getID(),myFriend.getID());
					extendPanel = new MsgManagerPanel(ChatFrame.this,fileManager);
					
					//extendPanel.setBackground(new java.awt.Color(220, 239, 255));
					extendPanel.setLayout(null);
					add(extendPanel);
					extendPanel.setBounds(438, 5, 400, 465);
					
					//必须重新绘制所有组件,否则无显示内容
					paintAll(getGraphics());
				}
				
				if (fileManager == null){
					fileManager = new FileManager("1000","1001");
					extendPanel.setMsgPages(fileManager);
				}
				
				extendStatus();
			}
			else
				normalStatus();
			
		}else if(e.getSource()==jb1){
			sendFile();
		}else if(e.getSource()==jb2){	//返回
			close();
		}else if(e.getSource()==jb3){	//发送消息
			sendMsg();
		}
	}

	/**发送消息*/
	private void sendMsg() {
		/**
		 * 	消息格式-------->[ID_信息体]
		 * 	信息体---------->[11-25 00:00:00:1223xxxxxxxxxxxxxxxxxxxxxxxx]
		 * 						日期        字体格式        接受信息
		 * 
		 * 1.清空发送窗口的文本
		 * 2.在接受框中显示自己发送的文本
		 * 3.再把消息发送给好友
		 * */
		String content = sendEditText.getDetailText().trim();
		System.out.println(content+"\n"+content.length());
		if (content.length() != 0){
			sendEditText.setText(null);
			sendEditText.requestFocusInWindow();
			//发送的用户信息，格式：ID 时间+字体格式
			String msg = UDP.hostUser.getID() + " " + getDate()
					+ fontPanel.getType();

			// 将自己发送的信息，显示在自己的聊天窗口中
			// 不加字体格式。
			receiveEditText.insertUserString(UDP.hostUser.getNickName() + " "
					+ getDate(), new Color(0, 127, 64));
			receiveEditText.setFontStyle(fontPanel.getFontName(),
					fontPanel.getFontColor(), fontPanel.getFontType(),
					fontPanel.getFontSize());
			receiveEditText.insertToTextPanel(content);

			// 将信息发送给好友
			if (myFriend.getStatus() != 0) // 好友在线
				UDP.sendMessage(MsgFactory.NOMALMSG + msg + content,
						myFriend.getIP(), myFriend.getPort());
			else { // 好友不在线
				@SuppressWarnings("unchecked")
				Service<String, String> service = (Service<String, String>) ServiceFactory
						.getService(ServiceFactory.TASK_LEFTINFO,
								TCPServer.SERVER_IP, TCPServer.PORT);
				service.service(myFriend.getID() + Param.SPACE + msg
						+ content);
			}
		}else{
			JOptionPane.showMessageDialog(ChatFrame.this, "不能发送空消息", "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**发送文件*/
	private void sendFile() {
		String receiveID = myFriend.getID();
		if(UDP.hostUser.getID() == receiveID){
			JOptionPane.showMessageDialog(this, "不能自己给自己发送文件！","错误" , JOptionPane.WARNING_MESSAGE);
		}else{
			JFileChooser jfc = new JFileChooser();
			if(jfc.showOpenDialog(this)== JFileChooser.APPROVE_OPTION){
				File file = jfc.getSelectedFile();
				
				UDP.sendMessage(
						MsgFactory.SENDFILEMSG + UDP.hostUser.getID()
								+ Param.SPACE + file.getName()
								+ Param.SPACE + UDP.hostUser.getIP()
								+ Param.SPACE + UDP.hostUser.getPort(),
						myFriend.getIP(), myFriend.getPort());
				try {
					//Socket s = new Socket(TCPServer.SERVER_IP,IPInfo.getFilePort());
					Socket s = new Socket(myFriend.getIP(),IPInfo.getFilePort());
					FileInputStream fis = new FileInputStream(file);
					byte[] buffer  = new byte[Param.DATA_SIZE];
					int cnt = 0;
					OutputStream os = s.getOutputStream();
					while((cnt=fis.read(buffer))>=0) {
					   os.write(buffer,0,cnt);
					}
					os.close();
					fis.close();
					s.close();
				} catch( Exception e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(this, "7000端口被占用！", "错误", JOptionPane.ERROR_MESSAGE);
				}
				//UDP.sendFile(MsgFactory.SENDFILEMSG + UDP.hostUser.getID(),file,
					//	myFriend.getIP(), myFriend.getPort());
			}
		}
		
	}

	/**
     * 	获取当前日期.格式为 11-26 12:05:09
     * 	@return String 格式化后的日期.
     * */
    public static String getDate(){
    	Date date = new Date();
		return s.format(date);
    }

	/**
	 * 	扩展状态
	 * */
	public void extendStatus(){
		setSize(850, 500);
		extendFlag = true;
	}
	
	/**
	 * 	正常状态.
	 * */
	public void normalStatus(){
		setSize(450, 500);
		extendFlag = false;
	}
	
	/**
	 * 	消息格式--
	 * 		[ID_信息体]
	 * 	信息体--
	 * 		[11-27 00:00:00 xxxxxxxxxxxxxxxxxxxxxxxx]
	 * 						日期        字体格式        接受信息
	 * */
	public void setReceiveMsg(String receiveMsg){
		// 获取接受日期
		String date = receiveMsg.substring(0, 8);
		// 获取字体格式
		String fontType = receiveMsg.substring(8, 12);
		// 接受信息内容
		receiveMsg = receiveMsg.substring(12);

		receiveEditText.insertUserString(myFriend.getNickName() +" "+ date,
				new Color(0,0,255));
		// 设置字体
		receiveEditText
				.setFontStyle(fontPanel.getFontName(Integer.parseInt(fontType
						.substring(0, 1))), // 字体名称
						fontPanel.getFontColor(Integer.parseInt(fontType
								.substring(1, 2))),// 字体颜色
						Integer.parseInt(fontType.substring(2, 3)), // 字体格式
						fontPanel.getFontSize(Integer.parseInt(fontType
								.substring(3, 4)))); // 字体大小
		// 显示信息.
		receiveEditText.insertToTextPanel(receiveMsg);

	}
}
