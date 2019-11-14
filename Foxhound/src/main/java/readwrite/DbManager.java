package readwrite;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;

import static readwrite.MySQL.*;

public class DbManager {

    private static final String url = "jdbc:mysql://localhost:3306/MySQL";
    private static final String user = "root";
    private static final String password = "Ravgen4!";

    public static Connection openConnection() throws SQLException {
        Connection dbConnection = DriverManager.getConnection(url, user, password);
        return dbConnection;
    }
    //TODO: Abstract method from initializations that takes drop & create strings
    //Database Table initialization
    public static void initializeCaseTable() throws SQLException {
        Connection connection = openConnection();
        try {
            PreparedStatement dropStmt = connection.prepareStatement(dropCaseTable);
            PreparedStatement createStmt = connection.prepareStatement(createCaseTable);
            try {
                dropStmt.executeUpdate();
                createStmt.executeUpdate();
            } catch (Exception e){
                createStmt.executeUpdate();
            }
        } finally {
            connection.close();
        }
    }
    public static void initializeSampleTable() throws SQLException{
        Connection connection = openConnection();
        try {
            PreparedStatement dropStmt = connection.prepareStatement(dropSampleTable);
            PreparedStatement createStmt = connection.prepareStatement(createSampleTable);
            try {
                dropStmt.executeUpdate();
                createStmt.executeUpdate();
            } catch (Exception e){
                createStmt.executeUpdate();
            }
        } finally {
            connection.close();
        }
    }
    public static void initializePatientTable() throws SQLException{
        Connection connection = openConnection();
        try {
            PreparedStatement dropStmt = connection.prepareStatement(dropPatientTable);
            PreparedStatement createStmt = connection.prepareStatement(createPatientTable);
            try {
                dropStmt.executeUpdate();
                createStmt.executeUpdate();
            } catch (Exception e){
                createStmt.executeUpdate();;
            }
        } finally {
            connection.close();
        }
    }
}
