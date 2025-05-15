package History;

import BankManage.BankManagement;
import Database.ConnectDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class History extends JFrame {
    private String[] columnNames;
    DefaultTableModel model;
    public History(JFrame frame) {
        JLabel label = new JLabel("Lịch sử giao dịch");
        label.setFont(new Font("Segoe UI", Font.BOLD, 25));
        label.setBounds(200,50,300,30);

        JButton button = new JButton("<");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        button.setBounds(100,50,50,30);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.addActionListener(e -> {
           this.setVisible(false);
           frame.setVisible(false);
           new BankManagement();
        });

        model = new DefaultTableModel();
       
        columnNames = new String[]{"ID", "Tên Người Gửi","Thời gian giao dịch","Số tiền giao dịch","Tên người nhận","Nội dung chuyển tiền"};
        for(String i : columnNames){
            model.addColumn(i);
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(204, 242, 250));
        table.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scrollPane.setBounds(100, 100, 800, 600);
        scrollPane.setVisible(true);

        showHistory();

        this.add(scrollPane);
        this.add(label);
        this.add(button);
        this.setTitle("Demo JTable");
        this.setSize(1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);
    }
    public void showHistory() {
        String sql = "SELECT * FROM history where userID=? OR received_userID=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1,BankManagement.getUserID());
            stmt.setString(2,BankManagement.getUserID());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                model.addRow(new Object[]{rs.getString(1),rs.getString(2),rs.getTimestamp(3),rs.getInt(4),rs.getString(5),rs.getString(6)});
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}