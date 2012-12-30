package com.jq.client.gui.chat;

import java.awt.Color;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;

import com.jq.util.MyResources;

public class FontPanel extends JDialog{

	private static final long serialVersionUID = 4732092019204972340L;
	private JComboBox font,color,size;
	private JToggleButton bold,italic,underline;
	private JPanel background;
	private ChatFrame owner = null;
	private String[] fontNames;
	private static final Color[] FONTCOLOR = { Color.BLACK, Color.BLUE,
		Color.RED, Color.PINK, Color.GREEN };
	
	public FontPanel(JFrame parent) {
		super(parent);
		owner = (ChatFrame) parent;
		background = new JPanel();
		background.setLayout(null);
		//setSize(428, 30);
		background.setBackground(new Color(200, 239, 255));
		background.setBorder(new LineBorder(new Color(153, 204, 255),
				1, true));
		this.getRootPane().setWindowDecorationStyle(0);
		this.setLayout(null);
		
		//GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    //fontNames = ge.getAvailableFontFamilyNames();
		fontNames = new String[]{"Arial","Comic Sans MS","Times New Roman","仿宋","宋体","微软雅黑","新宋体","楷体","黑体"};
		font = new JComboBox();
		font.setModel(new DefaultComboBoxModel(fontNames));
		font.setSelectedIndex(5);//默认是微软雅黑
		background.add(font);
		
		font.setBounds(8, 3, 80, 22);
		
		size = new JComboBox();
		size.setModel(new DefaultComboBoxModel(new String[] { "12", "14",
				"16", "18", "20" }));
		background.add(size);
		size.setBounds(99, 3, 55, 22);
		
		color = new JComboBox();
		color.setModel(new DefaultComboBoxModel(new String[] { "黑色", "蓝色",
				"红色", "粉色", "绿色" }));
		background.add(color);
		color.setBounds(165, 3, 65, 22);
		
		bold = new JToggleButton();
		bold.setIcon(MyResources.getImageIcon(MyResources.ICON
				+ "bold.gif"));
		background.add(bold);
		bold.setBounds(235, 1, 26, 26);
		
		italic = new JToggleButton();
		italic.setIcon(MyResources.getImageIcon(MyResources.ICON
				+ "italic.gif")); 
		background.add(italic);
		italic.setBounds(265, 1, 26, 26);
		
		underline = new JToggleButton();
		underline.setIcon(MyResources.getImageIcon(MyResources.ICON
				+ "underline.gif"));
		background.add(underline);
		underline.setBounds(295, 1, 26, 26);
		
		//background.setSize(426, 30);
		background.setBounds(0, 0, 426, 30);
		add(background);
		setSize(426,30);

	}

	/**
	 * 获取当前字体名称.
	 * 
	 * @return String 当前字体名称.
	 * */
	public String getFontName() {
		return font.getSelectedItem().toString();
	}

	/**
	 * 获取第i个字体的名称.
	 * 
	 * @param i
	 *            要查询的字体下标.
	 * @return String 第i个字体的名称.
	 * */
	public String getFontName(int i) {
		return font.getItemAt(i).toString();
	}


	/**
	 * 获取当前字体大小
	 * 
	 * @return int 当前字体的大小.
	 * 
	 * */
	public int getFontSize() {
		int i = Integer.parseInt(size.getSelectedItem().toString().trim());
		return i;
	}


	/**
	 * 获取第i个字体大小
	 * 
	 * @param i
	 *            要查询的字体大小的下标.
	 * @return int 返回的字体大小.
	 * */
	public int getFontSize(int i) {
		return Integer.parseInt(size.getItemAt(i).toString().trim());
	}
	
	/**
	 * 获取当前字体颜色 "黑色", "蓝色", "红色", "粉色", "绿色"
	 * 
	 * @return Color 当前字体颜色.
	 * */
	public Color getFontColor() {
		return FONTCOLOR[color.getSelectedIndex()];
	}

	

	/**
	 * 获取当前字体颜色 "黑色", "蓝色", "红色", "粉色", "绿色"
	 * 
	 * @param i
	 *            要查询的颜色下标.
	 * @return Color 第i个颜色.
	 * */
	public Color getFontColor(int i) {
		return FONTCOLOR[i];
	}

	/**
	 * 获取字体详细样式字符串
	 * 格式为：2123
	 * */
	// String fontName, Color col, int type, int fontSize
	public String getType() {
		return "" + font.getSelectedIndex() + color.getSelectedIndex() + getFontType()
				+ size.getSelectedIndex();
	}

	/**
	 * 获取当前字体样式. 字体样式: 0 为常规. 1 为粗体. 2 为斜体. 3 为下划线. 4 为粗体和下划线 . 5为粗体和斜体. 6
	 * 为斜体和下划线. 7为粗体,斜体和下划线.
	 * 
	 * @return int 字体的样式.
	 * */
	public int getFontType() {
		if (!bold.isSelected() && !italic.isSelected()
				&& !underline.isSelected())
			return 0;
		else if (bold.isSelected() && !italic.isSelected()
				&& !underline.isSelected())
			return 1;
		else if (!bold.isSelected() && italic.isSelected()
				&& !underline.isSelected())
			return 2;
		else if (!bold.isSelected() && !italic.isSelected()
				&& underline.isSelected())
			return 3;
		else if (bold.isSelected() && !italic.isSelected()
				&& underline.isSelected())
			return 4;
		else if (bold.isSelected() && italic.isSelected()
				&& !underline.isSelected())
			return 5;
		else if (!bold.isSelected() && italic.isSelected()
				&& underline.isSelected())
			return 6;
		else if (bold.isSelected() && italic.isSelected()
				&& underline.isSelected())
			return 7;
		return 0;
	}
	
	/**
		 * 关闭字体设置面板事件. 关闭前将所设置的字体设置传给文本框,更改并显示字体.
		 * */
	public void close() {
		this.setVisible(false);
		int fontType = Font.PLAIN;
		if(bold.isSelected()&&italic.isSelected()){
			fontType = Font.BOLD+Font.ITALIC;
		}else if(!bold.isSelected()&&italic.isSelected()){
			fontType = Font.ITALIC;
		}else if(bold.isSelected()&&!italic.isSelected()){
			fontType = Font.BOLD;
		}
		Font f = new Font(getFontName(), fontType, getFontSize());
		owner.sendEditText.setFont(f);
		owner.sendEditText.setForeground(getFontColor());
	}

	public void showFont() {
		this.setLocation(owner.getLocationOnScreen().x+13, owner.getLocationOnScreen().y+302);
		this.setVisible(true);
		
	}

}