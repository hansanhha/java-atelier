package hansanhha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqlInjectionQuery {

    public void getUserByUsername(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "password");
             Statement stmt = conn.createStatement()) {

            // sql injection 취약점 (사용자 입력이 직접 sql에 포함됨)
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("User found: " + rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
