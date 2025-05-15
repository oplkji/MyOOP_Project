package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    static final String DB_URL = "jdbc:mysql://localhost:3306/bank_management";
    static final String USER = "root";
    static final String PASS = "";
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL,USER,PASS);
    }
}
