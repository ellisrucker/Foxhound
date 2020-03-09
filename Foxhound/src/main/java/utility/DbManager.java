package utility;

import java.io.IOException;
import java.sql.*;

import static utility.MySQL.*;

public class DbManager {

    private static String url;
    private static String user;
    private static String password;

    //Constructor


    public static Connection openConnection() throws SQLException {
        Connection dbConnection = DriverManager.getConnection(url, user, password);
        //Set autocommit to false to allow for Rollbacks
        dbConnection.setAutoCommit(false);
        return dbConnection;
    }

    public static void configure() throws IOException {
        url = Configuration.getInstance().getUrl();
        user = Configuration.getInstance().getUser();
        password = Configuration.getInstance().getPassword();
    }

    public static void initializeTables() throws SQLException {
        Connection dbConnection = openConnection();

        try {
            initializeCaseTable(dbConnection);
            initializeTestTable(dbConnection);
            initializeSampleTable(dbConnection);
            initializePatientTable(dbConnection);
            initializeGenotypeTable(dbConnection);
            initializePlasmaTable(dbConnection);
            initializeFilteredTable(dbConnection);
            initializeHashTable(dbConnection);
            initializeLogTable(dbConnection);
            initializeErrorTable(dbConnection);
        } finally {
            dbConnection.close();
        }
    }

    //Database Table initialization
    private static void initializeCaseTable(Connection dbConnection) throws SQLException {
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropCaseTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createCaseTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();

    }
    private static void initializeTestTable(Connection dbConnection) throws SQLException {
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropTestTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createTestTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }
    private static void initializeSampleTable(Connection dbConnection) throws SQLException{
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropSampleTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createSampleTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }
    private static void initializePatientTable(Connection dbConnection) throws SQLException{
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropPatientTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createPatientTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }
    private static void initializeGenotypeTable(Connection dbConnection) throws SQLException{
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropGenotypeTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createGenotypeTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }
    private static void initializePlasmaTable(Connection dbConnection) throws SQLException{
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropPlasmaTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createPlasmaTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }
    private static void initializeFilteredTable(Connection dbConnection) throws SQLException{
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropFilteredTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createFilteredTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }
    private static void initializeHashTable(Connection dbConnection) throws SQLException{
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropHashTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createHashTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }
    private static void initializeLogTable(Connection dbConnection) throws SQLException{
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropLogTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createLogTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }
    private static void initializeErrorTable(Connection dbConnection) throws SQLException {
        PreparedStatement dropStmt = dbConnection.prepareStatement(dropErrorTable);
        PreparedStatement createStmt = dbConnection.prepareStatement(createErrorTable);
        dropStmt.executeUpdate();
        createStmt.executeUpdate();
    }

}
