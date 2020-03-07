package readwrite;

import DataTransferObject.*;
import DataTransferObject.Error;
import logic.Interpreter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static readwrite.MySQL.*;

public class Creator {

    //Primarily handles Creation and Insertion of new Database Rows

    private ExcelRow inputRow;
    private String fileName;
    private String caseID;
    private String testID;
    private String motherLastName;
    private String motherFirstName;
    private LocalDate dateUpdated;
    private Interpreter interpreter;
    private Connection dbConnection;



    //Constructors
    public Creator(ExcelRow inputRow, String fileName) throws ParseException{
        this.inputRow = inputRow;
        this.fileName = fileName;
        interpreter = new Interpreter(inputRow);
        caseID = interpreter.findFirstMaternalSampleID();
        testID = this.caseID +"_" + "1";
        motherLastName = interpreter.findLastName();
        motherFirstName = interpreter.findFirstName();
        dateUpdated = interpreter.findRowDate();
    }


    //New Case Creation

    public void generateNewCase() throws ParseException, SQLException {
        //Create DTOs
        inputRow.setCaseID(caseID);
        Case newCase = new Case(inputRow);
        Test newTest = new Test(interpreter, caseID, testID, dateUpdated);
        HashRow hashRow = new HashRow(caseID,inputRow);
        ArrayList<List<Sample>> newSamples = interpreter.consolidateSamples();
        ArrayList<Event> newEvents = interpreter.consolidateAllEvents();

        //Create db connection
        dbConnection = DbManager.openConnection();

        try {
            newCase.insert(dbConnection);
            hashRow.insert(dbConnection);
            newTest.insert(dbConnection);
            for (List<Sample> sampleList: newSamples){
                createNewSamples(sampleList);
                createNewPatient(sampleList);
            }
            for (Event e: newEvents){
                createNewEvent(e);
            }
            dbConnection.commit();
        } catch (Exception e) {
            dbConnection.rollback();
            Error error = new Error(caseID, fileName, e);
            error.insert(dbConnection);
            dbConnection.commit();
        } finally {
            dbConnection.close();
        }
    }

    private void createNewSamples(List<Sample> samplesFromPatient) throws SQLException {
        LocalDate date = dateUpdated;
        String testID = this.testID;
        for (Sample s: samplesFromPatient){
            s.setDateReceived(date);
            s.setTestID(testID);
            s.insert(dbConnection);
        }
    }
    private void createNewPatient(List<Sample> sampleList) throws SQLException{
        Sample firstSample = sampleList.get(0);
        Patient newPatient = new Patient(firstSample);

        if(newPatient.getRelationship() == Patient.Relation.M){
            //Set first and last name
            newPatient.setLastName(motherLastName);
            newPatient.setFirstName(motherFirstName);
        }
        newPatient.insert(dbConnection);
    }
    private void createNewEvent(Event event) throws SQLException {
        LocalDate date = Interpreter.findEventDate(event.getOriginalString(), dateUpdated);
        event.setDate(date);
        event.setTestID(testID);
        if(event.getType() == Event.LabTest.GENOTYPE){
            insertNewGenotype(event);
        }
        if(event.getType() == Event.LabTest.PLASMA){
            insertNewPlasma(event);
        }
    }

    //DML
    public void insertNewGenotype(Event g) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertGenotype);
        stmt.setObject(1, g.getDate());
        stmt.setString(2, g.getPrimerSet().name());
        stmt.setString(3, g.getPerformedBy());
        stmt.setString(4, g.getTestID());
        stmt.executeUpdate();
    }
    public void insertNewPlasma(Event p) throws SQLException{
        PreparedStatement stmt = dbConnection.prepareStatement(insertPlasma);
        stmt.setObject(1, p.getDate());
        stmt.setString(2, p.getPlasmaNumber().name());
        stmt.setString(3, p.getPlasmaUsed());
        stmt.setInt(4, p.getPlasmaGestation());
        stmt.setString(5, p.getPerformedBy());
        stmt.setString(6, p.getTestID());
        stmt.executeUpdate();
    }

}
