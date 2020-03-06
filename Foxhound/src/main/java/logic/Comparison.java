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
    private String fileName;
    private Connection dbConnection;

    //Constructors
    public Comparison (ExcelRow inputRow, String fileName){
        Interpreter interpreter = new Interpreter(inputRow);
        this.inputRow = inputRow;
        caseID = interpreter.findFirstMaternalSampleID();
        this.fileName = fileName;
    }

    public void evaluateCase() throws SQLException, ParseException {
        dbConnection = DbManager.openConnection();
        try {
            boolean caseExists = caseExists();
            boolean caseIsFiltered = caseHasBeenFiltered();
            if(!caseExists && !caseIsFiltered) {
                Creator creator = new Creator(inputRow);
                creator.generateNewCase();
            } else {
                if(!caseIsFiltered && caseHasChanged()) {
                    implementUpdates();
                }
            }
            //TODO: Add catch to log errors
        } catch(Exception e) {
            dbConnection.rollback();
            //TODO: Create Error and add to Error Table, commit
        } finally {
            dbConnection.close();
        }
    }


    /* Calling rs.next() moves the ResultSet cursor. This is not an issue
    if rs is empty; but if there is a result, the cursor will now be
    pointed at second row in rs and any subsequent calls of rs.next()
    will miss the first result. If rs is used again, remember
    to call rs.beforeFirst() to reset cursor to default position,
    just before the first row. Or use Do-While loop.
    */
    private boolean caseExists() throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(selectCaseByID);
        stmt.setString(1,caseID);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    private boolean caseHasBeenFiltered() throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(selectFilteredCaseByID);
        stmt.setString(1,caseID);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    private boolean caseHasChanged() throws SQLException{
        int newRowHash = inputRow.hashCode();
        int storedRowHash = retrieveStoredHashRow();
        return (newRowHash != storedRowHash);
    }
    private int retrieveStoredHashRow() throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(selectCaseByID);
        stmt.setString(1, caseID);
        int storedRowHash = 0;
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            storedRowHash = rs.getInt(9);
        }
        return storedRowHash;
    }

    private void implementUpdates() throws SQLException, ParseException{
        PreparedStatement stmt = dbConnection.prepareStatement(selectHashByID);
        stmt.setString(1,caseID);
        ResultSet rs = stmt.executeQuery();
        HashRow storedHash = new HashRow(rs);
        HashRow newHash = new HashRow(caseID,inputRow);

        Log log = new Log(newHash,storedHash,fileName);
        Updater updater = new Updater(inputRow);
        updater.updateCase(log);
        replaceHash(newHash);
    }

    private void replaceHash(HashRow hashRow) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(replaceHash);
        stmt.setString(1,caseID);
        stmt.setInt(2,hashRow.getDate());
        stmt.setInt(3,hashRow.getMotherName());
        stmt.setInt(4,hashRow.getMaternalPatientId());
        stmt.setInt(5,hashRow.getPaternalPatientId());
        stmt.setInt(6,hashRow.getGestationGender());
        stmt.setInt(7,hashRow.getTestTypeCost());
        stmt.setInt(8,hashRow.getReferral());
        stmt.setInt(9,hashRow.getGenotypeA());
        stmt.setInt(10,hashRow.getGenotypeB());
        stmt.setInt(11,hashRow.getFirstDraw());
        stmt.setInt(12,hashRow.getSecondDraw());
        stmt.setInt(13,hashRow.getThirdDraw());
        stmt.setInt(14,hashRow.getReferral());
        stmt.setInt(15,hashRow.getConfirmation());
        stmt.executeUpdate();
    }

}
