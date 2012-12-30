package com.jq.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/** [MsgPages] 
 * 聊天记录翻页管理类.
 * 用于聊天记录的管理.*/
public class MsgPages {
	
	/** 分页数 */
	private int 				pageNum = 0;
	/** 现在的页数 */
	private int 				currentPage = 0;
	private StringBuilder[] 	pages = null;
	private HashMap<String, StringBuilder> pageMap = new HashMap<String, StringBuilder>();
	private ArrayList<String> 	dateList = new ArrayList<String>();
	
	/** 聊天记录翻页管理类 构造函数. */
	public MsgPages(FileManager fm) {
		
		pageNum = fm.getLines() / Param.PAGE_LINES;
		if(fm.getLines() % Param.PAGE_LINES > 0)
			pageNum = pageNum + 1;

		pages = new StringBuilder[pageNum];
		for (int i = 0; i < pages.length; i++)
			pages[i] = new StringBuilder();
		
		/** 读取出每一行的内容. */
		String line;	
		int i = 0;
		/** 连接聊天记录文件的输入流.*/
		BufferedReader inputStream = fm.getBufferedReader();
		try {
			while ((line = inputStream.readLine()) != null) {
				i++;
				if (line.startsWith("[20")) {	//是否是以日期为开头，这有缺陷
					line = line.substring(1, line.length() - 1);	//提取日期，去除[]符号
					pageMap.put(line, pages[i / (Param.PAGE_LINES + 1)]);	//第51行就是第2页了
					dateList.add(line);	//可通过查看日期查看记录，故加入日期线
				}
				pages[i / (Param.PAGE_LINES + 1)].append(line + Param.NEWLINE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取第一页.
	 * @return String 第一页的文字内容.
	 * */
	public String getFristPage() {
		if (pageNum == 0)
			return null;
		
		currentPage = 0;
		return pages[0].toString();
	}

	/**
	 * 获取最后一页.
	 * @return String 获取最后一页的内容.
	 * */
	public String getLastPage() {
		if (pageNum == 0)
			return null;

		currentPage = pageNum - 1;
		return pages[pageNum - 1].toString();
	}

	/**
	 * 获取上一页. 
	 * @return String 当前页的上一页的内容.
	 * */
	public String getPrePage() {
		if (pageNum == 0)
			return null;
		
		currentPage--;
		if (currentPage <= 0)
			currentPage = 0;
		
		return pages[currentPage].toString();
	}

	/**
	 * 获取下一页.
	 * @return String 获取当前页下一页的内容.
	 * */
	public String getNextPage() {
		if (pageNum == 0)
			return null;

		currentPage++;
		if (currentPage >= pageNum - 1)
			currentPage = pageNum - 1;
		
		return pages[currentPage].toString();
	}

	/**
	 * 获取当前页. 
	 * @return String 获取当前页的内容.
	 * */
	public String getCurrentPage() {
		return pages[currentPage].toString();
	}

	/**
	 * 获取日期.
	 * */
	public String[] getDates() {
		String[] p = new String[1];
		return dateList.toArray(p);
	}

	/**
	 * 根据给定日期获取当日聊天内容的页.
	 * @param date
	 *            给定的日期的字符串表示.
	 * @return 给定日期的页的内容.
	 * */
	public String getPageByDate(String date) {
		return pageMap.get(date).toString();
	}

	/**
	 * 搜索. 通过给定的msg搜索其所在的页.
	 * @param msg
	 *            要搜索的内容.
	 * @return String 有则返回所在页的内容,无则返回null;
	 * */
	public String search(String msg) {
		for (int i = 0; i < pages.length; i++) {
			if (pages[i].toString().contains(msg)) {
				currentPage = i;
				return pages[i].toString();
			}
		}
		return null;
	}

	/**
	 * 获取某页（第几页）的内容.
	 * @param index
	 *            页下标 0~length-1
	 * @return String 搜索页内容.
	 * */
	public String getPageAt(int index) {
		if (index < 0 || index > pages.length)
			//throw new IndexOutOfBoundsException();
			return "没有这一页.Sorry,No this page.";
		
		return pages[index].toString();
	}

	/**
	 * 获取当前页码.
	 * @return int 页码.
	 * */
	public int getCurrentNum() {
		return currentPage + 1;
	}

	/**
	 * 获取总共页数.
	 * @return int 总共的页数.
	 * */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * 删除所有页面内容.
	 * */
	public void delete() {
		pageNum = 0;
		currentPage = 0;
		dateList.removeAll(dateList);
		for (StringBuilder i : pages)
			i.delete(0, i.toString().length());
	}

	
}
