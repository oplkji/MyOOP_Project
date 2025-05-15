package UpdatePassword;

import Database.ConnectDB;
import Login.Login;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdatePassWord extends JFrame {
    private String newPass;
    private String userID;
    private String phoneNumber;
    private String userName;
    public UpdatePassWord(){
        ImageIcon image = new ImageIcon("Image/login.jpg");
        Image resized = image.getImage().getScaledInstance(1000,1000,Image.SCALE_SMOOTH);
        ImageIcon newImage = new ImageIcon(resized);

        JLabel getImage = new JLabel();
        getImage.setIcon(newImage);
        getImage.setBounds(0, 0, 1000,1000);

        JLabel label = new JLabel();
        label.setText("Update Password");
        label.setBounds(450,100, 250,40);
        label.setFont(new Font("Arial", Font.PLAIN, 30));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.CENTER);

        JLabel label1 = new JLabel();
        label1.setText("Cập nhật mật khẩu thành công");
        label1.setBounds(400,50,500,40);
        label1.setFont(new Font("Arial", Font.PLAIN, 25));
        label1.setHorizontalTextPosition(JLabel.CENTER);
        label1.setVerticalTextPosition(JLabel.CENTER);
        label1.setVisible(false);

        JLabel label2 = new JLabel();
        label2.setText("Số căn cước: ");
        label2.setBounds(300,170,250,30);
        label2.setFont(new Font("Arial", Font.PLAIN, 20));
        label2.setHorizontalTextPosition(JLabel.CENTER);
        label2.setVerticalTextPosition(JLabel.CENTER);

        JLabel label4 = new JLabel();
        label4.setText("Số điện thoại: ");
        label4.setBounds(300,220, 150,30);
        label4.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label4.setHorizontalTextPosition(JLabel.CENTER);
        label4.setVerticalTextPosition(JLabel.CENTER);

        JLabel label5 = new JLabel();
        label5.setText("Username: ");
        label5.setBounds(300,270, 150,30);
        label5.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label5.setHorizontalTextPosition(JLabel.CENTER);
        label5.setVerticalTextPosition(JLabel.CENTER);

        JLabel label6 = new JLabel();
        label6.setText("Password: ");
        label6.setBounds(300,320, 150,30);
        label6.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label6.setHorizontalTextPosition(JLabel.CENTER);
        label6.setVerticalTextPosition(JLabel.CENTER);

        JLabel label7 = new JLabel();
        label7.setText("Nhập lại password: ");
        label7.setBounds(300,370, 200,30);
        label7.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label7.setHorizontalTextPosition(JLabel.CENTER);
        label7.setVerticalTextPosition(JLabel.CENTER);

        JTextField[] textField = new JTextField[3];
        for(int i = 0; i<3; i++){
            textField[i] = new JTextField();
        }
        textField[0].setBounds(500,175, 150,20);
        textField[1].setBounds(500,225, 150,20);
        textField[2].setBounds(500,275, 150,20);

        JPasswordField pField_1 = new JPasswordField();
        pField_1.setBounds(500,325, 150,30);
        pField_1.setEchoChar('*');

        JPasswordField pField_2 = new JPasswordField();
        pField_2.setBounds(500,375, 150,30);
        pField_2.setEchoChar('*');

        JButton button = new JButton();
        button.setText("Update");
        button.setBounds(500, 420, 150, 30);
        button.setBackground(new Color(173,216,230));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            this.phoneNumber = textField[1].getText();
            this.userName = textField[2].getText();
            this.userID = textField[0].getText();
            System.out.println(this.phoneNumber);
            System.out.println(this.userName);
            if(!checkInfor()){
                label1.setBounds(300,50,700,40);
                label1.setText("Thông tin cập nhật không chính xác");
                label1.setVisible(true);
            } else {
                if(!this.password(pField_1.getPassword()).equals(this.password(pField_2.getPassword()))) {
                    label1.setText("Mật khẩu không trùng nhau!");
                    label1.setVisible(true);
                } else {
                    if(!this.checkPassword(this.password(pField_2.getPassword()))){
                        label1.setBounds(150,50,800,40);
                        label1.setText("Mật khẩu phải có 8 kí tự trở lên, ít nhất 1 kí tự số, 1 kí tự in hoa");
                        label1.setVisible(true);
                    } else {
                        label1.setText("Cập nhật mật khẩu thành công");
                        this.userID = textField[0].getText();
                        newPass = this.password(pField_1.getPassword());
                        label1.setVisible(true);
                        System.out.println(this.userID + " " + newPass);
                        this.setNewPassword();
                    }
                }
            }
        });

        JButton button1 = new JButton();
        button1.setText("Login");
        button1.setBounds(300, 420, 150, 30);
        button1.setBackground(new Color(173,216,230));
        button1.setForeground(Color.WHITE);
        button1.setFont(new Font("Arial", Font.PLAIN, 20));
        button1.setFocusPainted(false);
        button1.addActionListener(e -> {
            new Login();
            this.setVisible(false);
        });

        this.setTitle("Update Password");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000,1000);
        this.setResizable(false);
        this.setLayout(null);
        this.add(label);
        this.add(label1);
        this.add(label2);
        this.add(label4);
        this.add(label5);
        this.add(label6);
        this.add(label7);
        this.add(pField_1);
        this.add(pField_2);
        for(int i = 0; i<3; i++){
            this.add(textField[i]);
        }
        this.add(button);
        this.add(button1);
        this.add(getImage);
        this.setVisible(true);
    }
    public String password(char[] pass){
        String password = "";
        for(int i = 0; i<pass.length; i++){
            password += pass[i];
        }
        return password;
    }
    public boolean checkPassword(String password){
        boolean check = false, isNum = false;
        for(int i = 0;i < password.length();i++){
            if(password.charAt(i) >= 'A' && password.charAt(i) <= 'Z'){
                check = true;
                break;
            }
        }
        for(int i = 0;i < password.length();i++){
            if(password.charAt(i) >= '0' && password.charAt(i) <= '9'){
                isNum = true;
                break;
            }
        }
        return password.length() >= 8 && check && isNum;
    }
    public void setNewPassword(){
        String sql = "UPDATE updatepassword SET newpassword = ? WHERE userID=?";
        String sql_2 = "UPDATE login SET password = ? WHERE userID=?";
        String sql_3 = "UPDATE signup SET password = ? WHERE userID=?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt_2 = conn.prepareStatement(sql_2);
            PreparedStatement stmt_3 = conn.prepareStatement(sql_3);){
            conn.setAutoCommit(false);

            stmt.setString(1,this.newPass);
            stmt.setString(2,this.userID);
            stmt.executeUpdate();

            stmt_2.setString(1,this.newPass);
            stmt_2.setString(2,this.userID);
            stmt_2.executeUpdate();

            stmt_3.setString(1,this.newPass);
            stmt_3.setString(2,this.userID);
            stmt_3.executeUpdate();

            conn.commit();
            System.out.println("Cập nhật thành công");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public boolean checkInfor(){
        String sql = "SELECT * FROM updatepassword WHERE userID=?";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1,this.userID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                System.out.println(rs.getString("username"));
                System.out.println(rs.getString("phoneNumber"));
                return this.userName.equals(rs.getString("username")) && this.phoneNumber.equals(rs.getString("phoneNumber"));
            }
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
