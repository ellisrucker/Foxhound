package DataTransferObject;

import logic.Interpreter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static readwrite.MySQL.insertGenotype;

public class Genotype implements Insertable {

    private LocalDate date;
    private String performedBy;
    private PrimerSet primerSet;
    private String testID;
    private String originalString;

    public enum PrimerSet {
        A, B, A96, B96, X, Y, CFTR, UNKNOWN
    }

    //Constructors
    public Genotype(){
    }
    public Genotype(String str, PrimerSet set){
        this.primerSet = set;
        performedBy = Interpreter.findPersonnel(str);
        originalString = str;
    }

    public void insert(Connection dbConnection) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertGenotype);
        stmt.setObject(1,date);
        stmt.setString(2,primerSet.name());
        stmt.setString(3,performedBy);
        stmt.setString(4,testID);
        stmt.executeUpdate();
    }


    //Setters & Getters
    public void setDate(LocalDate date){
        this.date = date;
    }
    public void setPerformedBy(String name){
        performedBy = name;
    }
    public void setPrimerSet(PrimerSet set){
        primerSet = set;
    }
    public void setTestID(String testID){
        this.testID = testID;
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
    public PrimerSet getPrimerSet(){
        return primerSet;
    }
    public String getTestID(){
        return testID;
    }
    public String getOriginalString(){
        return originalString;
    }

}
