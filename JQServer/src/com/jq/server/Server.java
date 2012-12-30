package com.jq.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.UIManager;

import com.jq.server.service.SQLServerProcess;
import com.jq.util.Param;
import com.jq.util.PropertyFile;

public class Server {
	public static void main(String[] args){
		//JFrame.setDefaultLookAndFeelDecorated( true ); 
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("微软雅黑", Font.PLAIN, 12));
		} catch (Exception e) {
			e.printStackTrace();
		}
		new SQLServerProcess(checkPropertyFile());
	}
	
	/**查看数据库配置文件是否存在*/
	private static PropertyFile checkPropertyFile(){
		File file = new File(Param.CURRENTPATH + Param.FILENAME);
		File dir = new File(Param.CURRENTPATH);
		//如果不存在则创建一个默认的
		if (!file.exists()) {
			dir.mkdirs();	//产生目录
			try {
				file.createNewFile();	//创建文件
				//PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				FileWriter out = new FileWriter(file);
				out.write(Param.PROPERTYFILE);	//把默认的内容写进去
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return new PropertyFile(Param.CURRENTPATH + Param.FILENAME);
	}
}
