package DataTransferObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

import readwrite.DbManager;
import logic.Interpreter;
import static readwrite.MySQL.*;

public class Case implements Insertable {

    private String id;
    private String motherFirstName;
    private String motherLastName;
    private LocalDate dateStarted;
    private String gender;
    private boolean twins = false;
    private String source;
    private String result;
    private Integer rowHash;


    //Constructors
    public Case() {
    }
    public Case(ExcelRow newRow) throws ParseException {
        Interpreter interpreter = new Interpreter(newRow);

        id = interpreter.findFirstMaternalSampleID();
        motherLastName = interpreter.findLastName();
        motherFirstName = interpreter.findFirstName();
        dateStarted = interpreter.findRowDate();
        gender = interpreter.findFirstGender();
        twins = interpreter.isHavingTwins();
        source = interpreter.findSource();
        result = interpreter.findResultString();
        rowHash = newRow.hashCode();
    }

    public void insert(Connection dbConnection) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertCase);
        stmt.setString(1,id);
        stmt.setString(2,motherLastName);
        stmt.setString(3,motherFirstName);
        stmt.setObject(4,dateStarted);
        stmt.setString(5,gender);
        stmt.setBoolean(6,twins);
        stmt.setString(7,source);
        stmt.setString(8,result);
        stmt.setInt(9,rowHash);
        stmt.executeUpdate();
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
    public String getSource() {
        return source;
    }
    public String getResult(){
        return result;
    }
    public Integer getRowHash(){
        return rowHash;
    }
}
