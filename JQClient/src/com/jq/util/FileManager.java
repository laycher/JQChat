package com.jq.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * [用于文本操作的类.] 
 * 此类执行聊天记录的读写任务. 读取时获取聊天记录的行数,以便分页显示.
 * 写出时,若聊天记录中包含当前日期,则不写入当前日期,否则写入当前日期.
 * */
public class FileManager {
	private BufferedReader inputStream = null;
	private BufferedWriter outputStream = null;
	private StringBuilder sb = new StringBuilder();
	private String fileName;
	private int line = 0;

	private static SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd"); // 格式化日期使用

	/**
	 * 构造函数.
	 * 
	 * @param hostID
	 *            当前客户端用户ID
	 * @param friendID
	 *            聊天好友ID.
	 * */
	public FileManager(String hostID, String friendID) {

		checkExsit(hostID, friendID);

		start();
	}
	
	/** 检查用户聊天记录文件夹路径存在否 */
	private void checkExsit(String hostID, String friendID) {
		
		File f = new File(Param.CURRENTPATH + "/JQchatLog/" + hostID);
		if (!f.exists())	//检查有没有JQchatLog目录
			f.mkdirs();

		fileName = f.getAbsolutePath() + "/" + friendID + ".txt";
		f = new File(fileName);
		if (!f.exists())   // 检查聊天记录文件有无 
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**开始对文件进行读取操作*/
	public void start() {

		try {
			inputStream = new BufferedReader(new FileReader(fileName));

			String l;
			while ((l = inputStream.readLine()) != null) {
				line++;	//行数加1
				sb.append(l + Param.NEWLINE);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取当前日期.格式为 [2011-11-11] 
	 * @return String 格式化后的日期.
	 * */
	private static String getDate() {
		Date date = new Date();
		return "[" + s.format(date) + "]";
	}

	/** 清除记录. */
	public void removeMsg() {
		line = 0;
		sb.delete(0, sb.toString().length());
	}

	/**	保存聊天记录. */
	public void save(String msg) {
		String newMsg = msg;
		// 如果msg不为空，且sb序列中不包含时间，则加入时间
		if (msg.trim().length() != 0 && !sb.toString().contains(getDate()))
			newMsg = getDate() + Param.NEWLINE + "---------------------"
					+ Param.NEWLINE + newMsg;

		sb.append(Param.NEWLINE + newMsg);
		try {
			outputStream = new BufferedWriter(new FileWriter(fileName));
			outputStream.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取记录内容.
	 * @return String 聊天记录String格式.
	 * */
	public String getString() {
		return sb.toString();
	}

	/**
	 * 获取输入流.
	 * @return BufferedRead 连接聊天记录的输入流.
	 * */
	public BufferedReader getBufferedReader() {
		return new BufferedReader(new StringReader(getString()));
	}

	/**
	 * 获取行数.
	 * @return int 聊天记录的总行数.
	 * */
	public int getLines() {
		return line;
	}

}