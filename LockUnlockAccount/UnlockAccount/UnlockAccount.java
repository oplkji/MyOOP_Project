package LockUnlockAccount.UnlockAccount;

import Database.ConnectDB;
import Login.Login;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UnlockAccount extends JFrame {
    public JTextField[] textField;
    private static String name;
    private static char[] pass;
    private static String email;
    private static String phone_Number;
    private static String userID;
    public UnlockAccount(){
        ImageIcon image = new ImageIcon("Image/login.jpg");
        Image resized = image.getImage().getScaledInstance(1000,1000,Image.SCALE_SMOOTH);
        ImageIcon newImage = new ImageIcon(resized);

        JLabel getImage = new JLabel();
        getImage.setIcon(newImage);
        getImage.setBounds(0, 0, 1000,1000);

        JLabel label = new JLabel();
        label.setText("Unlock Account");
        label.setBounds(450,100, 250,40);
        label.setFont(new Font("Arial", Font.PLAIN, 30));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.CENTER);

        JLabel notify = new JLabel();
        notify.setText("Mở khóa thành công");
        notify.setBounds(450, 50,250,40);
        notify.setFont(new Font("Arial",Font.PLAIN, 25));
        notify.setHorizontalTextPosition(JLabel.CENTER);
        notify.setVerticalTextPosition(JLabel.CENTER);
        notify.setVisible(false);

        JLabel label1 = new JLabel();
        label1.setText("Họ và tên: ");
        label1.setBounds(300,150, 150,50);
        label1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label1.setHorizontalTextPosition(JLabel.CENTER);
        label1.setVerticalTextPosition(JLabel.CENTER);

        JLabel id = new JLabel();
        id.setText("Số CMND: ");
        id.setBounds(300,220, 150,30);
        id.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        id.setHorizontalTextPosition(JLabel.CENTER);
        id.setVerticalTextPosition(JLabel.CENTER);

        JLabel label3 = new JLabel();
        label3.setText("Email: ");
        label3.setBounds(300,270, 150,30);
        label3.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label3.setHorizontalTextPosition(JLabel.CENTER);
        label3.setVerticalTextPosition(JLabel.CENTER);

        JLabel label4 = new JLabel();
        label4.setText("Số điện thoại: ");
        label4.setBounds(300,320, 150,30);
        label4.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label4.setHorizontalTextPosition(JLabel.CENTER);
        label4.setVerticalTextPosition(JLabel.CENTER);

        JLabel label5 = new JLabel();
        label5.setText("Mã PIN: ");
        label5.setBounds(300,370, 150,30);
        label5.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label5.setHorizontalTextPosition(JLabel.CENTER);
        label5.setVerticalTextPosition(JLabel.CENTER);

        JPasswordField passField2 = new JPasswordField();
        passField2.setBounds(500,375, 200,20);
        passField2.setEchoChar('*');

        textField = new JTextField[4];
        for(int i = 0;i < 4;i++){
            textField[i] = new JTextField();
        }
        textField[0].setBounds(500,165,200,20);
        textField[1].setBounds(500,225,200,20);
        textField[2].setBounds(500,275,200,20);
        textField[3].setBounds(500,325,200,20);

        JButton button = new JButton("Đăng nhập");
        button.setBounds(300, 450, 180, 50);
        button.setBackground(new Color(173,216,230));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            this.setVisible(false);
            new Login();
        });

        JButton button2 = new JButton("Unlock");
        button2.setBounds(500,450,200,50);
        button2.setBackground(new Color(173,216,230));
        button2.setForeground(Color.WHITE);
        button2.setFont(new Font("Arial", Font.PLAIN, 20));
        button2.setFocusPainted(false);
        button2.addActionListener(e -> {
            name = textField[0].getText();
            userID = textField[1].getText();
            pass = passField2.getPassword();
            email = textField[2].getText();
            phone_Number = textField[3].getText();
            if(name.isEmpty() || userID.isEmpty() || this.getPassword(pass).isEmpty() || email.isEmpty() || phone_Number.isEmpty()){
                notify.setBounds(300, 50,500,40);
                notify.setText("Vui lòng điền đầy đủ thông tin");
                notify.setVisible(true);
            } else {
                if(!checkLockAccount()){
                    notify.setBounds(350, 50,500,40);
                    notify.setText("Thông tin không chính xác");
                    notify.setVisible(true);
                } else {
                    notify.setText("Mở khóa thành công");
                    notify.setVisible(true);
                    unlock();
                }
            }
        });

        this.setTitle("Sign Up Account");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000,1000);
        this.setResizable(false);
        this.setLayout(null);
        this.add(label);
        this.add(label1);
        this.add(id);
        this.add(notify);
        this.add(label3);
        this.add(label4);
        this.add(label5);
        this.add(passField2);
        for(JTextField i : textField){
            this.add(i);
        }
        this.add(button);
        this.add(button2);
        this.add(getImage);
        this.setVisible(true);
    }
    public String getName(){
        return name;
    }
    public String getPassword(char[] pass){
        String password = "";
        for(char i : pass){
            password += i;
        }
        return password;
    }
    public static String getUserID(){
        return userID;
    }
    public void unlock(){
        String sql = "DELETE FROM lockaccount WHERE userID = ?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);){
            stmt.setString(1,userID);
            stmt.executeUpdate();
            System.out.println("Them user thanh cong");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean checkLockAccount(){
        String sql = "SELECT * FROM lockaccount WHERE userID=?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,userID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                System.out.println("Success");
                return name.equals(rs.getString("username")) && phone_Number.equals(rs.getString("phoneNumber")) && email.equals(rs.getString("email")) && new String(pass).equals(rs.getString("PIN"));
            } else {
                System.out.println("Fail");
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
