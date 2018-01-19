package service;

/*
 * 功能需求：
 * 1.连接服务器
 * 2.断开服务器
 * 3.发送消息
 * 4.接收消息
 * 5.显示在线用户*/

import entity.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Iterator;

public class ClientService {

    private JFrame superFrame;

    public ClientService(JFrame superFrame) {
        this.superFrame = superFrame;
    }

    // 当某个变量在多个线程中用到，可以使用volatile修饰，最大程度的做到同一时刻，只有一个线程对该变量修改
    protected static volatile boolean isConn = false;
    // 接受和发送消息的socket
    private static DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket(5555);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private String userName;  // 拿到当前姓名

    // 静态代码块
    // 1.其中代码最先执行
    // 2.只执行一次

    // 1.连接服务器，给定一个状态，如果是未连接，不让发送
    // 2.将当前用户信息交给服务器，服务器将用户信息存储
    public void connServer(User user, String port, String ip, DefaultListModel listModel, JTextArea textArea) {
        if(isConn) {
            // 弹出提示框，告知用户已经连接
            JOptionPane.showMessageDialog(this.superFrame, "服务器已连接", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else if (port == null || port.equals("")) {
            JOptionPane.showMessageDialog(this.superFrame, "请填写服务器的端口号", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else if (ip == null || ip.equals("")) {
            JOptionPane.showMessageDialog(this.superFrame, "请填写服务器的ip地址", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else if (user == null || user.name == null) {
            JOptionPane.showMessageDialog(this.superFrame, "请填写用户名", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // 将用户信息发送给服务端
        // 将用户信息序列化为字节流
        try {
            ByteArrayOutputStream byOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byOut);
            // 序列化对象，将对象转换为字节流，以便使用
            objectOut.writeObject(user);
            this.userName = user.name;
            byte[] userByte = byOut.toByteArray();

            // 构建要发送给服务器的数据包
            DatagramPacket packet = new DatagramPacket(userByte,
                    userByte.length,
                    InetAddress.getByName(ip),
                    Integer.parseInt(port));

            // 发送此数据包给服务端
            socket.send(packet);
            textArea.append("已连接至服务器 " + ip + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.receiveMes(textArea, listModel);
        isConn = true;

        // 接收消息

        // 显示在线用户
        //this.showOnlineUsers(listModel);
    }

    // 2.断开连接
    public void disConnServer(String port, String ip) {
        if(!isConn) {
            JOptionPane.showMessageDialog(this.superFrame, "服务器未连接", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        String content = "over";
        try {
            DatagramPacket packet = new DatagramPacket(content.getBytes(),
                    content.getBytes().length,
                    InetAddress.getByName(ip),
                    Integer.parseInt(port));
            socket.send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isConn = false;
    }

    // 3.发送消息
    // 需要view层提供·服务器的端口号，ip，发送内容
    public void sendMsg(JTextField inputTextField, String port, String ip, String content, JTextArea textArea) {
        // 判断是否连接，端口号，ip，是否有发送内容

        // 提示的信息
        String infoString = null;
        if(!isConn) {
            // 未连接服务器
            infoString = "未连接服务器";
        } else if (port == null || port.equals("")) {
            infoString = "请填写服务器端口号";
        } else if (ip == null || ip.equals("")) {
            infoString = "请填写服务器ip地址";
        } else if (content == null || content.equals("")) {
            infoString = "发送内容不能为空";
        }
        if(infoString != null) {
            JOptionPane.showMessageDialog(this.superFrame, infoString, "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 发送消息到服务器
        try {
            System.out.println(InetAddress.getByName(ip));
            System.out.println(Integer.parseInt(port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            if (content.equals("over")) {
                content += " ";
            }

            DatagramPacket packet = new DatagramPacket(content.getBytes(),
                    content.getBytes().length,
                    InetAddress.getByName(ip),
                    Integer.parseInt(port));
            socket.send(packet);

            if(content.equals("over")) {
                isConn = false;
            }

            // 显示在当前的聊天内容的文本区
            textArea.append(this.userName + ":");
            textArea.append(content + "\n");

            inputTextField.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 4.接收消息
    public void receiveMes(JTextArea textArea, DefaultListModel listModel) {
        new ReceiveMsgThread(this.socket, textArea, listModel).start();
    }

    // 5.显示在线用户
    public void showOnlineUsers(DefaultListModel listModel) {

        listModel.removeAllElements();
        // 添加用户名
        Iterator<User> iterator = ServerService.allUsers.values().iterator();
        while(iterator.hasNext()) {
            User user = iterator.next();
            listModel.addElement(user.name);
        }
    }
}

// 创建线程类，用来处理接收消息的功能。
class ReceiveMsgThread extends Thread {

    private DatagramSocket socket;
    private JTextArea textArea;
    private DefaultListModel listModel;

    private byte[] receiveContent;

    // 通过构造方法拿到socket参数
    public ReceiveMsgThread(DatagramSocket socket, JTextArea textArea, DefaultListModel listModel) {
        this.socket = socket;
        this.textArea = textArea;
        this.listModel = listModel;
    }

    public void run() {
        // 监听是否有数据包发送过来
        while(ClientService.isConn) {

            // 新建接收消息的数据包
            byte[] content = new byte[1024 * 64];
            DatagramPacket rPacket = new DatagramPacket(content, content.length);
            try {
                this.socket.receive(rPacket);

                // 将消息内容显示在文本框中
                String IP = rPacket.getAddress().getHostAddress();
                int Port = rPacket.getPort();
                System.out.println("Received packet from " + IP + ":" + Port);
                this.receiveContent = rPacket.getData();
                try {
                    ByteArrayInputStream in = new ByteArrayInputStream(receiveContent);
                    ObjectInputStream objectIn = new ObjectInputStream(in);
                    DefaultListModel userModel = (DefaultListModel)objectIn.readObject();
                    this.listModel.removeAllElements();
                    int totalUsers = userModel.size();
                    for(int i=0; i<totalUsers; i++) {
                        this.listModel.addElement(userModel.elementAt(i));
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("2");
                }
            } catch (IOException e) {
                System.out.println("3");
                String contentStr = new String(receiveContent, 0, rPacket.getLength());
                textArea.append(contentStr);
            }
        }
    }
}
