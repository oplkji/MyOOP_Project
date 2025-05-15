package BankManage;

import Database.ConnectDB;
import Login.Login;
import TransactionManage.Transaction.*;
import TransactionManage.Recharge.*;
import TransactionManage.Withdraw.*;
import LockUnlockAccount.LockAccount.*;
import History.*;
import Logout.*;
import PinRegister.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankManagement extends JFrame {
    private static String name;
    private static String userID;
    private static int money;
    public JLabel label1;
    public static JLabel label2;
    public BankManagement() {
        System.out.println(Login.getUsername());
        this.setMoneyFromDB();
        this.setNameFromDB();

        ImageIcon image = new ImageIcon("Image/bank.jpg");
        Image resized = image.getImage().getScaledInstance(1000,1000,Image.SCALE_SMOOTH);
        ImageIcon newImage = new ImageIcon(resized);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(200,300,600,300);

        JLabel label = new JLabel(newImage);
        label.setBounds(0,0,1000,1000);

        label1 = new JLabel();
        label1.setText("User: " + this.name);
        label1.setOpaque(false);
        label1.setBounds(200,100,400,30);
        label1.setHorizontalTextPosition(JLabel.LEFT);
        label1.setFont(new Font("Arial", Font.PLAIN, 18));

        label2 = new JLabel();
        label2.setOpaque(false);
        label2.setText("Tổng số dư VND: " + this.money);
        label2.setBounds(200,130,400,50);
        label2.setHorizontalTextPosition(JLabel.LEFT);
        label2.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton button1 = new JButton();
        button1.setText("Chuyển tiền");
        button1.setBounds(70,70,150,30);
        button1.setFont(new Font("Arial", Font.PLAIN, 14));
        button1.setBackground(Color.LIGHT_GRAY);
        button1.setHorizontalTextPosition(JButton.CENTER);
        button1.setVerticalTextPosition(JButton.CENTER);
        button1.addActionListener(e -> {
           new Transaction(this);
        });

        JButton button2 = new JButton();
        button2.setText("Nạp tiền");
        button2.setBounds(270,70,100,30);
        button2.setFont(new Font("Arial", Font.PLAIN, 14));
        button2.setBackground(Color.LIGHT_GRAY);
        button2.setHorizontalTextPosition(JButton.CENTER);
        button2.setVerticalTextPosition(JButton.CENTER);
        button2.addActionListener(e -> {
            new Recharge(this);
        });

        JButton button3 = new JButton();
        button3.setText("Rút tiền");
        button3.setBounds(420,70,100,30);
        button3.setFont(new Font("Arial", Font.PLAIN, 14));
        button3.setBackground(Color.LIGHT_GRAY);
        button3.setHorizontalTextPosition(JButton.CENTER);
        button3.setVerticalTextPosition(JButton.CENTER);
        button3.addActionListener(e -> {
            new Withdraw(this);
        });

        JButton button4 = new JButton();
        button4.setText("Khóa tài khoản");
        button4.setBounds(70,150,150,30);
        button4.setFont(new Font("Arial", Font.PLAIN, 14));
        button4.setBackground(Color.LIGHT_GRAY);
        button4.setHorizontalTextPosition(JButton.LEFT);
        button4.setVerticalTextPosition(JButton.CENTER);
        button4.addActionListener(e -> {
           new LockAccount(this);
        });

        JButton button5 = new JButton();
        button5.setText("Lịch sử");
        button5.setBounds(270,150,100,30);
        button5.setFont(new Font("Arial", Font.PLAIN, 14));
        button5.setBackground(Color.LIGHT_GRAY);
        button5.setHorizontalTextPosition(JButton.CENTER);
        button5.setVerticalTextPosition(JButton.CENTER);
        button5.addActionListener(e -> {
           new History(this);
        });

        JButton button6 = new JButton();
        button6.setText("Đăng xuất");
        button6.setBounds(420,150,100,30);
        button6.setFont(new Font("Arial", Font.PLAIN, 14));
        button6.setBackground(Color.LIGHT_GRAY);
        button6.setHorizontalTextPosition(JButton.CENTER);
        button6.setVerticalTextPosition(JButton.CENTER);
        button6.addActionListener(e -> {
            new Logout(this);
        });

        JButton button7 = new JButton();
        button7.setText("Đăng kí mã PIN");
        button7.setBounds(250,230,150,30);
        button7.setFont(new Font("Arial", Font.PLAIN, 14));
        button7.setBackground(Color.LIGHT_GRAY);
        button7.setHorizontalTextPosition(JButton.CENTER);
        button7.setVerticalTextPosition(JButton.CENTER);
        button7.addActionListener(e -> {
           new PIN_Register();
        });

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(button5);
        panel.add(button6);
        panel.add(button7);

        this.setTitle("Bank Management");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000,1000);
        this.setResizable(false);
        this.add(panel);
        this.add(label1);
        this.add(label2);
        this.add(label);
        this.setLayout(null);
        this.setVisible(true);
    }
    public static String getUserName(){
        return name;
    }
    public void setUserName(String name){
        this.name = name;
    }
    public static String getUserID(){
        return userID;
    }
    public static int getMoney(){
        return money;
    }
    public void setMoney(int money){
        this.money = money;
    }
    public void setMoneyFromDB(){
        String sql = "SELECT money From bankaccount where userID = ?";
        String sql_2 = "SELECT userID From login where username = ?";
        try(Connection connect = ConnectDB.getConnection();
            PreparedStatement ps = connect.prepareStatement(sql);
            PreparedStatement ps2 = connect.prepareStatement(sql_2);){
            connect.setAutoCommit(false);
            ps2.setString(1,Login.getUsername());
            ResultSet rs2 = ps2.executeQuery();
            if(rs2.next()){
                userID = rs2.getString("userID");
            }
            ps.setString(1,userID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                this.setMoney(rs.getInt("money"));
                System.out.println(rs.getInt("money"));
            }
            connect.commit();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void setNameFromDB(){
        String sql = "SELECT username From bankaccount where userID = ?";
        String sql_2 = "SELECT userID From login where username = ?";
        try(Connection connect = ConnectDB.getConnection();
            PreparedStatement ps = connect.prepareStatement(sql);
            PreparedStatement ps2 = connect.prepareStatement(sql_2);){
            connect.setAutoCommit(false);
            ps2.setString(1,Login.getUsername());
            ResultSet rs_2 = ps2.executeQuery();
            if(rs_2.next()){
                userID = rs_2.getString("userID");
            }
            ps.setString(1,userID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                this.setUserName(rs.getString("username"));
                System.out.println(rs.getString("username"));
            }
            connect.commit();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
