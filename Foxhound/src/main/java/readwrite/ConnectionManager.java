package readwrite;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;

public class ConnectionManager {

    private static final String url = "jdbc:mysql://localhost:3306/MySQL";
    private static final String user = "root";
    private static final String password = "Ravgen4!";

    public static void initializeCaseTable() throws SQLException {
        String dropTableQuery = "DROP TABLE rvdbtest.case";
        String createTableQuery = "CREATE TABLE rvdbtest.case (\n" +
                "  `caseID` varchar(16) NOT NULL,\n" +
                "  `motherLastName` varchar(45) DEFAULT NULL,\n" +
                "  `motherFirstName` varchar(45) DEFAULT NULL,\n" +
                "  `dateStarted` date DEFAULT NULL,\n" +
                "  `gender` varchar(16) DEFAULT NULL,\n" +
                "  `twins` tinyint(1) DEFAULT '0' COMMENT '0 = no twins\\n1 = twins',\n" +
                "  PRIMARY KEY (`caseID`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
        Connection connection = openConnection();
        try {
            PreparedStatement dropStmt = connection.prepareStatement(dropTableQuery);
            PreparedStatement createStmt = connection.prepareStatement(createTableQuery);
            dropStmt.executeUpdate();
            createStmt.executeUpdate();
        } finally {
            connection.close();
        }
    }

    public static Connection openConnection() throws SQLException {
        Connection dbConnection = DriverManager.getConnection(url, user, password);
        return dbConnection;
    }

}
