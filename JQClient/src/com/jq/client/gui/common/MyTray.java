package com.jq.client.gui.common;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JFrame;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * 	实现系统托盘功能.
 * 	@author 网络
 * */
public class MyTray{
	private Image image = null;
    private PopupMenu 	menu;							// 为这个托盘加一个弹出菜单
    private TrayIcon  icon;
    private static boolean isFrist = true;
	public SystemTray tray;
	
	public MyTray(final JFrame jf, final UDPServer UDP){
		//获取系统托盘
		tray = SystemTray.getSystemTray();

		//托盘的右键菜单
		menu = new PopupMenu();
		//退出菜单sss
		MenuItem exitItem = new MenuItem("退出JQ");
		exitItem.addActionListener(new ActionListener(){
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				//System.exit(0);
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
		});
		
		//显示主窗口菜单
		MenuItem showItem = new MenuItem("显示主界面");
		showItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showFrame(jf);
				tray.remove(icon);
			}
		});
		
		menu.add(showItem);
		menu.add(exitItem);
		
		
		
		//托盘的标题
		String title = "JQ 2011";
		//托盘图标
		image = MyResources.getImage(MyResources.ICON + "tray.gif");		/*装载托盘图象*/
		//实例化TrayIcon对象
		icon = new TrayIcon(image, title, menu);/*为这个托盘加一个提示信息*/
		//托盘添加鼠标事件
		icon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					showFrame(jf);
					//tray.remove(icon);
				}
			}
		});
		
        try{
            tray.add(icon);
            if (isFrist){
            	icon.displayMessage("提示", "JQ客户端" + Param.NEWLINE + 
            							"隐藏至系统托盘!", MessageType.INFO);// 运行程序的时候右下角会提示信息
            	isFrist = false;
            }
        }
        catch (java.awt.AWTException e) {
            System.err.println("无法向这个托盘添加新项： " + e);
        }
	}
	

	/**显示窗体*/
	private void showFrame(JFrame frame){
		frame.setVisible(true);		/*显示*/
		frame.setExtendedState(JFrame.NORMAL);	/*设置状态为正常显示*/
	}
	
	/**
	 * 	系统是否支持系统托盘功能.
	 * 	@return boolean 支持-true.不支持-false.
	 * */
	public static boolean isSupported(){
		return SystemTray.isSupported();
	}
	
	
}
