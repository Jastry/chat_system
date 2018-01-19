package view;

//import entity.User;
import entity.User;
import service.ClientService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ClientView {
    public static void main(String[] args) {
        new ClientFrame();
    }
}

// 创建客户端界面，frame使用的布局方式为边界布局
class ClientFrame extends JFrame implements ActionListener {


    // 页面北部
    // 采用网格布局，一行八列
    private JPanel northPanel;  // 北部最底层容器
    private JLabel portLabel;
    private JTextField portTextField;
    private JLabel serverIPLabel;
    private JTextField serverIPTextField;
    private JLabel nameLabel;
    private JTextField nameTextField;
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
    protected JTextField inputTextField;
    private JButton sendBtn;

    private ClientService clientService;
    private User user;

    // 自定义构造方法，在构造方法中对控件进行初始化
    public ClientFrame() {

        this.clientService = new ClientService(this);

        // 页面北部
        this.northPanel = new JPanel();
        this.northPanel.setBorder(new TitledBorder("Connection Info"));
        this.portLabel = new JLabel("     Port");
        this.portTextField = new JTextField("8080");
        this.serverIPLabel = new JLabel("   Server IP");
        this.serverIPTextField = new JTextField("127.0.0.1");
        this.nameLabel = new JLabel("    Name");
        this.nameTextField = new JTextField("SYC");
        this.connButton = new JButton("Connect");
        this.connButton.addActionListener(this);
        this.disConnButton = new JButton("Disconnect");
        this.disConnButton.addActionListener(this);

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
        this.nameLabel.setPreferredSize(new Dimension(60, 30));
        this.northPanel.add(this.nameLabel);
        this.nameTextField.setPreferredSize(new Dimension(140, 30));
        this.northPanel.add(this.nameTextField);
        this.connButton.setPreferredSize(new Dimension(140, 30));
        this.northPanel.add(this.connButton);
        this.disConnButton.setPreferredSize(new Dimension(140, 30));
        this.disConnButton.setEnabled(false);
        this.northPanel.add(this.disConnButton);

        // 页面中部
        this.userModel = new DefaultListModel();  // 管理List列表的数据
        this.userListView = new JList(this.userModel);  // 创建List对象，并将管理数据传入
        this.leftScrollPanel = new JScrollPane(this.userListView);
        this.leftScrollPanel.setBorder(new TitledBorder("Online Users"));
        this.contentTextArea = new JTextArea();
        this.contentTextArea.setEditable(false);
        this.rightScrollPanel = new JScrollPane(this.contentTextArea);
        this.rightScrollPanel.setBorder(new TitledBorder("Message"));

        this.splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                true,
                this.leftScrollPanel,
                this.rightScrollPanel);
        this.splitPanel.setDividerLocation(200);

        // 页面底部
        this.southPanel = new JPanel();
        this.southPanel.setBorder(new TitledBorder(""));
        this.southPanel.setLayout(new BorderLayout());
        this.inputTextField = new JTextField();
        this.sendBtn = new JButton("Send");
        this.sendBtn.addActionListener(this);
        this.southPanel.add(this.inputTextField, BorderLayout.CENTER);
        this.southPanel.add(this.sendBtn, BorderLayout.EAST);

        // 最底层容器
        this.setLayout(new BorderLayout());
        this.setTitle("Client");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.splitPanel, BorderLayout.CENTER);
        this.add(this.southPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        // 添加监听事件
        // inputTextField
        this.inputTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Sended");
                clientService.sendMsg(inputTextField,
                        portTextField.getText(),
                        serverIPTextField.getText(),
                        inputTextField.getText(),
                        contentTextArea);

            }
        });

        // sendBtn
        this.sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Sended");
                clientService.sendMsg(inputTextField,
                        portTextField.getText(),
                        serverIPTextField.getText(),
                        inputTextField.getText(),
                        contentTextArea);
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton) {
            JButton btn = (JButton) e.getSource();
            if(btn.getText().equals("Connect")) { 
                user = new User();
                user.name = nameTextField.getText();
                try {
                    user.userIP = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }
                user.userPort = "5555";

                clientService.connServer(user,
                        portTextField.getText(),
                        serverIPTextField.getText(),
                        userModel,
                        contentTextArea);

                this.portTextField.setEnabled(false);
                this.serverIPTextField.setEnabled(false);
                this.nameTextField.setEnabled(false);
                this.connButton.setEnabled(false);
                this.disConnButton.setEnabled(true);
            }else if(btn.getText().equals("Disconnect")) {
                clientService.disConnServer(portTextField.getText(), serverIPTextField.getText());

                this.portTextField.setEnabled(true);
                this.serverIPTextField.setEnabled(true);
                this.nameTextField.setEnabled(true);
                this.connButton.setEnabled(true);
                this.disConnButton.setEnabled(false);
                this.userModel.removeAllElements();
            } else if (btn.getText().equals("Send")) {

            }
        }
    }
}
