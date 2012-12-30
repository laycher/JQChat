package com.jq.client.gui.chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * TextPanel 自定义TextPanel, 可以自行设置文本显示格式,样式,大小. 也可以同时显示自定义文本样式和图片.
 * 
 */
public class JEditPane extends JTextPane {
	private static final long serialVersionUID = 1L;

	public StringBuilder sb = new StringBuilder(); // 用于记录接收文本框的内容,聊天记录使用.
	private MutableAttributeSet attrSet = null; // 属性集。字符串的格式,如字体,大小,字体格式等...
	private StyledDocument doc = null; // 重要!Document亦可...若使用Document,在
	// 无文本的时候插入图片时会抛出空指针异常
	// 而StyledDocument不会
	private String fontName = Param.FONT; // 当前字体名称,默认为"微软雅黑"
	private Color fontColor = Color.BLACK; // 当前字体颜色,默认为BLACK
	private int fontType = 0; // 当前字体样式,默认为0，粗体，斜体，下划线
	private int fontSize = 12; // 当前字体大小,默认为12
	private MyPopupMenu menu = null;

	private static final Pattern pattern = Pattern.compile(Param.GIF_REGEX,
			Pattern.MULTILINE | Pattern.DOTALL); // Pattern.DOTALL使.匹配所有字符
	private static Matcher matcher = null;

	public JEditPane(boolean hasPopupMenu) {
		doc = this.getStyledDocument(); // 获得JTextPane的Document
		attrSet = new SimpleAttributeSet(); // 设置字体样式
		setFontStyle(fontName, fontColor, fontType, fontSize); // 设置默认样式

		if (hasPopupMenu) {
			menu = new MyPopupMenu(this);
			this.setComponentPopupMenu(menu.getPopupMenu());
		}
	}

	/**
	 * 使用AttributeSet格式在尾部追加字符串.
	 * 
	 * @param str
	 *            追加的字符串.
	 */
	private void insertString(String str) {
		try {
			doc.insertString(doc.getLength(), str, attrSet);
			this.setCaretPosition(doc.getLength()); // 插入文本后自动滚动屏幕到最新的一行
		} catch (BadLocationException e) {
			System.out.println("JEditPane.insertString()");
			e.printStackTrace();
		}
	}

	/**
	 * 设置发送者或发送者的信息:用户A 00:00:00
	 * 
	 * @param sender
	 *            发送者的名称 和发送时间，例如：用户A 00:00:00
	 * @param col
	 *            发送者使用的字体颜色.
	 * */
	public void insertUserString(String sender, Color col) {
		setFontStyle(Param.FONT, col, 0, 12);//字体样式默认为0，即常规。字体大小为12
		insertString(sender + Param.NEWLINE);
		sb.append(sender.trim() + Param.NEWLINE);
	}

	/**
	 * 此项目专用,用于插入表情
	 * 
	 * @param icon
	 *            要插入的表情.
	 * */
	public void insertActionFaceIcon(Icon icon) {
		setCaretPosition(doc.getLength()); // 设置插入前光标位置
		insertIcon(icon); // 插入图片

		setCaretPosition(doc.getLength()); // 设置插入后光标位置
	}

	/**
	 * 插入图片,插入图片后换行.图片格式为jpg,gif,png......
	 * 
	 * @param image
	 *            为插入的图片.
	 */
	public void insertImage(Icon image) {
		setCaretPosition(doc.getLength()); // 设置插入前光标位置
		insertIcon(image); // 插入图片
		insertString(Param.NEWLINE); // 插入换行

		setCaretPosition(doc.getLength()); // 设置插入后光标位置
	}

	/** 获取JEditPane的内容,表情更改为#12.gif的格式的内容 */
	private List<Element> getAllElements() {
		Element[] roots = this.getStyledDocument().getRootElements();
		return getAllElements(roots);
	}

	private List<Element> getAllElements(Element[] roots) {
		List<Element> icons = new LinkedList<Element>();
		for (int i = 0; i < roots.length; i++) {
			if (roots[i] != null){
				icons.add(roots[i]);
				for (int j = 0; j < roots[i].getElementCount(); j++) {
					Element element = roots[i].getElement(j);
					icons.addAll(getAllElements(new Element[] { element }));
				}
			}
		}
		return icons;
	}

	/**
	 * 取得输入框(JTextPane) 中的详细内容. 包括文本和图片,图片转换为 #01.gif 这样的格式
	 * */
	public String getDetailText() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String text = this.getText();
		List<Element> els = getAllElements();
		for (Element el : els) {
			Icon icon = StyleConstants.getIcon(el.getAttributes());
			if (icon != null) {
				String temp = new File(((ImageIcon) icon).getDescription())
						.getName();
				map.put(el.getStartOffset(), temp);
			}
		}
		StringBuffer sb = new StringBuffer("");
		char[] ch = text.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			String s = map.get(new Integer(i));
			if (s == null)
				sb.append(ch[i]);
			else
				sb.append(s);
		}

		return sb.toString().trim();
	}

	/**
	 * 将接收到的信息(包含文本图片)解码表情后插入到文本框****************************
	 * */
	public void insertToTextPanel(String receiveMessage) {
		receiveMessage = receiveMessage.trim();
		// 记录接收到的文本信息,用于聊天记录存储.
		sb.append(receiveMessage + Param.NEWLINE);

		// 查看信息是否包含表情.
		matcher = pattern.matcher(receiveMessage);
		if (matcher.matches()) { // 包含表情
			String iconName = ""; // 解码
			int index = 0;
			for (int i = 0; i < receiveMessage.length();) {
				index = receiveMessage.indexOf(Param.GIF, i) - 2;

				// 判断剩下的字符串是否包含表情,如果没有则将剩余的(如果有)字符串添加
				if (index < 0) {
					insertString(receiveMessage.substring(i,
							receiveMessage.length()));
					break;
				}

				// 插入文字.
				if (i != index) {
					insertString(receiveMessage.substring(i, index));
					// 将i移至下一个表情
					i = index;
				}

				// 将下标后移6位(00.gif)
				i = i + 6;
				// 获取表情名称
				iconName = receiveMessage.substring(index, i);
				insertActionFaceIcon(MyResources
						.getImageIcon(MyResources.ACTION + iconName)); // 插入表情
			}
			insertString(Param.NEWLINE);
		} else
			// 不包含表情
			insertString(receiveMessage + Param.NEWLINE);
	}

	/**
	 * 设置插入文本的格式和样式.
	 * 就是设置属性集，即attrset
	 * @param fontName
	 *            字体属性.
	 * @param col
	 *            字体颜色.
	 * @param type
	 *            字体样式: 0 为常规. 1 为粗体. 2 为斜体. 3 为下划线. 4 为粗体和下划线 . 5为粗体和斜体. 6
	 *            为斜体和下划线. 7为粗体,斜体和下划线.
	 * @param fontSize
	 *            字体大小.
	 */
	public void setFontStyle(String fontName, Color col, int type, int fontSize) {
		/*
		 * javax.swing.text.StyleConstants : 一个已知的 或常见的属性键和方法的集合，可通过应用
		 * AttributeSet 或 MutableAttributeSet方法以类型安全的方式获取/设置属性。
		 */
		this.fontName = fontName;
		this.fontColor = col;
		this.fontType = type;
		this.fontSize = fontSize;

		try {
			StyleConstants.setFontFamily(attrSet, fontName); // 设置字体属性
		} catch (Exception e) {
			StyleConstants.setFontFamily(attrSet, Param.FONT); // 当无此字体时
		}
		StyleConstants.setForeground(attrSet, col); // 设置字体颜色
		StyleConstants.setFontSize(attrSet, fontSize); // 设置字体大小

		switch (type) { // 设置字体样式
		case 0: // 常规
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setUnderline(attrSet, false);
			break;
		case 1: // 粗体
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setUnderline(attrSet, false);
			break;
		case 2: // 斜体
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, true);
			StyleConstants.setUnderline(attrSet, false);
			break;
		case 3: // 下划线
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setUnderline(attrSet, true);
			break;
		case 4: // 粗体和下划线
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setUnderline(attrSet, true);
			break;
		case 5: // 粗体和斜体
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, true);
			StyleConstants.setUnderline(attrSet, false);
			break;
		case 6: // 斜体和下划线
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, true);
			StyleConstants.setUnderline(attrSet, true);
			break;
		case 7: // 粗体,斜体和下划线
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, true);
			StyleConstants.setUnderline(attrSet, true);
			break;
		}
	}

	/**
	 * 获得当前字体名称
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * 获得当前字体颜色
	 */
	public Color getFontColor() {
		return fontColor;
	}

	/**
	 * 获得当前字体样式
	 */
	public int getFontType() {
		return fontType;
	}

	/**
	 * 获得当前字体大小
	 */
	public int getFontSize() {
		return fontSize;
	}

	/*** 
	 * 内置弹出菜单类
	 * 剪切、复制、粘贴、全选、清屏
	 */
	protected class MyPopupMenu {
		private JMenuItem cut, copy, paste, selectAll, clean;
		private JPopupMenu menu;
		private JEditPane textPane;

		public MyPopupMenu(JEditPane text) {
			cut = new JMenuItem();
			copy = new JMenuItem();
			paste = new JMenuItem();
			selectAll = new JMenuItem();
			clean = new JMenuItem();
			menu = new JPopupMenu();
			textPane = text;

			setPopupMenu();
		}

		/** 设置弹出菜单 */
		private void setPopupMenu() {
			cut.setText("剪切");
			cut.setAccelerator(KeyStroke
					.getKeyStroke('X', InputEvent.CTRL_MASK));
			cut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textPane.cut();
				}
			});
			menu.add(cut);
			
			copy.setText("复制");
			copy.setAccelerator(KeyStroke.getKeyStroke('C',
					InputEvent.CTRL_MASK));
			copy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textPane.copy();
				}
			});
			menu.add(copy);

			paste.setText("粘贴");
			paste.setAccelerator(KeyStroke.getKeyStroke('V',
					InputEvent.CTRL_MASK));
			paste.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textPane.paste();
				}
			});
			menu.add(paste);
			
			menu.add(new JSeparator());

			selectAll.setText("全选");
			selectAll.setAccelerator(KeyStroke.getKeyStroke('A',
					InputEvent.CTRL_MASK));
			selectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textPane.selectAll();
				}
			});
			menu.add(selectAll);

			clean.setText("清屏");
			clean.setAccelerator(KeyStroke.getKeyStroke('D',
					InputEvent.CTRL_MASK));
			clean.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textPane.setText("");
				}
			});
			menu.add(clean);
		}

		public JPopupMenu getPopupMenu() {
			return menu;
		}
	}
}
