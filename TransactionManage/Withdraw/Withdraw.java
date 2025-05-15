package TransactionManage.Withdraw;

import BankManage.BankManagement;
import Database.ConnectDB;
import Login.Login;
import TransactionManage.Authentication.Authentication;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class Withdraw extends JFrame {
    private static String count;
    private JTextField textField;
    public Withdraw(JFrame frame){
        ImageIcon wrongIcon = new ImageIcon("Image/wrongIcon.png");
        Image wrong = wrongIcon.getImage().getScaledInstance(30,20,Image.SCALE_SMOOTH);
        ImageIcon getWrong = new ImageIcon(wrong);

        JLabel label = new JLabel();
        label.setText("Nhập số tiền muốn rút:");
        label.setBounds(10, 100, 300, 30);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font("Times New Roman",Font.PLAIN,20));

        JLabel label1 = new JLabel();
        label1.setBounds(10, 50, 350, 30);
        label1.setIcon(getWrong);
        label1.setText("Số tiền rút lớn hơn tài khoản gốc");
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setVerticalAlignment(JLabel.CENTER);
        label1.setFont(new Font("Times New Roman",Font.PLAIN,20));
        label1.setVisible(false);

        textField = new JTextField();
        textField.setBounds(300, 100, 150, 30);

        JButton button = new JButton();
        button.setText("Submit");
        button.setBounds(220,150,100,30);
        button.setFont(new Font("Times New Roman",Font.PLAIN,20));
        button.addActionListener(e -> {
            if(!this.checkIsLarger()){
                label1.setVisible(true);
            } else {
                new Authentication(this);
                this.setVisible(false);
                Authentication.buttons[10].addActionListener(ae -> {
                    if(Authentication.checkPIN()){
                        count = textField.getText();
                        this.withDrawMoney();
                        new BankManagement();
                        frame.setVisible(false);
                        this.setVisible(false);
                    } else {
                        this.setVisible(true);
                        label1.setText("Mã PIN không chính xác");
                        label1.setVisible(true);
                    }
                });
            }
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
        this.add(button);
        this.add(button_1);
        this.add(label1);
        this.setVisible(true);
    }

    public boolean checkIsLarger(){
        String sql_2 = "SELECT userID FROM login WHERE username=?";
        String sql_3 = "SELECT money FROM bankaccount WHERE userID=?";
        String userID = "";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt_2 = conn.prepareStatement(sql_2);
            PreparedStatement stmt_3 = conn.prepareStatement(sql_3);){
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
                return rs2.getInt("money") > Integer.parseInt(textField.getText());
            }
            conn.commit();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void withDrawMoney(){
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
                count = Integer.toString(rs2.getInt("money") - Integer.parseInt(count));
                System.out.println(count);
            }

            stmt.setString(1,count);
            stmt.setString(2,userID);
            stmt.executeUpdate();

            stmt_4.setString(1,userID);
            stmt_4.setString(2,BankManagement.getUserName());
            stmt_4.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
            stmt_4.setInt(4,-Integer.parseInt(textField.getText()));
            stmt_4.setString(5,"admin");
            stmt_4.setString(6,"Số tiền rút: " + textField.getText());
            stmt_4.setString(7,"001");
            stmt_4.executeUpdate();

            conn.commit();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
