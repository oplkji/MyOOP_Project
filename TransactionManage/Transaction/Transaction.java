package TransactionManage.Transaction;

import BankManage.BankManagement;
import Database.ConnectDB;
import TransactionManage.Accept.AcceptTransaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.time.LocalDateTime;

public class Transaction extends JFrame {
    private static String username;
    private static String userID;
    private static String transaction_Money;
    private static String content;
    public Transaction(JFrame frame) {
        frame.setVisible(false);

        JLabel titleLabel = new JLabel("Chuyển tiền tới số tài khoản");
        titleLabel.setFont(new Font("Open Sans", Font.BOLD, 25));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backButton = new JButton("<");
        backButton.setFont(new Font("Open Sans", Font.BOLD, 20));
        backButton.setBackground(new Color(173, 216, 230));
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setFocusable(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            this.setVisible(false);
            frame.setVisible(true);
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230));
        mainPanel.setBounds(0,0,800,130);
        mainPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.WEST);

        JLabel source = new JLabel("Nguồn chuyển tiền: " + BankManagement.getUserName());
        source.setFont(new Font("Open Sans", Font.PLAIN, 18));
        source.setAlignmentX(Component.LEFT_ALIGNMENT);
        source.setBounds(150,150,500,30);

        JLabel label = new JLabel("Số tài khoản:" + BankManagement.getUserID());
        label.setFont(new Font("Open Sans", Font.PLAIN, 18));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBounds(150,200,500,30);

        JLabel label1 = new JLabel("Chuyển đến:");
        label1.setFont(new Font("Open Sans", Font.PLAIN, 18));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);
        label1.setBounds(150,250,200,30);

        JTextField textField_1 = new JTextField();
        textField_1.setFont(new Font("Open Sans", Font.PLAIN, 18));
        textField_1.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField_1.setBounds(350,250,400,30);

        JLabel label2 = new JLabel("Tên người hưởng thụ:");
        label2.setFont(new Font("Open Sans", Font.PLAIN, 18));
        label2.setAlignmentX(Component.LEFT_ALIGNMENT);
        label2.setBounds(150,300,200,30);

        JTextField textField_2 = new JTextField();
        textField_2.setFont(new Font("Open Sans", Font.PLAIN, 18));
        textField_2.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField_2.setBounds(350,300,400,30);
        textField_2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(userID);
                textField_2.setText(username);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                userID = textField_1.getText();
                setNameFromDB();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JLabel label3 = new JLabel("Số tiền chuyển:");
        label3.setFont(new Font("Open Sans", Font.PLAIN, 18));
        label3.setAlignmentX(Component.LEFT_ALIGNMENT);
        label3.setBounds(150,350,200,30);

        JTextField textField_3 = new JTextField();
        textField_3.setFont(new Font("Open Sans", Font.PLAIN, 18));
        textField_3.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField_3.setBounds(350,350,400,30);

        JLabel label4 = new JLabel("Nội dung chuyển tiền:");
        label4.setFont(new Font("Open Sans", Font.PLAIN, 18));
        label4.setAlignmentX(Component.LEFT_ALIGNMENT);
        label4.setBounds(150,400,200,30);

        JTextField textField_4 = new JTextField(BankManagement.getUserName() + "CHUYỂN TIỀN");
        textField_4.setFont(new Font("Open Sans", Font.PLAIN, 18));
        textField_4.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField_4.setBounds(350,400,400,30);

        JButton button = new JButton("Tiếp tục");
        button.setFont(new Font("Open Sans", Font.PLAIN, 18));
        button.setBackground(new Color(173, 216, 230));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setBounds(350,450,100,30);
        button.addActionListener(e -> {
            transaction_Money = textField_3.getText();
            content = textField_4.getText();
            new AcceptTransaction(this);
        });

        this.setTitle("TransactionManage");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setResizable(false);
        this.add(mainPanel);
        this.add(label);
        this.add(label1);
        this.add(textField_1);
        this.add(source);
        this.add(label2);
        this.add(textField_2);
        this.add(label3);
        this.add(textField_3);
        this.add(label4);
        this.add(textField_4);
        this.add(button);
        this.setLayout(null);
        this.setVisible(true);
    }
    public static void updateData(){
        String sql = "INSERT INTO history (userID, username, transaction_time, transaction_money,received_username, content, received_userID) VALUES (?,?,?,?,?,?,?)";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1, BankManagement.getUserID());
            stmt.setString(2, BankManagement.getUserName());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, -Integer.parseInt(transaction_Money));
            stmt.setString(5, username);
            stmt.setString(6,content);
            stmt.setString(7,userID);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static String getUserID(){
        return userID;
    }
    public static int getTransactionMoney(){
        return Integer.parseInt(transaction_Money);
    }
    public void setNameFromDB(){
        String sql = "SELECT username FROM bankaccount WHERE userID=?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1,userID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                username = rs.getString("username");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
