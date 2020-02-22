package readwrite;

import DataTransferObject.*;
import logic.Interpreter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Creator {

    //Primarily handles Creation and Insertion of new Database Rows

    private ExcelRow inputRow;
    private String caseID;
    private String testID;
    private String motherLastName;
    private String motherFirstName;
    private LocalDate dateUpdated;
    private Interpreter interpreter;


    //New Case Creation

    public void generateNewCase() throws ParseException, SQLException {
        inputRow.setCaseID(caseID);
        Case newCase = new Case(inputRow);
        HashRow hashRow = new HashRow(caseID,inputRow);
        newCase.insertNewCase();
        hashRow.insertNewHash();
        createNewTest();
        ArrayList<List<Sample>> newSamples = interpreter.consolidateSamples();
        ArrayList<Event> newEvents = interpreter.consolidateAllEvents();
        for (List<Sample> sampleList: newSamples){
            createNewSamples(sampleList);
            createNewPatient(sampleList);
        }
        for (Event e: newEvents){

            createNewEvent(e);
        }
    }

    public void createNewTest() throws SQLException {
        Test newTest = new Test(interpreter, caseID, testID, dateUpdated);
        newTest.insertNewTest();
    }

    private void createNewSamples(List<Sample> samplesFromPatient) throws SQLException {
        LocalDate date = dateUpdated;
        String testID = this.testID;
        for (Sample s: samplesFromPatient){
            s.setDateReceived(date);
            s.setTestID(testID);
            s.insertNewSample();
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
        newPatient.insertNewPatient();
    }

    private void createNewEvent(Event event) throws SQLException {
        LocalDate date = interpreter.findEventDate(event.getOriginalString(), dateUpdated);
        event.setDate(date);
        event.setTestID(testID);
        if(event.getType() == Event.LabTest.GENOTYPE){
            event.insertNewGenotype();
        }
        if(event.getType() == Event.LabTest.PLASMA){
            event.insertNewPlasma();
        }

    }





    //Constructors
    public Creator(ExcelRow inputRow) throws ParseException{
        this.inputRow = inputRow;
        interpreter = new Interpreter(inputRow);
        caseID = interpreter.findFirstMaternalSampleID();
        testID = this.caseID +"_" + "1";
        motherLastName = interpreter.findLastName();
        motherFirstName = interpreter.findFirstName();
        dateUpdated = interpreter.findRowDate();

    }

}
