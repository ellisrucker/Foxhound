package logic;

import DataTransferObject.HashRow;
import DataTransferObject.Log;
import readwrite.Creator;
import readwrite.DbManager;
import DataTransferObject.ExcelRow;
import readwrite.Updater;

import java.io.File;
import java.sql.*;
import java.text.ParseException;

import static readwrite.MySQL.*;


public class Comparison {

    private ExcelRow inputRow;
    private String caseID;
    private Integer newRowHash;
    private String fileName;

    //Constructors
    public Comparison (ExcelRow inputRow, String fileName){
        Interpreter interpreter = new Interpreter(inputRow);
        this.inputRow = inputRow;
        caseID = interpreter.findFirstMaternalSampleID();
        this.fileName = fileName;
    }


    /* Calling rs.next() moves the ResultSet cursor. This is not an issue
    if rs is empty; but if there is a result, the cursor will now be
    pointed at second row in rs and any subsequent calls of rs.next()
    will miss the first result. If rs is used again, remember
    to call rs.beforeFirst() to reset cursor to default position,
    just before the first row. Or use Do-While loop.
    */
    public boolean caseExists() throws SQLException {
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(selectCaseByID);
        stmt.setString(1,caseID);
        try {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            else {
                return false;
            }
        } finally {
            connection.close();
        }
    }
    public boolean caseHasBeenFiltered() throws SQLException {
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(selectFilteredCaseByID);
        stmt.setString(1,caseID);
        try {
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }
            else {
                return false;
            }
        } finally {
            connection.close();
        }
    }
    public void evaluateCase() throws SQLException, ParseException {
        Boolean caseExists = caseExists();
        Boolean caseIsFiltered = caseHasBeenFiltered();
        if(!caseExists && !caseIsFiltered) {
            Creator creator = new Creator(inputRow);
            creator.generateNewCase();
        } else {
            if(!caseIsFiltered && caseHasChanged()) {
                implementUpdates();

            }
        }
    }
    public boolean caseHasChanged() throws SQLException{
        newRowHash = inputRow.hashCode();
        int storedRowHash = retrieveStoredHashRow();
        boolean caseChanged = (newRowHash != storedRowHash);
        return caseChanged;
    }
    private int retrieveStoredHashRow() throws SQLException {
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(selectCaseByID);
        stmt.setString(1, caseID);
        int storedRowHash = 0;
        try {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                storedRowHash = rs.getInt(9);
            }
            return storedRowHash;
        }finally {
            connection.close();
        }
    }

    private void implementUpdates() throws SQLException, ParseException{
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(selectHashByID);
        stmt.setString(1,caseID);
        try{
            ResultSet rs = stmt.executeQuery();
            HashRow storedHash = new HashRow(rs);
            HashRow newHash = new HashRow(caseID,inputRow);

            Log log = new Log(newHash,storedHash,fileName);
            Updater updater = new Updater(inputRow);
            updater.updateCase(log);
            newHash.replaceHash();
        } finally{
            connection.close();
        }
    }



}
