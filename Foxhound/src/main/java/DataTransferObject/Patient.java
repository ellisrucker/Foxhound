package DataTransferObject;

import IntermediateObject.SampleString;
import readwrite.DbManager;
import readwrite.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static readwrite.MySQL.insertPatient;

public class Patient {

    private String patientID;
    private String lastName;
    private String firstName;
    private SampleString.RELATION relationship;

    //Constructors
    public Patient() {
    }
    public Patient(SampleString ss){
        patientID = ss.getId();
        relationship = ss.getRelation();
    }
    public Patient(SampleString ss, String lastName, String firstName){
        patientID = ss.getId();
        relationship = ss.getRelation();
        this.lastName = lastName;
        this.firstName = firstName;
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

}
