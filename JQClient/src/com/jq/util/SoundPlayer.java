
package com.jq.util;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/** 提示声音 */
public class SoundPlayer {
	
	public static final String ADDTION  = "resources/Sound/addtion.wav";
	public static final String MSG  = "resources/Sound/msg.wav";
	public static final String ONLINE  = "resources/Sound/Online.wav";
	
	public static void play(String filePath) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(
											//new File(ClassLoader.getSystemResource(filePath).getFile()));// 获得音频输入流
					new File(SoundPlayer.class.getClassLoader().getResource(filePath).getPath()));
			
			
			AudioFormat aif = ais.getFormat();// 指定声音流中特定数据安排
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,aif);
			SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(info);
			// 从混频器获得源数据行
			sdl.open(aif);// 打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。
			sdl.start();// 允许数据行执行数据 I/O
			
			int BUFFER_SIZE = 4000 * 4;
			int nByte = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while (nByte != -1) {
				nByte = ais.read(buffer, 0, BUFFER_SIZE);// 从音频流读取指定的最大数量的数据字节，并将其放入给定的字节数组中。
				if (nByte >= 0) {
					 sdl.write(buffer, 0, nByte);// 通过此源数据行将音频数据写入混频器。
					//System.out.println(sdl.write(buffer, 0, nByte));
				}
			}
			sdl.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
