package logic;

import readwrite.DbManager;
import DataTransferObject.ExcelRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static readwrite.MySQL.*;


public class Comparison {

    private ExcelRow inputRow;
    private String caseID;
    private Integer rowHash;


    /*Calling rs.next() moves the ResultSet cursor. This is not an issue
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
            } else {
                return false;
            }
        } finally {
            connection.close();
        }
    }
    public boolean caseHasChanged() throws SQLException{
        rowHash = inputRow.hashCode();
        //TODO: Abstract into new methods getRow(String id) & getCell(String id, String column)?
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(selectCaseByID);
        stmt.setString(1,caseID);
        try {
            ResultSet rs = stmt.executeQuery();
            int storedRowHash = rs.getInt("rowHash");
            if (rowHash != storedRowHash) {
                return true;
            } else {
                return false;
            }
        } finally {
            connection.close();
        }

    }



    //Implement Comparison Functionality later
    /*public boolean caseStateChanged(String[] currentRow){
        ArrayList<Integer> newState = new ArrayList<>();
        for (String str : currentRow){
            newState.add(stringHash(str));
        }
        return this.caseState != newState;
    }
    */


    public Comparison (ExcelRow inputRow){
        Interpreter interpreter = new Interpreter(inputRow);
        this.inputRow = inputRow;
        caseID = interpreter.findFirstMaternalID();
    }
}
