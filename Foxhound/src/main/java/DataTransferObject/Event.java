package DataTransferObject;

import IntermediateObject.EventString;
import logic.Interpreter;
import readwrite.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static readwrite.MySQL.*;

public class Event {

    private Integer eventID;
    private LocalDate date;
    private LabTest type;
    private PrimerSet primerSet;
    private PlasmaNumber plasmaNumber;
    private String performedBy;
    private String plasmaUsed;
    //Initialization avoids null pointer in stmt.setInt()
    private Integer plasmaGestation = 0;
    //Future Foreign Key
    private String testID;

    public enum LabTest {
        GENOTYPE, PLASMA, CONFIRMATION, UNKNOWN
    }
    public enum PlasmaNumber {
        FIRST, SECOND, THIRD, FOURTH, FIFTH, UNKNOWN
    }

    public enum PrimerSet {
        A, B, A96, B96, X, Y, CFTR, UNKNOWN
    }

    //Constructors
    public Event(){
    }
    public Event(String testID, LocalDate testStartDate, EventString es) {
       date = Interpreter.findEventDate(es.getEvent(), testStartDate);
       type = es.getType();
       primerSet = es.getPrimerSet();
       plasmaNumber = es.getPlasmaNumber();
       this.testID = testID;
       performedBy = es.getPerformedBy();
    }

    //Data Access
    public void insertNewGenotype() throws SQLException {
        Connection connection = DbManager.openConnection();
        try{
           PreparedStatement stmt = connection.prepareStatement(insertGenotype);
           stmt.setObject(1,date);
           stmt.setString(2,primerSet.name());
           stmt.setString(3,performedBy);
           stmt.setString(4,testID);
           stmt.executeUpdate();
        } catch(Exception e){
            System.out.println(testID);
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
    public void insertNewPlasma() throws SQLException{
        Connection connection = DbManager.openConnection();
        try{
            PreparedStatement stmt = connection.prepareStatement(insertPlasma);
            stmt.setObject(1, date);
            stmt.setString(2,plasmaNumber.name());
            stmt.setString(3, plasmaUsed);
            stmt.setInt(4, plasmaGestation);
            stmt.setString(5, performedBy);
            stmt.setString(6, testID);
            stmt.executeUpdate();
        } catch(Exception e){
            System.out.println(testID);
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    //Setters & Getters
    public void setEventID(Integer id){
        eventID = id;
    }
    public void setDate(LocalDate newDate){
        date = newDate;
    }
    public void setType(LabTest testType){
        type = testType;
    }
    public void setPrimerSet(PrimerSet set){
        primerSet = set;
    }
    public void setPlasmaNumber(PlasmaNumber number){
        plasmaNumber = number;
    }
    public void setPerformedBy(String name){
        performedBy = name;
    }
    public void setPlasmaUsed(String sample){
        plasmaUsed = sample;
    }
    public void setPlasmaGestation(Integer gestation){
        plasmaGestation = gestation;
    }
    public void setTestID(String str){
        testID = str;
    }
    public Integer getEventID(){
        return eventID;
    }
    public LocalDate getDate(){
        return date;
    }
    public LabTest getType(){
        return type;
    }
    public PrimerSet getPrimerSet(){
        return primerSet;
    }
    public PlasmaNumber getPlasmaNumber(){
        return plasmaNumber;
    }
    public String getPerformedBy(){
        return performedBy;
    }
    public String getPlasmaUsed(){
        return plasmaUsed;
    }
    public Integer getPlasmaGestation(){
        return plasmaGestation;
    }
    public String getTestID(){
        return testID;
    }
}
