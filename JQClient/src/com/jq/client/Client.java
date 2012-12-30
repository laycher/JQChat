package com.jq.client;

import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.jq.client.gui.login.Login;
import com.jq.util.GC;

public class Client {
	public static void main(String args[]) {
		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		    UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		    UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("微软雅黑", Font.PLAIN, 12));
		     
		}catch (Exception e) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}catch (Exception e2) {}
			JOptionPane.showMessageDialog(null, "加载皮肤失败，使用默认界面。","警告", JOptionPane.WARNING_MESSAGE);
		}
		
		//System.setProperty("java.awt.im.style", "on-the-spot");
		
		new Login();
		new GC();
		
	}
}
