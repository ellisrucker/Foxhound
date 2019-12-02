package DataTransferObject;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import logic.Interpreter;
import readwrite.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static readwrite.MySQL.insertTest;


public class Test {

    private String testID;
    private LocalDate dateStarted;
    private TestType type;
    private Integer cost;
    private Integer gestation;
    private boolean legal = true;
    private String caseID;

    public enum TestType {
        EARLY, ONE_WEEK, TWO_WEEK, THREE_WEEK, GENDER, SURROGATE, IVF, MATERNITY, MAT_PAT, UNKNOWN}



    //Constructors
    public Test(Interpreter interpreter, String caseID, String testID, LocalDate date){
        //testID is temporary. Will be iterated as additional Tests are added to a Case.
        this.testID = caseID + "_" + "1" ;
        dateStarted = date;
        type = interpreter.findFirstTestType();
        cost = interpreter.findFirstCost();
        gestation = interpreter.findFirstGestation();
        this.caseID = caseID;
    }

    //Data Access Logic
    public void insertNewTest() throws SQLException {
        Connection connection = DbManager.openConnection();
        try{
            PreparedStatement stmt = connection.prepareStatement(insertTest);
            stmt.setString(1,testID);
            stmt.setObject(2,dateStarted);
            stmt.setString(3,type.name());
            stmt.setInt(4,cost);
            stmt.setInt(5,gestation);
            stmt.setBoolean(6,legal);
            stmt.setString(7,this.caseID);
            stmt.executeUpdate();
        } catch(Exception e) {
            System.out.println(this.testID);
            e.printStackTrace();
        }finally{
            connection.close();
        }
    }


    //Setters & Getters
    public void setTestID(String id){
        testID = id;
    }
    public void setDateStarted(LocalDate date){
        dateStarted = date;
    }
    public void setType(TestType testType){
        type = testType;
    }
    public void setCost(Integer cost){
        this.cost = cost;
    }
    public void setGestation(Integer gestation){
        this.gestation = gestation;
    }
    public void setLegal(boolean isLegal){
        legal = isLegal;
    }
    public void setCaseID(String caseID){
        this.caseID = caseID;
    }

    public String getTestID(){
        return testID;
    }
    public LocalDate getDateStarted(){
        return dateStarted;
    }
    public TestType getType(){
        return type;
    }
    public Integer getCost(){
        return cost;
    }
    public Integer getGestation(){
        return gestation;
    }
    public boolean getLegal(){
        return legal;
    }
    public String getCaseID(){
        return caseID;
    }

}
