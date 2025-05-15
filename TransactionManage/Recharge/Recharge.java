package TransactionManage.Recharge;

import BankManage.BankManagement;
import Database.ConnectDB;
import Login.Login;
import TransactionManage.Authentication.Authentication;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class Recharge extends JFrame {
    private static String count;
    JTextField textField;
    public Recharge(JFrame frame) {
        JLabel label = new JLabel();
        label.setText("Nhập số tiền muốn nạp:");
        label.setBounds(10, 100, 300, 30);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font("Times New Roman",Font.PLAIN,20));

        JLabel announce = new JLabel();
        announce.setText("Mã PIN không chính xác");
        announce.setBounds(10, 50, 300, 30);
        announce.setHorizontalAlignment(JLabel.CENTER);
        announce.setVerticalAlignment(JLabel.CENTER);
        announce.setFont(new Font("Times New Roman",Font.PLAIN,20));
        announce.setVisible(false);

        textField = new JTextField();
        textField.setBounds(300, 100, 150, 30);

        JButton button = new JButton();
        button.setText("Submit");
        button.setBounds(220,150,100,30);
        button.setFont(new Font("Times New Roman",Font.PLAIN,20));
        button.addActionListener(e -> {
            new Authentication(this);
            this.setVisible(false);
            Authentication.buttons[10].addActionListener(ae -> {
                if(Authentication.checkPIN()){
                    count = textField.getText();
                    this.rechargeMoney();
                    new BankManagement();
                    frame.setVisible(false);
                    this.setVisible(false);
                } else {
                    this.setVisible(true);
                    announce.setVisible(true);
                }
            });
        });

        JButton button_1 = new JButton();
        button_1.setText("Back");
        button_1.setBounds(220,200,100,30);
        button_1.setFont(new Font("Times New Roman",Font.PLAIN,20));
        button_1.addActionListener(e -> {
            this.setVisible(false);
        });

        this.setTitle("Recharge Money");
        this.setSize(500,300);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(label);
        this.add(textField);
        this.add(announce);
        this.add(button);
        this.add(button_1);
        this.setVisible(true);
    }
    public void rechargeMoney(){
        String sql = "UPDATE bankaccount SET money=? where userID=?";
        String sql_2 = "SELECT userID FROM login WHERE username=?";
        String sql_3 = "SELECT money FROM bankaccount WHERE userID=?";
        String sql_4 = "INSERT INTO history VALUES(?,?,?,?,?,?,?)";
        String userID = "";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt_2 = conn.prepareStatement(sql_2);
            PreparedStatement stmt_3 = conn.prepareStatement(sql_3);
            PreparedStatement stmt_4 = conn.prepareStatement(sql_4);){
            conn.setAutoCommit(false);
            stmt_2.setString(1,Login.getUsername());
            ResultSet rs = stmt_2.executeQuery();
            if(rs.next()){
                userID = rs.getString("userID");
                System.out.println(userID);
            }

            stmt_3.setString(1,userID);
            ResultSet rs2 = stmt_3.executeQuery();
            if(rs2.next()){
                count = Integer.toString(Integer.parseInt(count) + rs2.getInt("money"));
                System.out.println(count);
            }

            stmt.setString(1,count);
            stmt.setString(2,userID);
            stmt.executeUpdate();

            stmt_4.setString(1,"001");
            stmt_4.setString(2,"admin");
            stmt_4.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            stmt_4.setInt(4,Integer.parseInt(textField.getText()));
            stmt_4.setString(5,BankManagement.getUserName());
            stmt_4.setString(6,"Số tiền nạp: " + textField.getText());
            stmt_4.setString(7,BankManagement.getUserID());
            stmt_4.executeUpdate();

            conn.commit();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
