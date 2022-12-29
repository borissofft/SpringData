import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Homework {

    public static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/";
    public static final String MINIONS_TABLE_NAME = "minions_db";
    private Connection connection;
    private BufferedReader reader;

    public Homework() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void setConnection(String user, String password) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        connection = DriverManager.getConnection(CONNECTION_STRING + MINIONS_TABLE_NAME, properties); // throws SQLException - added to method signature, not a good practise

    }

    public void getVillainsNamesEx2() throws SQLException {

        String query = "SELECT v.name, COUNT(DISTINCT mv.minion_id) AS 'count' FROM villains AS v\n" +
                "JOIN minions_villains AS mv ON v.id = mv.villain_id\n" +
                "GROUP BY v.id\n" +
                "HAVING count > 15\n" +
                "ORDER BY count DESC;";

        PreparedStatement statement = connection.prepareStatement(query); // throws SQLException
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d%n",
//                    resultSet.getString(1), // by number of the columns from the SELECT query
//                    resultSet.getInt(2));  // by number of the columns from the SELECT query
                    resultSet.getString("name"), // better by name!!!
                    resultSet.getInt("count"));  // better by name!!!
        }

    }

    public void getMinionNamesEx3() throws IOException, SQLException {

        System.out.println("Enter villain id:");
        int villainId = Integer.parseInt(reader.readLine());

        String villainName = getEntityNameById(villainId, "villains");

        if (villainName == null) {
            System.out.printf("No villain with ID %d exists in the database.", villainId);
            return;
        }

        System.out.printf("Villain: %s%n", villainName);

        String query = "SELECT m.name, m.age FROM minions AS m\n" +
                "JOIN minions_villains AS mv ON m.id = mv.minion_id\n" +
                "WHERE mv.villain_id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, villainId);

        ResultSet resultSet = statement.executeQuery();

        int counter = 1;
        while (resultSet.next()) {
            System.out.printf("%d. %s %d%n",
                    counter++,
                    resultSet.getString("name"),
                    resultSet.getInt("age"));
        }

    }

    private String getEntityNameById(int entityId, String tableName) throws SQLException {
        String query = String.format("SELECT name FROM %s WHERE id = ?", tableName);

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, entityId);

        ResultSet resultSet = statement.executeQuery();

        return resultSet.next() ? resultSet.getString("name") : null;
    }

    public void addMinionEx4() throws IOException, SQLException {
        System.out.println("Enter minions info by space: name, age, town name:");
        String[] minionInfo = reader.readLine().split("\\s+");
        System.out.println("Enter villain name:");
        String villainName = reader.readLine();
        String minionName = minionInfo[0];
        int age = Integer.parseInt(minionInfo[1]);
        String townName = minionInfo[2];

        // For town
        int townId = getEntityIdByName(townName, "towns");
        if (townId < 0) { // No such town
            insertEntityInTowns(townName);
            townId = getEntityIdByName(townName, "towns");
            System.out.printf("Town %s was added to the database.%n", townName);
        }

        // For minion
        int minionId = getEntityIdByName(minionName, "minions");
        if (minionId < 0) {
            insertEntityInMinions(minionName, age, townId);
            minionId = getEntityIdByName(minionName, "minions");
        }

        // For villain
        int villainId = getEntityIdByName(villainName, "villains");
        if (villainId < 0) {
            insertEntityInVillains(villainName);
            villainId = getEntityIdByName(villainName, "villains");
            System.out.printf("Villain %s was added to the database.%n", villainName);
        }

        String check_exist_relation = "SELECT villain_id FROM minions_villains WHERE minion_id = ?";
        PreparedStatement statement = connection.prepareStatement(check_exist_relation);
        statement.setInt(1, minionId);
        ResultSet resultSet = statement.executeQuery();
        int villain_id = resultSet.next() ? resultSet.getInt("villain_id") : -1; // set it to -1 if there isn't such pair in mapping table

        // Mapping table
        if (villain_id == -1) { // check if there is such minion - villain pair
            String query = "INSERT INTO minions_villains(minion_id, villain_id) VALUES(?,?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, minionId);
            statement.setInt(2, villainId);
            statement.execute();
            System.out.printf("Successfully added %s to be minion of %s.%n", minionName, villainName);
        }

    }

    private void insertEntityInVillains(String villainName) throws SQLException {
        String query = "INSERT INTO villains(name, evilness_factor) value(?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, villainName);
        statement.setString(2, "evil");
        statement.execute();
    }

    private void insertEntityInMinions(String minionName, int age, int townId) throws SQLException {
        String query = "INSERT INTO minions(name, age, town_id) VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, minionName);
        statement.setInt(2, age);
        statement.setInt(3, townId);
        statement.execute();
    }

    private void insertEntityInTowns(String townName) throws SQLException {
        String query = "INSERT INTO towns(name) VALUE(?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, townName);
        statement.execute();
    }

    private int getEntityIdByName(String entityName, String tableName) throws SQLException {
        String query = String.format("SELECT id FROM %s WHERE name = ?", tableName);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entityName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() ? resultSet.getInt("id") : -1;
    }

    public void changeTownNameCasingEx5() throws IOException, SQLException {
        System.out.println("Enter country name:");
        String countryName = reader.readLine();

        String query = "UPDATE towns SET name = UPPER(name) WHERE country = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, countryName);

        int townsAffected = statement.executeUpdate();

        if (townsAffected > 0) {
            System.out.printf("%d town names were affected.%n", townsAffected);
            String query2 = "SELECT name FROM towns WHERE country = ?";
            statement = connection.prepareStatement(query2);
            statement.setString(1, countryName);
            ResultSet resultSet = statement.executeQuery();

            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }

            System.out.println(names);

        } else {
            System.out.println("No town names were affected.");
        }

    }

    public void printAllMinionNamesEx7() throws SQLException {

        List<String> names = new ArrayList<>();
        String query = "SELECT name FROM minions";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            names.add(resultSet.getString("name"));
        }

        for (int i = 0; i < (names.size()) / 2; i++) {
            System.out.println(names.get(i));
            System.out.println(names.get(names.size() - 1 - i));
        }

    }


    public void increaseMinionsAgeEx8() throws IOException, SQLException {

        System.out.println("Enter minion IDs, separated by space:");
        int[] ids = Arrays.stream(reader.readLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();

        PreparedStatement statement;

        for (int id : ids) {
            String query = "UPDATE minions SET age = age + 1, name = LOWER(name) WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
        }

        String query2 = "SELECT name, age FROM minions";
        statement = connection.prepareStatement(query2);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.printf("%s %d%n",
                    resultSet.getString("name"),
                    resultSet.getInt("age"));
        }

    }

    public void increaseMinionsAgeWithProcedureEx9() throws IOException, SQLException {

        System.out.println("Enter minion id:");
        int minionId = Integer.parseInt(reader.readLine());

        String query = "CALL usp_get_older(?)";
        CallableStatement callableStatement = connection.prepareCall(query);
        callableStatement.setInt(1, minionId);
        callableStatement.execute();

        String query2 = "SELECT name, age FROM minions WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query2);
        statement.setInt(1, minionId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            System.out.printf("%s %d", resultSet.getString("name"), resultSet.getInt("age"));
        } else {
            System.out.println("No minion with this id. Please try ids from 1 to 50");
        }

    }

}

