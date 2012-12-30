
package com.jq.client.gui.common;

import javax.swing.*;

import com.jq.util.Friend;
import com.jq.util.MyResources;



/**好友上线通告窗口*/
public class NotifyFrame implements Runnable{
	private static final long serialVersionUID = -5114605156121535712L;
	
	private JFrame jf;
	private JLabel background;
	private JLabel face;
	private JLabel name;
	private JLabel signed;
	
    private int	PIX = 2;	//每次上升的高度.
    private int	lastLocation;	//最后的纵坐标
    private boolean running = true;
    private Friend  friend = null;
    
	/** 构造函数，创造一个好友上线通知 */
	public NotifyFrame(Friend friend) {
		this.friend = friend;
		initComponents();
		
		new Thread(this).start();
	}

	/** 初始化界面*/
	private void initComponents() {

		jf = new JFrame();
		face = new JLabel();
		signed = new JLabel();
		name = new JLabel();
		background = new JLabel();

		jf.setLayout(null);
		jf.getRootPane().setWindowDecorationStyle(0);
		jf.setAlwaysOnTop(true);
		jf.setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));

		face.setIcon(friend.getFaceIcon()); 
		signed.setText("好友上线提示!");
		name.setText(friend.getNickName() + "[" + friend.getID() + "]");
		background.setIcon(MyResources.getImageIcon(MyResources.ICON + "notify.png")); 
		
		face.setBounds(10, 38, 46, 46);
		signed.setBounds(60, 60, 90, 20);		
		name.setBounds(60, 40, 130, 20);		
		background.setBounds(0, 0, 232, 103);
		
		jf.add(face);
		jf.add(signed);
		jf.add(name);
		jf.add(background);

		jf.pack();

		jf.setSize(232, 113);
        jf.setLocation(MyResources.getScreenCenterX() * 2  - 250, MyResources.getScreenCenterY() * 2 + 80);
        lastLocation = MyResources.getScreenCenterY() * 2 - 130;	//最后的纵坐标
		jf.setVisible(true);
	}
	
	
	public void run() {
		while(running){
			if (jf.getY() > lastLocation)
				jf.setLocation(jf.getX(),jf.getY() - PIX);
			try {
				Thread.sleep(45);
				if (jf.getY() <= lastLocation){
					Thread.sleep(5000);
					running = false;
					jf.dispose();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}