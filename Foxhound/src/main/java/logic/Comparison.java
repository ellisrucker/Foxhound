package logic;

import readwrite.DbManager;
import readwrite.SeparatedRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static readwrite.MySQL.*;


public class Comparison {

    private SeparatedRow inputRow;
    private Integer rowHash;
    //TODO: Add caseID as field to refer to, rather than finding it again in each method.


    /*Calling rs.next() moves the ResultSet cursor. This is not an issue
    if rs is empty; but if there is a result, the cursor will now be
    pointed at second row in rs and any subsequent calls of rs.next()
    will miss the first result. If rs is used again, remember
    to call rs.beforeFirst() to reset cursor to default position,
    just before the first row. Or use Do-While loop.
    */
    public boolean caseExists() throws SQLException {
        Interpreter caseIDinterpreter = new Interpreter(inputRow.getMaternalPatientId());
        String id = caseIDinterpreter.findFirstMaternalID();
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(selectCaseByID);
        stmt.setString(1,id);
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
        Interpreter caseIDinterpreter = new Interpreter(inputRow.getMaternalPatientId());
        String id = caseIDinterpreter.findFirstMaternalID();
        //TODO: Abstract into new methods getRow(String id) & getCell(String id, String column)?
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(selectCaseByID);
        stmt.setString(1,id);
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


    public Comparison (SeparatedRow inputRow){
        this.inputRow = inputRow;
    }
}
