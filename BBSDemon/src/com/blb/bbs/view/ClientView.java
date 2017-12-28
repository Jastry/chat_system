package com.blb.bbs.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

//import java
 
public class ClientView {
	public  static void main(String []args) {
		ClientFrame client = new ClientFrame();
	}
}


class ClientFrame extends JFrame {
	/*
	 * 空间为私有变量
	 * 北部
	 * 
	 * 采用网格布局，一行八列
	 * */
	private JPanel northPanel;		//北部底层容器
	private JLabel northLable;		//显示端口号的标签
	private JTextField portTF;		//显示端口号
	private JLabel portLable;		//端口号标签
	private JLabel serverIPLabel;	//显示服务器IP标签
	private JTextField serverIPTF;	//显示服务器IP
	private JLabel nameLabel;		//显示当前用户的姓名标签
	private JTextField nameTF;		//显示当前用户的姓名
	private JButton connButton;		//连接服务器按钮
	private JButton disConnButton;	//断开服务器按钮
	
	//中部部分
	private JSplitPane splitPane;			//承载中部所有组件
	private JScrollPane leftScrollPane;
	private JList userListView;				//显示在线用户的组件
	private DefaultListModel userModel;		//管理 list 上的数据
	private JScrollPane rightScrollPane;	//显示聊天内容容器
	private JTextArea contentTextArea;		//显示聊天内容组件
	
	//底部
	private JPanel southPanel;	//底部的底层容器
	private JTextField inputTF;	//输入框消息
	private JButton sendBtn;	//发送按钮
	
	/*
	 * 自定义构造方法，在构造方法中对控件进行初始化布局
	 * */
	public ClientFrame() {
		//北部
		
		this.northPanel = new JPanel();
		this.northPanel.setBorder(new TitledBorder("连接信息"));
		this.portLable = new JLabel("端口");
		this.portTF = new JTextField("6666");
		this.serverIPLabel = new JLabel("服务器");
		this.serverIPTF = new JTextField("127.0.0.1");
		this.nameLabel = new JLabel("姓名");
		this.nameTF = new JTextField("符文解");
		this.disConnButton = new JButton("断开");
		this.connButton = new JButton("连接");
		
		/* 设置布局 */
		this.northPanel.setLayout(new GridLayout(1, 8));
		this.northPanel.add(this.portLable);
		this.northPanel.add(this.portTF);
		this.northPanel.add(this.serverIPLabel);
		this.northPanel.add(this.serverIPTF);
		this.northPanel.add(this.nameLabel);
		this.northPanel.add(this.nameTF);
		this.northPanel.add(this.connButton);
		this.northPanel.add(this.disConnButton);
		
		//中部
		this.userModel = new DefaultListModel();
		this.userListView = new JList(this.userModel);
		this.leftScrollPane = new JScrollPane(this.userListView);
		

		this.leftScrollPane.add(this.userListView);
		//设置标题
		this.leftScrollPane.setBorder(new TitledBorder("在线用户"));
		//中右部分
		//
		this.contentTextArea = new JTextArea();
		this.contentTextArea.setText("聊天内容");
		this.rightScrollPane = new JScrollPane(this.contentTextArea);
		//

		
		this.contentTextArea.setEditable(false);
		this.rightScrollPane.setBorder(new TitledBorder("聊天信息"));
		this.rightScrollPane.add(this.contentTextArea);
		
		//中间部分最底层容器
		this.splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, 
				this.leftScrollPane, 
				this.rightScrollPane);
		
		//中间分割线的位置
		this.splitPane.setDividerLocation(100);
		
		//底部部分
		this.southPanel = new JPanel();
		this.southPanel.setBorder(new TitledBorder("发送消息"));
		
		this.southPanel.setLayout(new BorderLayout());
		this.inputTF = new JTextField();
		this.sendBtn = new JButton("发送");
		this.southPanel.add(this.inputTF, BorderLayout.CENTER);
		this.southPanel.add(this.sendBtn, BorderLayout.EAST);
		
		//最底层容器
		this.setLayout(new BorderLayout());
		this.setTitle("客户机");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.splitPane, BorderLayout.CENTER);
		this.add(this.southPanel, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//添加监听事件
		//连接
		this.connButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// 连接服务器
				/*
				 * 调用 service 层中连接服务器的方法即可
				 * */
				System.out.println("连接服务器");
			}
		});
		/*
		 * 其他按钮的自己加
		 * 添加输入消息框的 enter 事件
		 * */
		
		this.inputTF.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("点击会送 888 黄金");
			}
		});

		/*
		 * 发送动作
		 * */
		this.sendBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("发送成功");
			}
			
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}