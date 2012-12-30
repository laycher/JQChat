package com.jq.client.gui.chat;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * 	显示icon面板.
 * 	目的:
 * 		使iconPanel为static,所有聊天窗口共享同一个iconPanel,避免每一个聊天窗口都实
 * 		例化一个iconPanel,大大减少了资源消耗.
 * */
public class IconPanel extends JPanel {

	private static final long serialVersionUID = 2887772324964311057L;

	private Icon[] icons = null;
	private JLabel[] iconLabel = null;
	// 记录上一次的鼠标事件.
	private MouseAdapter lastEvent = null;

	public IconPanel(String iconPath, int ROW, int COM) {
		setBorder(new LineBorder(new Color(153,
				204, 255), 1, true));
		setBackground(new Color(220, 239, 255));
		setBounds(10, 10, 345, 215);

		setLayout(new GridLayout(ROW, COM, 0, 0));
		initIcon(iconPath);
	}

	/** 初始化icon资源 */
	private void initIcon(String iconPath) {
		//如果是表情图标
		if (iconPath.contains("action"))
			icons = actionIcons(iconPath);
		else
			icons = faceIcons(iconPath);

		iconLabel = new JLabel[icons.length];

		int i = 0;
		for (Icon f : icons) {
			// 实现icon载体得label
			iconLabel[i] = new JLabel(f);
			// 将label添加到Panel中
			add(iconLabel[i++]);
		}
	}

	/* 获取表情 */
	private Icon[] actionIcons(String iconPath) {
		Icon[] icons = new Icon[90];

		String k;
		for (int i = 0; i < 90; i++) {
			if (i < 10)
				k = "0" + i + Param.GIF;
			else
				k = i + Param.GIF;

			icons[i] = MyResources.getImageIcon(iconPath + k);
		}

		return icons;
	}

	/* 获取头像 */
	private Icon[] faceIcons(String iconPath) {
		Icon[] icons = new Icon[20];

		for (int i = 0; i < 20; i++)
			icons[i] = MyResources.getImageIcon(iconPath + (i + 1) + Param.GIF);

		return icons;
	}

	/**
	 * 为各icon的载体添加鼠标事件.
	 * 
	 * @param evt
	 *            要监听的鼠标事件
	 * */
	public void setMouseEvent(MouseAdapter evt) {
		// 如果是新窗口的鼠标事件操作,则将其添加到label监听事件队列中.
		if (lastEvent != evt) {
			// 移除上一个鼠标监听事件
			removeMouseEvent(evt);

			lastEvent = evt;
			for (JLabel l : iconLabel)
				if (l != null)
					l.addMouseListener(evt);
		}
	}

	/**
	 * 为各icon的载体移除鼠标事件.
	 * 
	 * @param evt
	 *            要监听的鼠标事件
	 * */
	private void removeMouseEvent(MouseAdapter evt) {
		// 如果此次与上一次的窗口相同则不对鼠标事件进行移除操作.
		if (lastEvent != null)
			for (JLabel l : iconLabel)
				l.removeMouseListener(lastEvent);
	}

}
