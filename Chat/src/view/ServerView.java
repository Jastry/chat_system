package view;

import entity.User;
import service.ServerService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ServerView {
    public static void main(String[] args) {
        new ServerFrame();
    }
}

// 创建客户端界面，frame使用的布局方式为边界布局
class ServerFrame extends JFrame implements ActionListener{


    // 页面北部
    // 采用流式布局
    private JPanel northPanel;  // 北部最底层容器
    private JLabel portLabel;
    private JTextField portTextField;
    private JLabel serverIPLabel;
    private JTextField serverIPTextField;
    private JLabel stateLabel;
    private JTextField stateTextField;
    private JButton connButton;
    private JButton disConnButton;

    // 页面中部
    private JSplitPane splitPanel;  // 中部底层容器
    private JScrollPane leftScrollPanel;
    private JList userListView;  // 显示在线用户的组件
    private DefaultListModel userModel;  // 管理List上的数据
    private JScrollPane rightScrollPanel;
    private JTextArea contentTextArea;

    // 页面底部
    private JPanel southPanel;
    private JTextField inputTextField;
    private JButton sendBtn;

    // 实例化ServerService类
    private ServerService ss;

    // 自定义构造方法，在构造方法中对控件进行初始化
    public ServerFrame() {
        // 页面北部
        this.northPanel = new JPanel();
        this.northPanel.setBorder(new TitledBorder("Connection Info"));
        this.portLabel = new JLabel("     Port");
        this.portTextField = new JTextField("6666");
        this.portTextField.setEditable(false);
        this.serverIPLabel = new JLabel("   Server IP");
        try {
            String hostIP = InetAddress.getLocalHost().getHostAddress();
            this.serverIPTextField = new JTextField(hostIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.serverIPTextField.setEditable(false);
        this.stateLabel = new JLabel("    Server state");
        this.stateTextField = new JTextField("Stopped");
        this.stateTextField.setEditable(false);
        this.connButton = new JButton("Start");
        connButton.addActionListener(this);
        this.disConnButton = new JButton("Stop");
        disConnButton.addActionListener(this);

        // 设置布局管理器
        this.northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.portLabel.setPreferredSize(new Dimension(60, 30));
        this.northPanel.add(this.portLabel);
        this.portTextField.setPreferredSize(new Dimension(140, 30));
        this.northPanel.add(this.portTextField);
        this.serverIPLabel.setPreferredSize(new Dimension(80, 30));
        this.northPanel.add(this.serverIPLabel);
        this.serverIPTextField.setPreferredSize(new Dimension(140, 30));
        this.northPanel.add(this.serverIPTextField);
        this.stateLabel.setPreferredSize(new Dimension(100, 30));
        this.northPanel.add(stateLabel);
        this.stateTextField.setPreferredSize(new Dimension(140, 30));
        this.northPanel.add(stateTextField);
        this.connButton.setPreferredSize(new Dimension(140, 30));
        this.northPanel.add(this.connButton);
        this.disConnButton.setPreferredSize(new Dimension(140, 30));
        this.disConnButton.setEnabled(false);
        this.northPanel.add(this.disConnButton);

        // 页面中部
        this.contentTextArea = new JTextArea();
        this.contentTextArea.setEditable(false);
        ss  = new ServerService(contentTextArea);
        this.userModel = new DefaultListModel();  // 管理List列表的数据
        this.userListView = new JList(this.userModel);  // 创建List对象，并将管理数据传入
        this.rightScrollPanel = new JScrollPane(this.userListView);
        this.rightScrollPanel.setBorder(new TitledBorder("Online Users"));
        this.leftScrollPanel = new JScrollPane(this.contentTextArea);
        this.leftScrollPanel.setBorder(new TitledBorder("Message"));

        this.splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                true,
                this.leftScrollPanel,
                this.rightScrollPanel);
        this.splitPanel.setDividerLocation(800);

        // 页面底部
        this.southPanel = new JPanel();
        this.southPanel.setBorder(new TitledBorder(""));
        this.southPanel.setLayout(new BorderLayout());
        this.inputTextField = new JTextField();
        this.sendBtn = new JButton("Send");
        this.southPanel.add(this.inputTextField, BorderLayout.CENTER);
        this.southPanel.add(this.sendBtn, BorderLayout.EAST);

        // 最底层容器
        this.setLayout(new BorderLayout());
        this.setTitle("Server");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.splitPanel, BorderLayout.CENTER);
        this.add(this.southPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    // 按钮的响应事件

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) {
            JButton btn = (JButton)e.getSource();
            if(btn.getText().equals("Start")) {
                ss.startServer(this.userModel);
                this.stateTextField.setText("Running");
                this.connButton.setEnabled(false);
                this.disConnButton.setEnabled(true);
            } else if (btn.getText().equals("Stop")) {
                ss.stopServer();
                this.stateTextField.setText("Stopped");
                this.disConnButton.setEnabled(false);
                this.connButton.setEnabled(true);
            } else {
                // 发送
            }
        }
    }
}

/*class DataModel extends DefaultListModel {

    private HashMap<String, User> map = new HashMap<>();

    public DataModel() {
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            addElement(entry.getKey() + ":" + entry.getValue());
        }
    }
}*/
