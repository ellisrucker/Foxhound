package DataTransferObject;

import readwrite.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static readwrite.MySQL.insertPatient;

public class Patient {

    private String patientID;
    private String lastName;
    private String firstName;
    private Relation relationship;

    public enum Relation {M,P1,P2,P3,P4,P5,UNKNOWN}

    //Constructors
    public Patient() {
    }
    public Patient(String id, Relation relation){
        patientID = id;
        relationship = relation;
    }
    public Patient(Sample sample){
        patientID = sample.getSampleID();
        relationship = sample.getRelation();
    }

    public void insertNewPatient() throws SQLException {
        Connection connection = DbManager.openConnection();
        try{
            PreparedStatement stmt = connection.prepareStatement(insertPatient);
            stmt.setString(1,patientID);
            stmt.setString(2,lastName);
            stmt.setString(3,firstName);
            stmt.setString(4,relationship.name());
            stmt.executeUpdate();
        } finally{
            connection.close();
        }
    }

    //Setters & Getters
    public void setPatientID(String patientID){
        this.patientID = patientID;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setRelationship(Relation relationship){
        this.relationship = relationship;
    }

    public String getPatientID(){
        return patientID;
    }
    public String getLastName(){
        return lastName;
    }
    public String getFirstName(){
        return firstName;
    }
    public Relation getRelationship(){
        return relationship;
    }
}
