package readwrite;

public class MySQL {

    //
    private static final String database = "rvdbtest";

    //Table & Column Names

    //Case
    private static final String caseTable = "case";
    private static final String caseID = "caseID";
    private static final String motherLastName = "motherLastName";
    private static final String motherFirstName = "motherFirstName";
    private static final String dateStarted = "dateStarted";
    private static final String gender = "gender";
    private static final String twins = "twins";
    private static final String rowHash = "rowHash";

    //Sample
    private static final String sampleTable = "sample";
    private static final String sampleID = "sampleID";
    private static final String dateReceived = "dateReceived";

    //Patient
    private static final String patientTable = "patient";
    private static final String patientID = "patientID";
    private static final String lastName = "lastName";
    private static final String firstName = "firstName";
    private static final String relationship = "relationship";

    //Test
    private static final String testTable = "test";
    private static final String testID = "testID";
    private static final String testStartDate = "testStartDate";
    private static final String testType = "testType";
    private static final String cost = "cost";
    private static final String gestation = "gestation";
    private static final String legal = "legal";

    //Drop Table
    public static String dropCaseTable = "DROP TABLE "+ database +"."+ caseTable;
    public static String dropSampleTable = "DROP TABLE "+ database +"."+ sampleTable;
    public static String dropPatientTable = "DROP TABLE "+ database +"."+ patientTable;
    public static String dropTestTable = "DROP TABLE "+ database + "." + testTable;

    //Create Table
    public static String createCaseTable = "CREATE TABLE "+ database +"."+ caseTable +" (" +
            caseID +" varchar(16) NOT NULL, " +
            motherLastName +" varchar(45) DEFAULT NULL, " +
            motherFirstName +" varchar(45) DEFAULT NULL, " +
            dateStarted +" date DEFAULT NULL, " +
            gender +" varchar(16) DEFAULT NULL, " +
            twins +" tinyint(1) DEFAULT '0' COMMENT '0 = no twins\\n1 = twins', " +
            rowHash +" int DEFAULT NULL, " +
            "PRIMARY KEY ("+ caseID +")" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createSampleTable = "CREATE TABLE "+ database +"."+ sampleTable +" (" +
            sampleID +" varchar(16) NOT NULL, " +
            dateReceived +" date DEFAULT NULL, " +
            testID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            patientID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            "PRIMARY KEY ("+ sampleID +")" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createPatientTable = "CREATE TABLE " + database +"."+ patientTable +" (" +
            patientID +" varchar(16) NOT NULL, " +
            lastName +" varchar(45) DEFAULT NULL, " +
            firstName +" varchar(45) DEFAULT NULL, " +
            relationship +" varchar(4) DEFAULT NULL, " +
            "PRIMARY KEY ("+ patientID +")" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createTestTable = "CREATE TABLE " + database +"." + testTable + " (" +
            testID +" varchar(16) NOT NULL, " +
            testStartDate +" date DEFAULT NULL, " +
            testType +" varchar(16) DEFAULT NULL, " +
            cost +" int DEFAULT NULL, " +
            gestation +" int DEFAULT NULL, " +
            legal +" tinyint(1) DEFAULT '1' COMMENT '0 = non-legal test\\n1 = legal test', " +
            caseID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            "PRIMARY KEY ("+ testID + ")" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

    //Insert Into Table
    //Parameterized Statements: Update DTOs if columns are added, removed, or switched!
    public static String insertCase = "INSERT INTO "+ database +"."+ caseTable +" (" +
            caseID +", " +
            motherLastName +", " +
            motherFirstName +", " +
            dateStarted +", " +
            gender +", " +
            twins +", " +
            rowHash +") VALUES(" +
            "?,?,?,?,?,?,?)";
    public static String insertSample = "INSERT INTO "+ database +"."+ sampleTable +" (" +
            sampleID +", " +
            dateReceived +", " +
            testID +", " +
            patientID +") VALUES("+
            "?,?,?,?)";
    public static String insertPatient = "INSERT INTO " + database +"."+ patientTable +" (" +
            patientID + ", " +
            lastName +", " +
            firstName +", " +
            relationship + ") VALUES(" +
            "?,?,?,?)";
    public static String insertTest = "INSERT INTO " + database +"." + testTable +" (" +
            testID + ", " +
            testStartDate + ", " +
            testType + ", " +
            cost + ", " +
            gestation + ", " +
            legal + ", " +
            caseID + ") VALUES(" +
            "?,?,?,?,?,?,?)";


    //Select From Table
    public static String selectCaseByID = "SELECT * FROM "+ database +"."+ caseTable +" WHERE "+ caseID +" = ? LIMIT 1";

}
