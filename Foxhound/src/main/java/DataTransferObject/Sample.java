package DataTransferObject;

import IntermediateObject.SampleString;
import logic.Interpreter;
import readwrite.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import static readwrite.MySQL.*;
public class Sample {

    //CaseID acts as placeholder for TestID until
    //functionality for multiple Tests is added
    private String sampleID;
    private LocalDate dateReceived;
    private String caseID;
    private String patientID;


    //Constructors
    public Sample(String str,String caseID, LocalDate date){
        sampleID = patientID = Interpreter.findID(str);
        this.caseID = caseID;
        dateReceived = date;
    }
    public Sample(SampleString ss,String patientID,String caseID, LocalDate date){
        sampleID = ss.getId();
        this.patientID = patientID;
        this.caseID = caseID;
        dateReceived = date;
    }

    //Data Access Logic
    public void insertNewSample () throws SQLException {
        Connection connection = DbManager.openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(insertSample);
            stmt.setString(1,sampleID);
            stmt.setObject(2,dateReceived);
            stmt.setString(3,caseID);
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
    public void setCaseID(String id){
        caseID = id;
    }
    public void setPatientID(String patient){
        patientID = patient;
    }

    public String getSampleID(){
        return sampleID;
    }
    public LocalDate getDateReceived(){
        return dateReceived;
    }
    public String getCaseID(){
        return caseID;
    }
    public String getPatientID(){
        return patientID;
    }
}
