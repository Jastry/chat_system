package service;

// 服务端的功能
// 1.接收到各个客户端的消息，并进行分发
// 2.存储所有的连接用户
// 3.启动服务
// 4.关闭服务

import entity.User;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ServerService {

    // 是否启动服务的标志
    protected volatile static boolean isStart = false;
    // 服务端的socket
    protected static DatagramSocket socket;
    // 存储所有连接的用户
    protected volatile static HashMap<String, User> allUsers;
    // 服务器端界面的消息窗口
    private JTextArea textArea;

    static {
        try {
            socket = new DatagramSocket(8080);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        allUsers = new HashMap<String, User>();
    }

    public ServerService(JTextArea textArea) {
        this.textArea = textArea;
    }

    // 开辟一个新线程，用来接收消息，在接受到消息的时候需要判断该消息的种类
    // 1.连接消息
    // 2.聊天消息
    // 3.断开消息
    //   如果有，说明是2或者3;如果没有就是1。如果接收到的消息内容为over，那么就断开连接。


    // 启动服务端
    public void startServer(DefaultListModel userModel) {
        isStart = true;
        this.textArea.append("服务器已启动\n");
        new ServerThread(textArea, userModel).start();

    }

    // 关闭服务端
    public void stopServer() {
        isStart = false;
        textArea.append("服务端已关闭\n");
        //socket.close();
    }
}

// 开辟的线程，处理消息、用户等操作
class ServerThread extends Thread {

    private JTextArea textArea;
    private DefaultListModel userModel;

    public ServerThread(JTextArea textArea, DefaultListModel userModel) {
        this.textArea = textArea;
        this.userModel = userModel;
    }

    public void run() {
        while(ServerService.isStart) {
            // 接受各种消息
            // 创建数据包，用来接收消息
            byte[] cache = new byte[1024 * 64];
            DatagramPacket receivePacket = new DatagramPacket(cache, cache.length);
            try {
                ServerService.socket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 1.如果是连接消息，将用户添加进map中
            //   1.得到客户端的IP
            String clientIP = receivePacket.getAddress().getHostAddress();
            //   2.判断当前map中是否有此键，如果没有，说明是连接消息，不需要将此消息分发给其他用户
            Set allKeys = ServerService.allUsers.keySet();
            if(!allKeys.contains(clientIP)) {
                // 当前消息为连接消息，将此用户存入map中
                byte[] userData = receivePacket.getData();
                // 将字节流转换为User对象
                ByteArrayInputStream in = new ByteArrayInputStream(userData);
                try {
                    ObjectInputStream objectIn = new ObjectInputStream(in);
                    User user = (User)objectIn.readObject();
                    // 新上线用户添加进map中
                    ServerService.allUsers.put(clientIP, user);
                    this.userModel.addElement(clientIP+ "/" + user.name);
                    textArea.append("A new client connected.\n");
                    textArea.append("User name: " + user.name + "\n");
                    textArea.append("User IP：" + user.userIP + "\n");

                    ByteArrayOutputStream byOut = new ByteArrayOutputStream();
                    ObjectOutputStream objectOut = new ObjectOutputStream(byOut);
                    objectOut.writeObject(this.userModel);
                    byte[] userByte = byOut.toByteArray();

                    Iterator<Map.Entry<String, User>> iterator = ServerService.allUsers.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, User> entry = iterator.next();
                        String keyIP = entry.getKey();
                        User valueUser = entry.getValue();
                        DatagramPacket packet = new DatagramPacket(userByte,
                                userByte.length,
                                InetAddress.getByName(keyIP),
                                Integer.parseInt(valueUser.userPort));
                        ServerService.socket.send(packet);
                        System.out.println("123");
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // UserMap里已经有此用户
                // 要么是聊天消息，要么是断开连接消息

                // Step One 拿到消息内容
                byte[] content = receivePacket.getData();
                String contentStr = new String(content, 0, receivePacket.getLength());
                // 去除首位的空白
                if(contentStr.equals("over")) {
                    // 断开连接
                    User removeUser = ServerService.allUsers.get(clientIP);
                    this.textArea.append("Client " + removeUser.name + "/" + removeUser.userIP + " disconnected.\n");
                    this.userModel.removeElement(clientIP + "/" + removeUser.name);
                    ServerService.allUsers.remove(clientIP);

                    try {
                        ByteArrayOutputStream byOut = new ByteArrayOutputStream();
                        ObjectOutputStream objectOut = new ObjectOutputStream(byOut);
                        objectOut.writeObject(this.userModel);
                        byte[] userByte = byOut.toByteArray();
                        Iterator<Map.Entry<String, User>> iterator = ServerService.allUsers.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, User> entry = iterator.next();
                            String keyIP = entry.getKey();
                            User valueUser = entry.getValue();
                            DatagramPacket packet = new DatagramPacket(userByte,
                                    userByte.length,
                                    InetAddress.getByName(keyIP),
                                    Integer.parseInt(valueUser.userPort));
                            ServerService.socket.send(packet);
                            System.out.println("123");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    // 正常消息,为其他客户端分发该消息
                    // 遍历map，获取其他客户端的ip和port，发送消息
                    Iterator<Map.Entry<String, User>> iterator = ServerService.allUsers.entrySet().iterator();
                    // 将用户名和消息拼接到一块
                    User currentUser = ServerService.allUsers.get(clientIP);
                    String currentName = currentUser.name;
                    String conStr = currentName + ":\n" + contentStr + "\n";
                    System.out.println(conStr);
                    DatagramPacket sendPacket = new DatagramPacket(conStr.getBytes(), conStr.getBytes().length);
                    while(iterator.hasNext()) {
                        Map.Entry<String, User> entry = iterator.next();
                        String keyIP = entry.getKey();
                        User valueUser = entry.getValue();
                        if(!keyIP.equals(clientIP)) {
                            // 分发消息
                            try {
                                sendPacket.setAddress(InetAddress.getByName(keyIP));
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                            sendPacket.setPort(Integer.parseInt(valueUser.userPort));
                            try {
                                ServerService.socket.send(sendPacket);
                                System.out.println("Send content to " + keyIP + ":" + valueUser.userPort + "/" + valueUser.name);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            // 分发消息给各个客户端

        }
    }
}

/*
class DataModel extends DefaultListModel {

    private HashMap<String, User> map = new HashMap<>();

    public DataModel(HashMap map) {
        this.map = map;
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            addElement(entry.getKey() + ":" + entry.getValue());
        }
    }
}
*/
