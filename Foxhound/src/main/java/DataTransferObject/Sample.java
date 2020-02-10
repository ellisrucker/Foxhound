package DataTransferObject;

import logic.Interpreter;
import readwrite.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static readwrite.MySQL.*;

public class Sample implements Comparable<Sample> {

    private String sampleID;
    private LocalDate dateReceived;
    private String testID;
    private String patientID;
    //Relation is used solely for helping to create Patients
    private Patient.Relation relation;



    //Constructors
    public Sample(String str){
        sampleID = Interpreter.findID(str);
        relation = Interpreter.findRelation(str);
    }
    public Sample(String str, Patient.Relation relation){
        sampleID = Interpreter.findID(str);
        this.relation = relation;
    }
    public Sample(String str, String testID, LocalDate date){
        sampleID = patientID = Interpreter.findID(str);
        this.testID = testID;
        dateReceived = date;
    }


    @Override
    public int compareTo(Sample o) {
        return Interpreter.idToNumber(this.sampleID) - Interpreter.idToNumber(o.sampleID);
    }
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        Sample s = (Sample) o;
        return s.sampleID == this.sampleID
                && s.dateReceived == this.dateReceived
                && s.testID == this.testID
                && s.patientID == this.patientID
                && s.relation == this.relation;
    }

    //Data Access
    public void insertNewSample () throws SQLException {
        Connection connection = DbManager.openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(insertSample);
            stmt.setString(1,sampleID);
            stmt.setObject(2,dateReceived);
            stmt.setString(3,testID);
            stmt.setString(4,patientID);
            stmt.executeUpdate();
        } finally {
            connection.close();
        }
    }




    //Setters and Getters
    public void setSampleID(String id){
        sampleID = id;
    }
    public void setDateReceived(LocalDate date){
        dateReceived = date;
    }
    public void setTestID(String id){
        testID = id;
    }
    public void setPatientID(String patient){
        patientID = patient;
    }
    public void setRelation(Patient.Relation relation){
        this.relation = relation;
    }

    public String getSampleID(){
        return sampleID;
    }
    public LocalDate getDateReceived(){
        return dateReceived;
    }
    public String getTestID(){
        return testID;
    }
    public String getPatientID(){
        return patientID;
    }
    public Patient.Relation getRelation() {
        return relation;
    }
}
