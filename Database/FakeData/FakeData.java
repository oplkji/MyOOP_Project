package Database;

import com.github.javafaker.Faker;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FakeData {
    public FakeData() {
        Faker faker = new Faker();
        Random rand = new Random();

        String shuffled = "";
        ArrayList<String> userID = new ArrayList<String>();
        ArrayList<String> password = new ArrayList<>();
        for(int i = 0;i < 100;i++){
            String lower = faker.lorem().characters(6, false);
            char upper = (char) ('A' + rand.nextInt(26));
            int digit = rand.nextInt(10);

            String rawPassword = lower + upper + digit;
            shuffled = shuffleString(rawPassword);
            password.add(shuffled);
        }
        String sql = "INSERT INTO bankaccount (userID, username, money) VALUES (?,?,?)";
        String sql_2 = "SELECT username, password,phoneNumber,email,birthDate,name FROM signup WHERE userID=?";
        String sql_3 = "INSERT INTO login (userID, username, password,loginAt, logoutAt) VALUES (?,?,?,?,?)";
        String sql_4 = "INSERT INTO signup (userID, username, password,phoneNumber,email,birthDate,name) VALUES (?,?,?,?,?,?,?)";
        String sql_5 = "INSERT INTO updatepassword (userID, phoneNumber, username, oldpassword, newpassword) VALUES (?,?,?,?,?)";
        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt_2 = conn.prepareStatement(sql_2);
            PreparedStatement stmt_3 = conn.prepareStatement(sql_3);
            PreparedStatement stmt_4 = conn.prepareStatement(sql_4);
            PreparedStatement stmt_5 = conn.prepareStatement(sql_5);){

            for(int i = 0;i < 10;i++){
                String fakeID = faker.bothify("USR######");
                stmt_4.setString(1, fakeID);
                stmt_4.setString(2, faker.name().username());
                stmt_4.setString(3, password.get(rand.nextInt(password.size())));
                stmt_4.setString(4, faker.phoneNumber().cellPhone());
                stmt_4.setString(5,faker.internet().emailAddress());
                stmt_4.setDate(6, new java.sql.Date(faker.date().birthday(18,50).getTime()));
                stmt_4.setString(7,faker.name().fullName());
                stmt_4.executeUpdate();
                userID.add(fakeID);
            }
            for(int i = 0;i < userID.size();i++){
                String USERID = userID.get(i);
                stmt_2.setString(1, USERID);
                ResultSet rs_2 = stmt_2.executeQuery();
                if(rs_2.next()){
                    stmt.setString(1, userID.get(i));
                    stmt.setString(2, rs_2.getString("name"));
                    stmt.setInt(3, Integer.parseInt(faker.number().digits(6)));
                    stmt.executeUpdate();

                    stmt_3.setString(1, USERID);
                    stmt_3.setString(2,rs_2.getString("username"));
                    stmt_3.setString(3,rs_2.getString("password"));
                    stmt_3.setDate(4,new java.sql.Date(faker.date().past(10, TimeUnit.DAYS).getTime()));
                    stmt_3.setDate(5,new java.sql.Date(faker.date().future(9,TimeUnit.DAYS).getTime()));
                    stmt_3.executeUpdate();

                    stmt_5.setString(1, USERID);
                    stmt_5.setString(2,rs_2.getString("phoneNumber"));
                    stmt_5.setString(3,rs_2.getString("username"));
                    stmt_5.setString(4,rs_2.getString("password"));
                    stmt_5.setString(5,rs_2.getString("password"));
                    stmt_5.executeUpdate();
                }
            }
            conn.close();
            System.out.println("Add user successfully");
        } catch (SQLException e){
            e.printStackTrace();
        }
        for(int i = 0;i < userID.size();i++){
            System.out.println(userID.get(i));
        }
    }
    public static String shuffleString(String input) {
        ArrayList<Character> chars = new ArrayList<>();
        for (char c : input.toCharArray()) chars.add(c);
        Collections.shuffle(chars);
        StringBuilder output = new StringBuilder();
        for (char c : chars) output.append(c);
        return output.toString();
    }
}
