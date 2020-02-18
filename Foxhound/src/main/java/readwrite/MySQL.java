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
    private static final String source = "source";
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

    //Event
    private static final String eventTable = "event";
    private static final String genotypeTable = "genotype";
    private static final String plasmaTable = "plasma";
    private static final String eventID = "eventID";
    private static final String genotypeID = "genotypeID";
    private static final String plasmaID = "plasmaID";
    private static final String eventDate = "date";
    private static final String eventType = "type";
    private static final String primerSet = "primerSet";
    private static final String plasmaNumber = "plasmaNumber";
    private static final String performedBy = "performedBy";
    private static final String plasmaUsed = "plasmaUsed";
    private static final String plasmaGestation = "plasmaGestation";

    //Filtered
    private static final String filteredTable = "filtered";
    private static final String date = "date";
    private static final String motherName = "motherName";
    private static final String maternalPatientId = "maternalPatientId";
    private static final String paternalPatientId = "paternalPatientId";
    private static final String gestationGender = "gestationGender";
    private static final String testTypeCost = "testTypeCost";
    private static final String referral = "referral";
    private static final String genotypeA = "genotypeA";
    private static final String genotypeB = "genotypeB";
    private static final String firstDraw = "firstDraw";
    private static final String secondDraw = "secondDraw";
    private static final String thirdDraw = "thirdDraw";
    private static final String result = "result";
    private static final String confirmation = "confirmation";

    //ExcelHash
    private static final String hashTable = "hash";

    //Drop Table
    public static String dropCaseTable = "DROP TABLE "+ database +"."+ caseTable;
    public static String dropSampleTable = "DROP TABLE "+ database +"."+ sampleTable;
    public static String dropPatientTable = "DROP TABLE "+ database +"."+ patientTable;
    public static String dropTestTable = "DROP TABLE "+ database +"."+ testTable;
    public static String dropGenotypeTable = "DROP TABLE "+ database +"."+ genotypeTable;
    public static String dropPlasmaTable = "DROP TABLE "+ database +"."+ plasmaTable;
    public static String dropFilteredTable = "DROP TABLE "+ database +"."+ filteredTable;
    public static String dropHashTable = "DROP TABLE "+ database +"."+ hashTable;

    //Create Table
    public static String createCaseTable = "CREATE TABLE "+ database +"."+ caseTable +" (" +
            caseID +" varchar(16) NOT NULL, " +
            motherLastName +" varchar(45) DEFAULT NULL, " +
            motherFirstName +" varchar(45) DEFAULT NULL, " +
            dateStarted +" date DEFAULT NULL, " +
            gender +" varchar(16) DEFAULT NULL, " +
            twins +" tinyint(1) DEFAULT '0' COMMENT '0 = no twins\\n1 = twins', " +
            source +" varchar(45) DEFAULT NULL COMMENT 'Will eventually become SiteID', " +
            result +" varchar(45) DEFAULT NULL, " +
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
    public static String createGenotypeTable = "CREATE TABLE "+ database +"."+ genotypeTable+ " ("+
            genotypeID +" MEDIUMINT NOT NULL AUTO_INCREMENT, "+
            eventDate +" date NOT NULL, "+
            primerSet +" varchar(16) DEFAULT NULL, "+
            performedBy +" varchar(16) DEFAULT NULL, "+
            testID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            "PRIMARY KEY ("+ genotypeID + ")" +
            ") AUTO_INCREMENT = 1 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createPlasmaTable = "CREATE TABLE "+ database +"."+ plasmaTable+ " ("+
            plasmaID +" MEDIUMINT NOT NULL AUTO_INCREMENT, " +
            eventDate +" date NOT NULL, "+
            plasmaNumber +" varchar(16) DEFAULT NULL, "+
            plasmaUsed +" varchar(16) DEFAULT NULL, "+
            plasmaGestation +" int DEFAULT NULL, "+
            performedBy +" varchar(16) DEFAULT NULL, "+
            testID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            "PRIMARY KEY ("+ plasmaID + ")" +
            ") AUTO_INCREMENT = 1 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    //Set high upper limit for cell character count
    public static String createFilteredTable = "CREATE TABLE " + database +"."+ filteredTable + " (" +
            caseID +" varchar(100) NOT NULL, " +
            date +" varchar(100) DEFAULT NULL, "+
            motherName +" varchar(100) DEFAULT NULL, "+
            maternalPatientId +" varchar(100) DEFAULT NULL, "+
            paternalPatientId +" varchar(100) DEFAULT NULL, "+
            gestationGender +" varchar(100) DEFAULT NULL, "+
            testTypeCost +" varchar(100) DEFAULT NULL, "+
            referral +" varchar(100) DEFAULT NULL, "+
            genotypeA +" varchar(100) DEFAULT NULL, "+
            genotypeB +" varchar(100) DEFAULT NULL, "+
            firstDraw +" varchar(100) DEFAULT NULL, "+
            secondDraw +" varchar(100) DEFAULT NULL, "+
            thirdDraw +" varchar(100) DEFAULT NULL, "+
            result +" varchar(100) DEFAULT NULL, "+
            confirmation +" varchar(100) DEFAULT NULL, "+
            "PRIMARY KEY ("+ caseID +") " +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createHashTable = "CREATE TABLE " + database +"."+ hashTable + " (" +
            caseID +" varchar(100) NOT NULL, " +
            date +" int DEFAULT NULL, "+
            motherName +" int DEFAULT NULL, "+
            maternalPatientId +" int DEFAULT NULL, "+
            paternalPatientId +" int DEFAULT NULL, "+
            gestationGender +" int DEFAULT NULL, "+
            testTypeCost +" int DEFAULT NULL, "+
            referral +" int DEFAULT NULL, "+
            genotypeA +" int DEFAULT NULL, "+
            genotypeB +" int DEFAULT NULL, "+
            firstDraw +" int DEFAULT NULL, "+
            secondDraw +" int DEFAULT NULL, "+
            thirdDraw +" int DEFAULT NULL, "+
            result +" int DEFAULT NULL, "+
            confirmation +" int DEFAULT NULL, "+
            "PRIMARY KEY ("+ caseID +") " +
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
            source +", " +
            result +", " +
            rowHash +") VALUES(" +
            "?,?,?,?,?,?,?,?,?)";
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
    public static String insertGenotype = "INSERT INTO " + database + "."+ genotypeTable + " (" +
            eventDate +", "+
            primerSet +", "+
            performedBy +", "+
            testID +") VALUES(" +
            "?,?,?,?)";
    public static String insertPlasma = "INSERT INTO " + database + "."+ plasmaTable + " (" +
            eventDate +", "+
            plasmaNumber +", "+
            plasmaUsed +", "+
            plasmaGestation +", "+
            performedBy +", "+
            testID +") VALUES(" +
            "?,?,?,?,?,?)";
    public static String insertFilteredCase = "INSERT INTO " + database +"." + filteredTable +" (" +
            caseID + ", " +
            date + ", " +
            motherName + ", " +
            maternalPatientId + ", " +
            paternalPatientId + ", " +
            gestationGender + ", " +
            testTypeCost + ", " +
            referral + ", " +
            genotypeA + ", " +
            genotypeB + ", " +
            firstDraw + ", " +
            secondDraw + ", " +
            thirdDraw + ", " +
            result + ", " +
            confirmation + ") VALUES(" +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static String insertHash = "INSERT INTO " + database +"." + hashTable +" (" +
            caseID + ", " +
            date + ", " +
            motherName + ", " +
            maternalPatientId + ", " +
            paternalPatientId + ", " +
            gestationGender + ", " +
            testTypeCost + ", " +
            referral + ", " +
            genotypeA + ", " +
            genotypeB + ", " +
            firstDraw + ", " +
            secondDraw + ", " +
            thirdDraw + ", " +
            result + ", " +
            confirmation + ") VALUES(" +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


    //Select From Table
    public static String selectCaseByID = "SELECT * FROM "+ database +"."+ caseTable +" WHERE "+ caseID +" = ? LIMIT 1";
    public static String selectFilteredCaseByID = "SELECT * FROM "+ database +"."+ filteredTable + " WHERE "+ caseID +" = ? LIMIT 1";
    public static String selectHashByID = "SELECT * FROM "+ database +"."+ hashTable + " WHERE "+ caseID +" = ? LIMIT 1";
    public static String selectSampleIDsByTestID = "SELECT "+ sampleID +" FROM "+ database +"."+ sampleTable +" WHERE "+ testID + " = ?";

    //Update
    public static String updateCaseResult = "UPDATE "+ database +"."+ caseTable +" SET " + result + " = ? WHERE "+ caseID + " = ?";
    public static String updateMotherName = "UPDATE "+ database +"."+ caseTable +" SET " + motherLastName +" = ?, "+ motherFirstName +" = ? WHERE "+ caseID + " = ?";
    public static String updateGestation = "UPDATE "+ database +"."+ testTable +" SET " + gestation + " = ? WHERE "+ testID + " = ?";
    public static String updateGender = "UPDATE "+ database +"."+ caseTable +" SET " + gender + " = ? WHERE "+ caseID + " = ?";
    public static String updateTestTypeCost = "UPDATE "+ database +"."+ testTable +" SET " + testType +" = ?, "+ cost +" = ? WHERE "+ testID + " = ?";
    public static String updateSource = "UPDATE "+ database +"."+ caseTable +" SET " + source + " = ? WHERE "+ caseID + " = ?";
    public static String updateSamplePatientID = "UPDATE "+ database +"."+ sampleTable +" SET " + patientID + " = ? WHERE "+ sampleID + " = ?";

    //Delete
    public static String deleteSample = "DELETE FROM "+ database +"."+ sampleTable +" WHERE "+ sampleID +" =?";
    public static String deletePatient = "DELETE "+ database +"."+ patientTable +
            " FROM "+ database +"."+ sampleTable + " INNER JOIN " + database +"."+ patientTable +
            " ON " + sampleTable +"."+ patientID +" = "+ patientTable +"."+ patientID +
            " WHERE "+ sampleTable +"." +testID +" =?";
}
