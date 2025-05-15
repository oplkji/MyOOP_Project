package Login;

import BankManage.BankManagement;
import Database.ConnectDB;
import LockUnlockAccount.UnlockAccount.UnlockAccount;
import UpdatePassword.UpdatePassWord;
import SignUp.SignUp;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class Login extends JFrame {
    private static String username;
    private char[] password;
    public Login(){
        JTextField textField_1 = new JTextField();
        textField_1.setBounds(380, 255, 250,20);

        JPasswordField textField_2 = new JPasswordField();
        textField_2.setBounds(380, 285, 250,20);
        textField_2.setEchoChar('*');

        ImageIcon image = new ImageIcon("Image/login.jpg");
        Image resized = image.getImage().getScaledInstance(1000,1000,Image.SCALE_SMOOTH);
        ImageIcon newImage = new ImageIcon(resized);

        ImageIcon wrongIcon = new ImageIcon("Image/wrongIcon.png");
        Image wrong = wrongIcon.getImage().getScaledInstance(30,20,Image.SCALE_SMOOTH);
        ImageIcon getWrong = new ImageIcon(wrong);

        JLabel getImage = new JLabel();
        getImage.setIcon(newImage);
        getImage.setBounds(0, 0, 1000,1000);

        JLabel noData = new JLabel();
        noData.setText("Vui lòng điền đầy đủ thông tin");
        noData.setFont(new Font("Arial", Font.PLAIN, 20));
        noData.setIcon(getWrong);
        noData.setBounds(350,100,350,100);
        noData.setVisible(false);

        JButton button = new JButton("Đăng nhập");
        button.setBounds(380, 330, 250, 50);
        button.setBackground(new Color(173,216,230));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            username = textField_1.getText();
            password = textField_2.getPassword();
            if(username.isEmpty() || this.getPassword().isEmpty()){
                noData.setVisible(true);
            } else {
                System.out.println(username);
                if(this.checkUser(username,this.getPassword()) && !this.checkLockAccount()){
                    this.setVisible(false);
                    this.setLocalDateTime();
                    new BankManagement();
                } else if(!this.checkUser(username,this.getPassword())){
                    System.out.println("Wrong data");
                    noData.setText("Tên đăng nhập hoặc mật khẩu sai");
                    noData.setVisible(true);
                } else if(this.checkLockAccount()){
                    noData.setText("Tài khoản của bạn đã bị khóa");
                    noData.setVisible(true);
                }
            }
        });

        JButton button2 = new JButton("Quên mật khẩu");
        button2.setBounds(380,400,250,50);
        button2.setBackground(new Color(173,216,230));
        button2.setForeground(Color.WHITE);
        button2.setFont(new Font("Arial", Font.PLAIN, 20));
        button2.setFocusPainted(false);
        button2.addActionListener(e -> {
            this.setVisible(false);
            new UpdatePassWord();
        });

        JButton button3 = new JButton("Đăng kí");
        button3.setBounds(380,470,250,50);
        button3.setBackground(new Color(173,216,230));
        button3.setForeground(Color.WHITE);
        button3.setFont(new Font("Arial", Font.PLAIN, 20));
        button3.setFocusPainted(false);
        button3.addActionListener(e -> {
            this.setVisible(false);
            new SignUp();
        });

        JButton button4 = new JButton("Mở khóa tài khoản");
        button4.setBounds(380,540,250,50);
        button4.setBackground(new Color(173,216,230));
        button4.setForeground(Color.WHITE);
        button4.setFont(new Font("Arial", Font.PLAIN, 20));
        button4.setFocusPainted(false);
        button4.addActionListener(e -> {
           this.setVisible(false);
           new UnlockAccount();
        });

        JLabel label = new JLabel();
        label.setOpaque(false);
        label.setText("Login to Account");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBounds(380,200,250,250);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);

        JLabel label2 = new JLabel();
        label2.setOpaque(false);
        label2.setText("Username:");
        label2.setBounds(180,250,100,30);
        label2.setFont(new Font("Times New Roman",Font.PLAIN,16));
        label2.setVerticalTextPosition(JLabel.BOTTOM);

        JLabel label3 = new JLabel();
        label3.setOpaque(false);
        label3.setText("Password:");
        label3.setBounds(180,280,100,30);
        label3.setFont(new Font("Times New Roman",Font.PLAIN,16));

        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000,1000);
        this.setResizable(false);
        this.setLayout(null);
        this.add(label);
        this.add(label2);
        this.add(label3);
        this.add(noData);
        this.add(textField_1);
        this.add(textField_2);
        this.add(button);
        this.add(button2);
        this.add(button3);
        this.add(button4);
        this.add(getImage);
        this.setVisible(true);
    }
    public static String getUsername(){
        return username;
    }
    public String getPassword(){
        String pass = "";
        for(char i : this.password){
            pass += i;
        }
        return pass;
    }
    public boolean checkUser(String username, String password){
        String sql = "SELECT username, password FROM login WHERE username = ?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,this.getUsername());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                System.out.println("Success");
                return username.equals(rs.getString("username")) && password.equals(rs.getString("password"));
            } else {
                System.out.println("Fail");
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public void setLocalDateTime(){
        String sql = "UPDATE login SET loginAt=? WHERE username=?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            conn.setAutoCommit(false);
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, this.getUsername());
            stmt.executeUpdate();
            conn.setAutoCommit(true);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public boolean checkLockAccount(){
        String sql = "SELECT userID FROM signup WHERE username = ?";
        String sql_2 = "SELECT * FROM lockaccount WHERE userID=?";
        String userID = "";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt_2 = conn.prepareStatement(sql_2);){
            stmt.setString(1,this.getUsername());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                userID = rs.getString("userID");
            }

            stmt_2.setString(1,userID);
            ResultSet rs_2 = stmt_2.executeQuery();
            if(rs_2.next()) {
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
