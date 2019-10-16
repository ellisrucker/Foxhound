package DataTransferObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import readwrite.ConnectionManager;
import readwrite.SeparatedRow;
import logic.Interpreter;


public class Case {

    private String id;
    private String motherFirstName;
    private String motherLastName;
    private LocalDate dateStarted;
    private String gender;
    private boolean twins = false;

    public Case() {
    }

    public Case(SeparatedRow newRow) throws ParseException {
        Interpreter dateInterpreter = new Interpreter(newRow.getDate());
        Interpreter nameInterpreter = new Interpreter(newRow.getMotherName());
        Interpreter babyInterpreter = new Interpreter(newRow.getGestationGender());
        id = newRow.getMaternalPatientId();
        motherLastName = nameInterpreter.findLastName();
        motherFirstName = nameInterpreter.findFirstName();
        dateStarted = dateInterpreter.stringToDate();
        gender = babyInterpreter.findGender();
        twins = babyInterpreter.isHavingTwins();
    }

    public void insertNewCase() throws SQLException {
        Connection connection = ConnectionManager.openConnection();
        String newCaseQuery = "INSERT INTO rvdbtest.case ("
            + "caseID, "
            + "motherLastName, "
            + "motherFirstName, "
            + "dateStarted, "
            + "gender, "
            + "twins) VALUES("
            + "?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(newCaseQuery);
            stmt.setString(1,id);
            stmt.setString(2,motherLastName);
            stmt.setString(3,motherFirstName);
            stmt.setObject(4,dateStarted);
            stmt.setString(5, gender);
            stmt.setBoolean(6,twins);
            stmt.executeUpdate();
        } finally {
            connection.close();
        }

    }

    //Setters and Getters
    public String getId() {
        return id;
    }
    public String getMotherLastName(){
        return motherLastName;
    }
    public String getMotherFirstName(){
        return motherFirstName;
    }
    public LocalDate getDateStarted(){
        return dateStarted;
    }
    public String getGender(){
        return gender;
    }
    public boolean getTwins(){
        return twins;
    }
}
