package TransactionManage.Accept;

import BankManage.BankManagement;
import Database.ConnectDB;
import TransactionManage.Authentication.Authentication;
import TransactionManage.Transaction.Transaction;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AcceptTransaction extends JFrame {
    public AcceptTransaction(JFrame frame) {
        JLabel label = new JLabel();
        label.setText("Bạn có muốn chuyển ?");
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

        JLabel trueData = new JLabel();
        trueData.setText("Bạn đã chuyển tiền thành công");
        trueData.setFont(new Font("Arial", Font.PLAIN, 18));
        trueData.setBounds(150,150,400,100);
        trueData.setVisible(false);

        JButton button2 = new JButton("Yes");
        button2.setBounds(300,100,50,30);
        button2.setFont(new Font("Times New Roman",Font.PLAIN,10));
        button2.setForeground(Color.WHITE);
        button2.setBackground(Color.BLACK);
        button2.setFocusPainted(false);
        button2.addActionListener(e -> {
            if(!checkTransaction()){
                trueData.setText("Số dư không khả dụng");
                trueData.setVisible(true);
            } else {
                new Authentication(this);
                this.setVisible(false);
                Authentication.buttons[10].addActionListener(ae -> {
                    if(Authentication.checkPIN()){
                        trueData.setVisible(true);
                        Transaction.updateData();
                        updateMoneyFromDB();
                        new BankManagement();
                        this.setVisible(false);
                        frame.setVisible(false);
                    } else {
                        this.setVisible(true);
                        trueData.setText("Mã PIN không chính xác");
                        trueData.setVisible(true);
                    }
                });
            }
        });

        this.setTitle("AcceptTransaction");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,300);
        this.setResizable(false);
        this.setLayout(null);
        this.add(label);
        this.add(button1);
        this.add(button2);
        this.add(trueData);
        this.setVisible(true);
    }
    public static boolean checkTransaction(){
        return Transaction.getTransactionMoney() < BankManagement.getMoney();
    }
    public void updateMoneyFromDB(){
        String sql = "UPDATE bankaccount SET money=? where userID=?";
        String sql_2 = "UPDATE bankaccount SET money=? where userID=?";
        String sql_3 = "SELECT money FROM bankaccount WHERE userID=?";
        int money = 0;
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement stmt_2 = conn.prepareStatement(sql_2);
             PreparedStatement stmt_3 = conn.prepareStatement(sql_3);){
            stmt.setInt(1, BankManagement.getMoney() - Transaction.getTransactionMoney());
            stmt.setString(2,BankManagement.getUserID());
            stmt.executeUpdate();

            stmt_3.setString(1, Transaction.getUserID());
            ResultSet rs = stmt_3.executeQuery();
            if(rs.next()){
                money = rs.getInt("money");
            }

            stmt_2.setInt(1,money + Transaction.getTransactionMoney());
            stmt_2.setString(2, Transaction.getUserID());
            stmt_2.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
