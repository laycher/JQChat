
package com.jq.client.gui.mainframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jq.client.gui.common.MsgManagerPanel;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.FileManager;
import com.jq.util.Friend;
import com.jq.util.MyResources;

/**聊天记录管理器*/
public class MessageManager extends JFrame {
	private static final long serialVersionUID = 1804419973563604654L;
	
	private JPanel background;
	private JButton closeButton;
	private JList friendTree;
	private JScrollPane jScrollPane1;
	private MsgManagerPanel messagePanel;

	private Map<String, FileManager> msgMap = new HashMap<String, FileManager>();
	private String lastID = null;
	private UDPServer UDP = null;

	public MessageManager(Friend[] friendList,UDPServer UDP) {
		this.UDP = UDP;
		initComponents(friendList);
		initEvent();
		
		setSize(550, 525);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	private void initComponents(final Friend[] friendList) {
		
		background = new JPanel();
		jScrollPane1 = new JScrollPane();
		friendTree = new JList();
		
		FileManager f = new FileManager(UDP.hostUser.getID(),UDP.hostUser.getID());
		messagePanel = new MsgManagerPanel(null,f);
		msgMap.put(UDP.hostUser.getID(), f);
		
		closeButton = new JButton();

		getContentPane().setLayout(null);
		
		background.setLayout(null);

		friendTree.setModel(new AbstractListModel() {
			private static final long serialVersionUID = 5250468670023116735L;
			Friend[] strings = friendList;

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});

		friendTree.setToolTipText("双击查看聊天记录");
		jScrollPane1.setViewportView(friendTree);

		background.add(jScrollPane1);
		jScrollPane1.setBounds(0, 0, 140, 455);

		messagePanel.setLayout(null);
		background.add(messagePanel);
		messagePanel.setBounds(138, 0, 400, 455);

		getContentPane().add(background);
		background.setBounds(7, 5, 540, 455);

		closeButton.setText("关闭");
		getContentPane().add(closeButton);
		closeButton.setBounds(430, 465, 90, 30);

		setTitle("聊天记录管理");
		setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		pack();
	}
	
	private void initEvent(){
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		friendTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				Friend friend = (Friend)friendTree.getSelectedValue();

				if (friend != null && !friend.getID().equals(UDP.hostUser.getID()) && !friend.getID().equals(lastID)){
					messagePanel.setMsgPages(check(friend.getID()));
					lastID = friend.getID();
				}
			}
		});
	}

	private FileManager check(String friendID){
		FileManager fileManager = msgMap.get(friendID);
		if (fileManager == null){
			fileManager = new FileManager(UDP.hostUser.getID(),friendID);
			msgMap.put(friendID, fileManager);
		}
		
		return fileManager;
	}


}