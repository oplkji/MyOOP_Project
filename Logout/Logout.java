package Logout;

import BankManage.BankManagement;
import Database.ConnectDB;
import Login.Login;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Logout extends JFrame {
    public Logout(JFrame BankManagement) {
        JLabel label = new JLabel();
        label.setText("Bạn có muốn đăng xuất ?");
        label.setBounds(130,50,250,30);
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
            BankManagement.dispose();
            logout();
            new Login();
        });

        this.setTitle("Logout");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,300);
        this.setResizable(false);
        this.setLayout(null);
        this.add(label);
        this.add(button1);
        this.add(button2);
        this.setVisible(true);
    }
    public void logout() {
        String sql = "UPDATE login SET logoutAt=? WHERE userID=?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, BankManagement.getUserID());
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
