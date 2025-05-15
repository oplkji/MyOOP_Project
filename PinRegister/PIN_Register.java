package PinRegister;
import BankManage.BankManagement;
import Database.ConnectDB;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PIN_Register extends JFrame {
    JPasswordField passwordField;
    private static String PIN;
    public PIN_Register() {
        JLabel announce = new JLabel("Bạn đã đăng kí thành công");
        announce.setHorizontalAlignment(SwingConstants.CENTER);
        announce.setVerticalAlignment(SwingConstants.CENTER);
        announce.setFont(new Font("Arial", Font.PLAIN, 25));
        announce.setBounds(50,50,300,30);
        announce.setVisible(false);

        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setBounds(50,100,300,100);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 35));

        JButton[] buttons = new JButton[13];
        for(int i = 0; i < buttons.length; i++){
            buttons[i] = new JButton();
            buttons[i].setText(String.valueOf(i+1));
            buttons[i].setHorizontalAlignment(SwingConstants.CENTER);
            buttons[i].setVerticalAlignment(SwingConstants.CENTER);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            buttons[i].setFocusPainted(false);
        }
        buttons[0].setBounds(50,200,100,80);
        buttons[1].setBounds(150,200,100,80);
        buttons[2].setBounds(250,200,100,80);
        buttons[3].setBounds(50,280,100,80);
        buttons[4].setBounds(150,280,100,80);
        buttons[5].setBounds(250,280,100,80);
        buttons[6].setBounds(50,360,100,80);
        buttons[7].setBounds(150,360,100,80);
        buttons[8].setBounds(250,360,100,80);
        buttons[9].setText("0");
        buttons[9].setBounds(50,440,300,80);
        buttons[10].setText("Submit");
        buttons[10].setBounds(150,520,100,30);
        buttons[11].setText("x");
        buttons[11].setBounds(50,520,100,30);
        buttons[12].setText("Back");
        buttons[12].setBounds(250,520,100,30);

        for(int i = 0; i < buttons.length-3; i++){
            int j = i;
            buttons[i].addActionListener(e -> {
                String currentText = new String(passwordField.getPassword());
                passwordField.setText(currentText + buttons[j].getText());
                System.out.println(buttons[j].getText());
            });
        }

        buttons[10].addActionListener(e -> {
           if(new String(passwordField.getPassword()).length() > 6){
               announce.setText("Mã PIN phải dưới 6 kí tự");
               announce.setVisible(true);
           } else {
               announce.setText("Bạn đã đăng kí thành công");
               announce.setVisible(true);
               PIN = new String(passwordField.getPassword());
               setPinIntoDB();
           }
        });

        buttons[11].addActionListener(e -> {
           passwordField.setText("");
        });

        buttons[12].addActionListener(e -> {
            this.setVisible(false);
        });

        this.setTitle("PIN Register");
        this.setSize(400,600);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(announce);
        this.add(passwordField);
        for(int i = 0; i < buttons.length; i++){
            this.add(buttons[i]);
        }
        this.setResizable(false);
        this.setVisible(true);
    }
    public static String getPin(){
        return PIN;
    }
    public void setPinIntoDB(){
        String sql = "UPDATE signup SET PIN = ? WHERE userID = ?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1,new String(passwordField.getPassword()));
            stmt.setString(2, BankManagement.getUserID());
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
