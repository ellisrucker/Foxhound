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
        ArrayList<Genotype> newGenotypes = interpreter.consolidateAllGenotypes();
        ArrayList<Plasma> newPlasmas = interpreter.consolidateAllPlasmas();

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
            for(Genotype g: newGenotypes){
                createNewGenotype(g);
            }
            for(Plasma p: newPlasmas){
                createNewPlasma(p);
            }
            dbConnection.commit();
        } catch (Exception e) {
            dbConnection.rollback();
            Error error = new Error(caseID, fileName, e);
            error.insert(dbConnection);
            dbConnection.commit();
            System.out.println("Error at "+ caseID +" in "+ fileName);
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
    private void createNewGenotype(Genotype g) throws SQLException {
        LocalDate date = Interpreter.findEventDate(g.getOriginalString(), dateUpdated);
        g.setDate(date);
        g.setTestID(testID);
        g.insert(dbConnection);
    }
    private void createNewPlasma(Plasma p) throws SQLException {
        LocalDate date = Interpreter.findEventDate(p.getOriginalString(), dateUpdated);
        p.setDate(date);
        p.setTestID(testID);
        p.insert(dbConnection);
    }

}
