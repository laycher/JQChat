package com.jq.util;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/** 图像资源 */
public class MyResources {
	
	public static final String ICON = "resources/icon/";
	public static final String ACTION = "resources/action/";
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
	
	/**
	 * 	获取屏幕中点X坐标
	 * */
	public static int getScreenCenterX(){
        /*获得显示器尺寸*/
		return (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
	}
	
	
	/**
	 * 	获取屏幕中点Y坐标
	 * */
	public static int getScreenCenterY(){
        /*获得显示器尺寸*/
		return (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
	}
}
