package com.jq.client.gui.chat;

import java.io.File;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.jq.util.MyResources;

/**
 * 表情面板.
 * */
public class ActionPanel extends JDialog {

	private static final long serialVersionUID = 2476196871746577148L;

	private MouseAdapter mouseEvent = null;
	protected ChatFrame owner = null;
	private JPanel background;
	private static final int COM = 12;
	private static final int ROW = 8;
	/** 全部聊天窗口共享同一个表情面板 */
	private static IconPanel iconPanel = new IconPanel(MyResources.ACTION, ROW,
			COM);

	public ActionPanel(Frame parent) {
		super(parent);
		owner = (ChatFrame)parent;
		background = new JPanel();
		background.setLayout(null);
		
		background.setBackground(new Color(220, 239, 255));
		background.setBorder(new LineBorder(new Color(153, 204, 255), 1, true));
		this.getRootPane().setWindowDecorationStyle(0);
		this.setLayout(null);
		// 实例化当前窗口所拥有的鼠标监听事件.
		mouseEvent = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Icon checkedFace = ((JLabel) e.getSource()).getIcon();

				owner.sendEditText.insertActionFaceIcon(
				// 重新载入表情图片,否则会不显示重复图片(相同对象).
						MyResources.getImageIcon(MyResources.ACTION
								+ new File(((ImageIcon) checkedFace)
										.getDescription()).getName()));
				setVisible(false);
				owner.brow.setSelected(false);
			}
		};
		iconPanel.setMouseEvent(mouseEvent);
		background.add(iconPanel);
		background.setBounds(0, 0, 365, 235);
		add(background);
		this.setSize(365, 235);
	}

	/** 显示表情面板*/
	public void showBrow() {
		if (!this.isVisible()) {
			this.setLocation(owner.getLocationOnScreen().x+13, owner.getLocationOnScreen().y+97);
			setVisible(true);
		}
	}
	
	/** 关闭表情面板 */
	public void close(){
		owner.brow.setSelected(false);
		setVisible(false);
	}

}