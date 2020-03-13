package dto;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static utility.MySQL.insertError;

public class Error implements Insertable {

    private String caseID;
    private String fileName;
    private String stackTrace;

    //Constructors
    public Error(){
    }
    public Error(String caseID, String fileName, Exception e){
        this.caseID = caseID;
        this.fileName = fileName;
        this.stackTrace = ExceptionUtils.getStackTrace(e);
    }

    public void insert(Connection dbConnection) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertError);
        stmt.setString(1,caseID);
        stmt.setString(2,fileName);
        stmt.setString(3,stackTrace);
        stmt.executeUpdate();
    }

    //Setters & Getters
    public void setCaseID(String caseID){
        this.caseID = caseID;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public void setStackTrace(String stackTrace){
        this.stackTrace = stackTrace;
    }

    public String getCaseID(){
        return caseID;
    }
    public String getFileName(){
        return fileName;
    }
    public String getStackTrace(){
        return stackTrace;
    }

}
