package com.jq.util;

import java.awt.Image;
import javax.swing.ImageIcon;

/** 图像资源 */
public class MyResources {
	
	public static final String LOADICON = "resources/skin/loading/";
	public static final String ICON = "resources/skin/icon/";
	public static final String CHAT = "resources/skin/chat/";
	public static final String ACTION = "resources/action/";
	public static final String MAINFRAME = "resources/skin/mainframe/";
	public static final String USERFACE = "resources/userFace/";
	
	/**
	 * 根据指定路径获取Image.使用class避免相对,绝对路径问题.
	 * 	@param path 要获取Image的路径.
	 * */
	public static Image getImage(String path){
		return getImageIcon(path).getImage();
	}
	
	/**
	 * 根据指定路径获取ImageIcon.使用class避免相对,绝对路径问题.
	 * 	@param path 要获取ImageIcon的路径.
	 * */
	public static ImageIcon getImageIcon(String path){
		return new ImageIcon(MyResources.class.getClassLoader().getResource(path));
	}

}
