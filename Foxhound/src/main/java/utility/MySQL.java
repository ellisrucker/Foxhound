package utility;

public class MySQL {

    //
    private static final String database = "rvdbtest";

    //Table & Column Names

    //Case
    private static final String caseTable = database +"."+"case";
    private static final String caseID = "caseID";
    private static final String motherLastName = "motherLastName";
    private static final String motherFirstName = "motherFirstName";
    private static final String dateStarted = "dateStarted";
    private static final String gender = "gender";
    private static final String twins = "twins";
    private static final String source = "source";
    private static final String rowHash = "rowHash";

    //Sample
    private static final String sampleTable = database +"."+"sample";
    private static final String sampleID = "sampleID";
    private static final String dateReceived = "dateReceived";

    //Patient
    private static final String patientTable = database +"."+"patient";
    private static final String patientID = "patientID";
    private static final String lastName = "lastName";
    private static final String firstName = "firstName";
    private static final String relationship = "relationship";

    //Test
    private static final String testTable = database +"."+"test";
    private static final String testID = "testID";
    private static final String testStartDate = "testStartDate";
    private static final String testType = "testType";
    private static final String cost = "cost";
    private static final String gestation = "gestation";
    private static final String legal = "legal";

    //Procedures
    private static final String genotypeTable = database +"."+"genotype";
    private static final String plasmaTable = database +"."+"plasma";
    private static final String genotypeID = "genotypeID";
    private static final String plasmaID = "plasmaID";
    private static final String eventDate = "date";
    private static final String primerSet = "primerSet";
    private static final String plasmaNumber = "plasmaNumber";
    private static final String performedBy = "performedBy";
    private static final String plasmaUsed = "plasmaUsed";
    private static final String plasmaGestation = "plasmaGestation";

    //Filtered
    private static final String filteredTable = database +"."+"filtered";
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
    private static final String hashTable = database +"."+"hash";

    //Log
    private static final String logTable = database +"."+"log";
    private static final String dateUpdated = "dateUpdated";
    private static final String fileName = "fileName";
    private static final String logID = "logID";

    //Error
    private static final String errorTable = database +"."+ "error";
    private static final String errorID = "errorID";
    private static final String stackTrace = "stackTrace";

    //Drop Table
    public static String dropCaseTable = "DROP TABLE IF EXISTS "+ caseTable;
    public static String dropSampleTable = "DROP TABLE IF EXISTS "+ sampleTable;
    public static String dropPatientTable = "DROP TABLE IF EXISTS "+ patientTable;
    public static String dropTestTable = "DROP TABLE IF EXISTS "+ testTable;
    public static String dropGenotypeTable = "DROP TABLE IF EXISTS "+ genotypeTable;
    public static String dropPlasmaTable = "DROP TABLE IF EXISTS "+ plasmaTable;
    public static String dropFilteredTable = "DROP TABLE IF EXISTS "+ filteredTable;
    public static String dropHashTable = "DROP TABLE IF EXISTS "+ hashTable;
    public static String dropLogTable = "DROP TABLE IF EXISTS " + logTable;
    public static String dropErrorTable = "DROP TABLE IF EXISTS " + errorTable;

    //Create Table
    public static String createCaseTable = "CREATE TABLE "+ caseTable +" (" +
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
    public static String createSampleTable = "CREATE TABLE "+ sampleTable +" (" +
            sampleID +" varchar(16) NOT NULL, " +
            dateReceived +" date DEFAULT NULL, " +
            testID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            patientID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            "PRIMARY KEY ("+ sampleID +")" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createPatientTable = "CREATE TABLE " + patientTable +" (" +
            patientID +" varchar(16) NOT NULL, " +
            lastName +" varchar(45) DEFAULT NULL, " +
            firstName +" varchar(45) DEFAULT NULL, " +
            relationship +" varchar(4) DEFAULT NULL, " +
            "PRIMARY KEY ("+ patientID +")" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createTestTable = "CREATE TABLE " + testTable + " (" +
            testID +" varchar(16) NOT NULL, " +
            testStartDate +" date DEFAULT NULL, " +
            testType +" varchar(16) DEFAULT NULL, " +
            cost +" int DEFAULT NULL, " +
            gestation +" int DEFAULT NULL, " +
            legal +" tinyint(1) DEFAULT '1' COMMENT '0 = non-legal test\\n1 = legal test', " +
            caseID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            "PRIMARY KEY ("+ testID + ")" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createGenotypeTable = "CREATE TABLE "+ genotypeTable+ " ("+
            genotypeID +" MEDIUMINT NOT NULL AUTO_INCREMENT, "+
            eventDate +" date NOT NULL, "+
            primerSet +" varchar(16) DEFAULT NULL, "+
            performedBy +" varchar(16) DEFAULT NULL, "+
            testID +" varchar(16) DEFAULT NULL COMMENT 'Future FK', " +
            "PRIMARY KEY ("+ genotypeID + ")" +
            ") AUTO_INCREMENT = 1 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createPlasmaTable = "CREATE TABLE "+ plasmaTable+ " ("+
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
    public static String createFilteredTable = "CREATE TABLE " + filteredTable + " (" +
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
    public static String createHashTable = "CREATE TABLE " + hashTable + " (" +
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
    public static String createLogTable = "CREATE TABLE " + logTable + " (" +
            logID +" MEDIUMINT NOT NULL AUTO_INCREMENT, " +
            caseID +" varchar(16) DEFAULT NULL, " +
            dateUpdated +" date DEFAULT NULL, " +
            fileName +" varchar(45) DEFAULT NULL, " +
            date +" tinyint(1) DEFAULT '0', " +
            motherName +" tinyint(1) DEFAULT '0', " +
            maternalPatientId +" tinyint(1) DEFAULT '0', " +
            paternalPatientId +" tinyint(1) DEFAULT '0', " +
            gestationGender +" tinyint(1) DEFAULT '0', " +
            testTypeCost +" tinyint(1) DEFAULT '0', " +
            referral +" tinyint(1) DEFAULT '0', " +
            genotypeA +" tinyint(1) DEFAULT '0', " +
            genotypeB +" tinyint(1) DEFAULT '0', " +
            firstDraw +" tinyint(1) DEFAULT '0', " +
            secondDraw +" tinyint(1) DEFAULT '0', " +
            thirdDraw +" tinyint(1) DEFAULT '0', " +
            result +" tinyint(1) DEFAULT '0', " +
            confirmation +" tinyint(1) DEFAULT '0', " +
            "PRIMARY KEY ("+ logID +") " +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
    public static String createErrorTable = "CREATE TABLE " + errorTable + " (" +
            errorID + " MEDIUMINT NOT NULL AUTO_INCREMENT, " +
            caseID + " varchar(16) DEFAULT NULL, " +
            fileName + " varchar(40) DEFAULT NULL, " +
            stackTrace + " varchar(2000) DEFAULT NULL, " +
            "PRIMARY KEY ("+ errorID +") " +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

    //Insert Into Table
    //Parameterized Statements: Update DTOs if columns are added, removed, or switched!
    public static String insertCase = "INSERT INTO "+ caseTable +" (" +
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
    public static String insertSample = "INSERT INTO "+ sampleTable +" (" +
            sampleID +", " +
            dateReceived +", " +
            testID +", " +
            patientID +") VALUES("+
            "?,?,?,?)";
    public static String insertPatient = "INSERT INTO " + patientTable +" (" +
            patientID + ", " +
            lastName +", " +
            firstName +", " +
            relationship + ") VALUES(" +
            "?,?,?,?)";
    public static String insertTest = "INSERT INTO " + testTable +" (" +
            testID + ", " +
            testStartDate + ", " +
            testType + ", " +
            cost + ", " +
            gestation + ", " +
            legal + ", " +
            caseID + ") VALUES(" +
            "?,?,?,?,?,?,?)";
    public static String insertGenotype = "INSERT INTO " + genotypeTable + " (" +
            eventDate +", "+
            primerSet +", "+
            performedBy +", "+
            testID +") VALUES(" +
            "?,?,?,?)";
    public static String insertPlasma = "INSERT INTO " + plasmaTable + " (" +
            eventDate +", "+
            plasmaNumber +", "+
            plasmaUsed +", "+
            plasmaGestation +", "+
            performedBy +", "+
            testID +") VALUES(" +
            "?,?,?,?,?,?)";
    public static String insertFilteredCase = "INSERT INTO " + filteredTable +" (" +
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
    public static String insertHash = "INSERT INTO " + hashTable +" (" +
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
    public static String insertLog = "INSERT INTO " + logTable +" (" +
            caseID + ", " +
            dateUpdated + ", " +
            fileName + ", " +
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
            confirmation + ") VALUES (" +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static String insertError = "INSERT INTO " + errorTable + " (" +
            caseID + ", " +
            fileName + ", " +
            stackTrace + ") VALUES (" +
            "?,?,?)";

    //Replace
    public static String replaceHash = "REPLACE " + hashTable +" (" +
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


    //Select
    public static String selectCaseByID = "SELECT * FROM "+ caseTable +" WHERE "+ caseID +" = ? LIMIT 1";
    public static String selectFilteredCaseByID = "SELECT * FROM "+ filteredTable + " WHERE "+ caseID +" = ? LIMIT 1";
    public static String selectErrorByCaseID = "SELECT * FROM "+ errorTable +" WHERE "+ caseID +" = ? LIMIT 1";
    public static String selectHashByID = "SELECT * FROM "+ hashTable + " WHERE "+ caseID +" = ? LIMIT 1";
    public static String selectSampleIDsByTestID = "SELECT "+ sampleID +" FROM "+ sampleTable +" WHERE "+ testID + " = ?";


    //Update
    public static String updateCaseResult = "UPDATE "+ caseTable +" SET " + result + " = ? WHERE "+ caseID + " = ?";
    public static String updateMotherName = "UPDATE "+ caseTable +" SET " + motherLastName +" = ?, "+ motherFirstName +" = ? WHERE "+ caseID + " = ?";
    public static String updatePatientName = "UPDATE "+ patientTable +" SET " + lastName +" = ?, "+ firstName +" = ? WHERE "+ patientID + " = ?";
    public static String updateGestation = "UPDATE "+ testTable +" SET " + gestation + " = ? WHERE "+ testID + " = ?";
    public static String updateGender = "UPDATE "+ caseTable +" SET " + gender + " = ? WHERE "+ caseID + " = ?";
    public static String updateTestTypeCost = "UPDATE "+ testTable +" SET " + testType +" = ?, "+ cost +" = ? WHERE "+ testID + " = ?";
    public static String updateSource = "UPDATE "+ caseTable +" SET " + source + " = ? WHERE "+ caseID + " = ?";
    public static String updateSamplePatientID = "UPDATE "+ sampleTable +" SET " + patientID + " = ? WHERE "+ sampleID + " = ?";
    public static String updateCaseRowHash = "UPDATE "+ caseTable + " SET " + rowHash + " = ? WHERE " + caseID + " = ?";

    //Delete
    public static String deleteSample = "DELETE FROM "+ sampleTable +" WHERE "+ sampleID +" =?";
    public static String deletePatient = "DELETE "+ patientTable +
            " FROM "+ sampleTable + " INNER JOIN " + patientTable +
            " ON " + sampleTable +"."+ patientID +" = "+ patientTable +"."+ patientID +
            " WHERE "+ sampleTable +"." +testID +" =?";
    public static String deleteGenotype = "DELETE FROM "+ genotypeTable + " WHERE "+ primerSet +" = ? AND "+ testID + " =?";
    public static String deletePlasma = "DELETE FROM "+ plasmaTable + " WHERE "+ plasmaNumber +" = ? AND " + testID + " =?";

}
