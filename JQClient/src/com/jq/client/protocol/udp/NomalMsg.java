
package com.jq.client.protocol.udp;

import com.jq.util.SoundPlayer;

/**
 * 	正常消息理类.
 * */
public class NomalMsg extends AbstractMessage{

	/**正常消息构造函数*/
	public NomalMsg(String msg) {
		super(msg);
	}

	/**
	 * 	消息格式--
	 * 		[ID_信息体]
	 * 	信息体--
	 * 		[11-27 00:00:00 xxxxxxxxxxxxxxxxxxxxxxxx]
	 * 		日期                     字体格式        接收信息
	 * */
	@Override
	public void update(UDPServer UDP) {
		SoundPlayer.play(SoundPlayer.MSG);
		UDP.receiveMsg(ID, msgBody);
	}
}
