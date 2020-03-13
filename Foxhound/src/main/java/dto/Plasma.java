package dto;

import logic.Interpreter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static utility.MySQL.insertPlasma;

public class Plasma implements Insertable {

    private LocalDate date;
    private String performedBy;
    private String testID;
    private String originalString;
    private PlasmaNumber plasmaNumber;
    private String plasmaUsed;
    //Initialization avoids null pointer in stmt.setInt(): switch to int?
    private Integer plasmaGestation = 0;

    public enum PlasmaNumber {
        FIRST, SECOND, THIRD, FOURTH, FIFTH, UNKNOWN
    }

    //Constructors
    public Plasma(){
    }
    public Plasma(String str, PlasmaNumber plasmaNumber){
        this.plasmaNumber = plasmaNumber;
        performedBy = Interpreter.findPersonnel(str);
        originalString = str;
    }

    public void insert(Connection dbConnection) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertPlasma);
        stmt.setObject(1, date);
        stmt.setString(2,plasmaNumber.name());
        stmt.setString(3, plasmaUsed);
        stmt.setInt(4, plasmaGestation);
        stmt.setString(5, performedBy);
        stmt.setString(6, testID);
        stmt.executeUpdate();
    }

    //Setters & Getters
    public void setDate(LocalDate date){
        this.date = date;
    }
    public void setPerformedBy(String name){
        performedBy = name;
    }
    public void setTestID(String testID){
        this.testID = testID;
    }
    public void setPlasmaNumber(PlasmaNumber number){
        plasmaNumber = number;
    }
    public void setPlasmaUsed(String sample){
        plasmaUsed = sample;
    }
    public void setPlasmaGestation(Integer gestation){
        plasmaGestation = gestation;
    }
    public void setOriginalString(String str){
        originalString = str;
    }

    public LocalDate getDate(){
        return date;
    }
    public String getPerformedBy(){
        return performedBy;
    }
    public String getTestID(){
        return testID;
    }
    public PlasmaNumber getPlasmaNumber(){
        return plasmaNumber;
    }
    public String getPlasmaUsed(){
        return plasmaUsed;
    }
    public Integer getPlasmaGestation(){
        return plasmaGestation;
    }
    public String getOriginalString(){
        return originalString;
    }
}
