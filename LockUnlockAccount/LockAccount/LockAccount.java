package LockUnlockAccount.LockAccount;

import BankManage.BankManagement;
import Database.ConnectDB;
import Login.Login;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LockAccount extends JFrame {
    public LockAccount(JFrame frame) {
        JLabel label = new JLabel();
        label.setText("Bạn có muốn khóa tài khoản ?");
        label.setBounds(100,50,300,30);
        label.setFont(new Font("Times New Roman",Font.PLAIN,20));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);

        JButton button1 = new JButton("No");
        button1.setBounds(150,100,50,30);
        button1.setFont(new Font("Times New Roman",Font.PLAIN,10));
        button1.setForeground(Color.WHITE);
        button1.setBackground(Color.BLACK);
        button1.setFocusPainted(false);
        button1.addActionListener(e -> {
            this.setVisible(false);
        });

        JButton button2 = new JButton("Yes");
        button2.setBounds(300,100,50,30);
        button2.setFont(new Font("Times New Roman",Font.PLAIN,10));
        button2.setForeground(Color.WHITE);
        button2.setBackground(Color.BLACK);
        button2.setFocusPainted(false);
        button2.addActionListener(e -> {
            this.dispose();
            frame.dispose();
            addLockAccount();
            new Login();
        });

        this.setTitle("LockAccount");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,300);
        this.setResizable(false);
        this.setLayout(null);
        this.add(label);
        this.add(button1);
        this.add(button2);
        this.setVisible(true);
    }
    public void addLockAccount() {
        String sql = "INSERT INTO lockaccount VALUES(?,?,?,?,?)";
        String sql_2 = "SELECT phoneNumber, email, PIN FROM signup WHERE userID=?";
        String phonenumber = "",email = "",PIN = "";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt_2 = conn.prepareStatement(sql_2);) {
            stmt_2.setString(1, BankManagement.getUserID());
            ResultSet rs = stmt_2.executeQuery();
            if(rs.next()) {
                phonenumber = rs.getString("phoneNumber");
                email = rs.getString("email");
                PIN = rs.getString("PIN");
            }
            stmt.setString(1,BankManagement.getUserID());
            stmt.setString(2,BankManagement.getUserName());
            stmt.setString(3,phonenumber);
            stmt.setString(4,email);
            stmt.setString(5,PIN);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
