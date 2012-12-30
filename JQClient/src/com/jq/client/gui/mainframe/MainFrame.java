package com.jq.client.gui.mainframe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jq.client.gui.common.MyTray;
import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.Friend;
import com.jq.util.IPInfo;
import com.jq.util.MyResources;

public class MainFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MyList friendList;
	private UDPServer UDP = null;
	//private SearchDialog searchDialog = null;
	private FriendsManager friendsManager = null;
	private Friend[] allFriends = null;
	private ChangePassword changePassword = null;
	private MessageManager	messageManager = null;
	
    private Rectangle rect;		//用于像QQ一样到屏幕边缘可以自动隐藏
    private Point point;		// 鼠标在窗体的位置
    private Timer timer = new Timer(10, this);;
    
    JFrame jf;
    JPanel main,login;	//login是登录界面，main是主界面  
    JToolBar topBar;
    JButton search,message,changePW,about;
    JLabel avator,name,id,sign;
    String[] status = {"隐身","离开","忙碌","在线"};;
    JComboBox jcb;
    JScrollPane jsp;
    
	public MainFrame(Friend[] friends,String[] leftMsg){
		this.allFriends = friends;
		initComponents(friends);
		/*有离线消息--显示*/
		if (leftMsg != null)
			new ShowLeftInfo(leftMsg);
	}
	
	/**
	 * 	[内部类]
	 * 	离线消息显示处理内部类.
	 * 	和UDP接收信息处理一样.
	 *  @param msg 
	 *  		接收到的离线信息数组.ID+" "+message
	 * */
	private class ShowLeftInfo extends Thread{
		String[] msg = null;
		
		public ShowLeftInfo(String[] leftInfo){
			this.msg = leftInfo;
			start();
		}
		
		public void run(){
			try {	/*等待10秒后显示*/
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			for (String message : msg){
				/* 解码 */
				int i = message.indexOf(" ");
				String id = message.substring(0, i + 1).trim(); 	/* 发送者ID */
				message = message.substring(i + 1).trim(); 		/* 信息体 */
				
				UDP.receiveMsg(id, message);
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**初始化界面*/
	private void initComponents(Friend[] friends) {
		friendsManager = new FriendsManager(friends);		
		UDP = new UDPServer(IPInfo.getClientPort(),friendsManager,friends[0]);//8000
		friendList = new MyList(friendsManager.getDefaultListModel(),UDP);
		
		jf = new JFrame("JQ 2011");
		jf.setVisible(true);	
		jf.setAlwaysOnTop(true);
		//jf.setExtendedState(Frame.ICONIFIED);
		jf.setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		//------------启动后直接最小化-----
		if (MyTray.isSupported()){
			//jf.dispose();
			new MyTray(jf,UDP);
		}else{
			jf.setExtendedState(JFrame.ICONIFIED);
		}
		//登录界面
		login = new JPanel();
		login.setLayout(null);
		//将界面显示在屏幕的右边
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension size = tk.getScreenSize();
		jf.setLocation((size.width-300), 50);
		
		JLabel logo = new JLabel(MyResources.getImageIcon(MyResources.ICON+"logo.png"));
		JLabel tip = new JLabel("正在登录...");
		JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL);	//jpb 进度条
		jpb.setIndeterminate(true);
		//jpb.setValue(100);
		logo.setBounds(64, 165, 100, 100);		//设置 logo，tip，jpb的位置
		tip.setBounds(90, 250, 100, 100);
		jpb.setBounds(40, 350, 160, 20);
		
		login.add(logo);		//jf里面有两个jpanel轮流切换，先是login，再是main。
		login.add(tip);
		login.add(jpb);
		jf.add(login);
		
		
		//主界面
		main = new JPanel();
		main.setLayout(null);
		
		JPanel jp = new JPanel();
		jp.setLayout(null);
		avator = new JLabel(MyResources.getImageIcon(MyResources.USERFACE+"19.gif"));
		avator.setToolTipText("点击更改个人资料");
		avator.addMouseListener(new MouseAdapter(){		//点击头像修改个人资料
			public void mouseClicked(MouseEvent evt) {
				friendList.showInfoFrame(UDP.hostUser);
			}
		});
		name = new JLabel();	//UserInfo.user.name
		id = new JLabel();	//UserInfo.user.id
		sign = new JLabel();	
		jcb = new JComboBox(status);
		
		avator.setBounds(5, 10, 40, 40);	//头像是40x40的
		name.setBounds(50,3,150,20);		//显示昵称
		id.setBounds(50,23, 150, 20);		//显示ID
		sign.setBounds(50, 43, 190, 20);	//显示个性签名
		jcb.setBounds(165,10,80,25);		//显示状态
		
		jp.add(avator);
		jp.add(name);
		jp.add(id);
		jp.add(sign);
		jp.add(jcb);
		jp.setBounds(0, 0, 250, 60);
		
		topBar = new JToolBar();
		search = new JButton(MyResources.getImageIcon(MyResources.ICON+"search.png"));
		search.setToolTipText("搜索添加好友");
		search.addActionListener(this);
		message = new JButton(MyResources.getImageIcon(MyResources.ICON+"message.png"));
		message.setToolTipText("历史消息管理");
		message.addActionListener(this);
		changePW = new JButton(MyResources.getImageIcon(MyResources.ICON+"password.png"));
		changePW.setToolTipText("修改密码");
		changePW.addActionListener(this);
		about = new JButton(MyResources.getImageIcon(MyResources.ICON+"about.png"));
		about.setToolTipText("关于");
		about.addActionListener(this);
		topBar.setFloatable(false);
		topBar.setRollover(true);
		topBar.add(search);
		topBar.add(message);
		topBar.add(changePW);
		topBar.add(about);
		topBar.setBounds(5, 60, 250, 25);
				
		main.add(jp);
		main.add(topBar);
		
		jsp = new JScrollPane();
		jsp.setViewportView(friendList);
		
		JTabbedPane choose = new JTabbedPane();
		choose.setBounds(0, 90, 250, 500);

		choose.add(jsp," 好友 ");
		JPanel group = new JPanel();
		choose.add(group," 群组 ");
		main.add(choose);
		
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
			public void windowIconified(WindowEvent e){//最小化时
				/*从写图标化操作*/		
				jf.dispose();
			}
		});
		
		roll().start();
		timer.start();		//隐藏用的
		jf.setResizable(false);
		jf.setSize(250,630);
		
		setInfo(friends[0]);

	}
	
	private void setInfo(Friend f){
		avator.setIcon(f.getFaceIcon());
		name.setText(f.getNickName() );
		id.setText("[" + f.getID() + "]");
		sign.setText(f.getSignedString());
		jcb.setSelectedIndex(f.getStatus()-1);	//未解决状态问题
	}

	@SuppressWarnings("unchecked")
	public void close() {
		jf.dispose();

		/* 向服务器发送下线通知 */
		Service<String, String> service = (Service<String, String>) ServiceFactory
				.getService(ServiceFactory.TASK_LOGOUT, TCPServer.SERVER_IP, TCPServer.PORT);

		service.service(UDP.hostUser.getID());
		System.out.println("Main发送注销");
		
		//向好友发送下线消息
		UDP.sendLogoutMessage();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		/* 退出 */
		System.exit(1);
		
	}

	public Thread roll()		//经过一定的时间变成主界面
    {
        return new Thread()
        {
			public void run(){
				try {
					Thread.sleep(1500);
				} catch (Exception e) {}
				//login.setVisible(false);
				jf.remove(login);
				jf.add(main);
			}
        };
    }
	
	 /**
	  * 判断一个点是否在一个矩形内
	  *  rect：Rectangle对象
	  *  point ：Point对象
	  * 如果在矩形内返回true，不在或者对象为null则返回false
	  */
    public boolean isPtInRect(Rectangle rect, Point point) {
        if (rect != null && point != null) {
            int x0 = rect.x;
            int y0 = rect.y;
            int x1 = rect.width;
            int y1 = rect.height;
            int x = point.x;
            int y = point.y;

            return x >= x0 && x < x1 && y >= y0 && y < y1;
        }
        return false;
    }

    /**处理动作*/
	@Override
	public void actionPerformed(ActionEvent e) {
		int left = jf.getLocationOnScreen().x;	 // 窗体离屏幕左边的距离
		int top = jf.getLocationOnScreen().y;		//窗体离屏幕顶部的距离
		int width = jf.getWidth();				// 窗体的宽
		int height = jf.getHeight();			 // 窗体的高
		rect = new Rectangle(0, 0, width, height);		  // 获取窗体的轮廓
        point = jf.getMousePosition();			  // 获取鼠标在窗体的位置
        
        Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension size = tk.getScreenSize();
		int right = size.width - left - width;	//窗体离屏幕右边的距离
        if ((top < 0) && isPtInRect(rect, point)) {
        	 // 当鼠标在当前窗体内，并且窗体的Top属性小于0 设置窗体的Top属性为0,就是将窗口上边沿紧靠顶部
        	jf.setLocation(left, 0);
        } else if (top > -3 && top < 3 && !(isPtInRect(rect, point))) {
        	// 当窗体的上边框与屏幕的顶端的距离小于5时 ，并且鼠标不再窗体上 将QQ窗体隐藏到屏幕的顶端
        	jf.setLocation(left, 3 - height);
        } else if ((left <0)&& isPtInRect(rect, point)){	//隐藏在左边
        	jf.setLocation(0, top);
        } else if (left > -3 && left < 3 && !(isPtInRect(rect, point))){
        	jf.setLocation(3 - width, top);
        } else if ((right < 0) && isPtInRect(rect, point)) {	//隐藏在右边
        	jf.setLocation(size.width - width, top);
        } else if (right > -3 && right < 3 && !(isPtInRect(rect, point))) {
        	jf.setLocation(size.width - 3, top);
        }
        
        if(e.getSource()==about  ){
        	 JOptionPane.showMessageDialog(jf, "JAVA QQ 2011\n www.laycher.com","关于",JOptionPane.INFORMATION_MESSAGE);	//关于
		}else if( e.getSource() == search){
			//new SearchDialog();	//添加好友
			//System.out.println("添加好友");
			//if (searchDialog == null)
				//searchDialog = new SearchDialog(jf,UDP,friendList,friendsManager);
			new SearchDialog(jf,UDP,friendList,friendsManager);
		}else if(e.getSource() == message){
			//System.out.println("消息管理");
			if (messageManager == null){
				messageManager = new MessageManager(allFriends,UDP);
			}else {
				messageManager.setVisible(true);
			}
		}else if(e.getSource() == changePW){
			//System.out.println("修改密码");
			if (changePassword == null){
				changePassword = new ChangePassword(UDP.hostUser);
			}else {
				changePassword.clean();
				changePassword.setVisible(true);
			}
		}
		
	}
	
}
