package com.jq.client.protocol.udp;

import javax.swing.JOptionPane;

/** 拒绝接收文件信息 */
public class RejectMsg extends AbstractMessage {

	public RejectMsg(String msg) {
		super(msg);
	}

	/** 重写update方法*/
	@Override
	public void update(UDPServer UDP) {
		JOptionPane.showConfirmDialog(null, "用户[" + ID + "]拒绝接收文件[" + msgBody
				+ "]", "拒绝接收文件", JOptionPane.WARNING_MESSAGE);
	}

}
