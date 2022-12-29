package jdbsdemo;


import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
/**
 jdbc - Java Database Connectivity
**/
public class Main {

    public static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    //    public static String DB_URL = "jdbc:mysql://root:******@localhost:3306/soft_uni"; - Not work with root and password integrated in URL, chek the problem!
    public static String DB_URL = "jdbc:mysql://localhost:3306/soft_uni";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter DB username (<Enter> for 'root'): ");
        String username = scanner.nextLine().trim();
        username = username.length() > 0 ? username : "root";

        System.out.println("Enter DB password (<Enter> for ''): ");
        String password = scanner.nextLine().trim();
        password = password.length() > 0 ? password : "";


        // 1. Load DB driver - Not necessarily!

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.printf("Database driver '%s' not found.%n", DB_DRIVER);
            System.exit(0);
        }

        System.out.println("DB Driver loaded successfully.");

        // 2. Connect to DB

        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        Connection connection = null;

        try {
//            DriverManager.getConnection(DB_URL, username, password);
//            DriverManager.getConnection(DB_URL); - Not work with root and password integrated in URL, chek the problem!
            connection = DriverManager.getConnection(DB_URL, props);
        } catch (SQLException throwables) {
            System.err.printf("Can not connect to DB: %s%n", DB_URL);
            System.exit(0);
        }

        System.out.printf("DB connection created successfully: %s%n", DB_URL);

        // 3. Read query params

        System.out.println("Enter minimal salary (<Enter> for '40000'): ");
        String salaryStr = scanner.nextLine().trim();
        salaryStr = salaryStr.length() > 0 ? salaryStr : "40000";

        double salary = 40000;

        try {
            salary = Double.parseDouble(salaryStr);
        } catch (NumberFormatException ex) {
            System.err.printf("Invalid number '%s'%n", salary);
        }

        // 4. Create prepared statement

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM employees WHERE salary > ?"); // ? - позиционен параметър. //
                                                                                                                // На негово място се задава стойност по на долу - ps.setDouble(1, salary);

            // 5. Execute statement with parameter

            ps.setDouble(1, salary);
            ResultSet rs = ps.executeQuery();

            // 6. Print results

            while (rs.next()) {
                System.out.printf("| %10d | %-15.15s | %-15.15s | %10.2f |%n",
                        rs.getLong("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDouble("salary")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // 7. Close connection

        try {
            connection.close();
        } catch (SQLException throwables) {
            System.err.printf("Error closing DB connection %s%n", throwables.getMessage());
        }

    }

}
