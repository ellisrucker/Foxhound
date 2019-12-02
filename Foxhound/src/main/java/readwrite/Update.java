package readwrite;

import DataTransferObject.Case;
import DataTransferObject.Patient;
import DataTransferObject.Sample;
import DataTransferObject.Test;
import IntermediateObject.SampleString;
import logic.Interpreter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Update {

    private SeparatedRow inputRow;
    private String caseID;
    private String testID;
    private String motherLastName;
    private String motherFirstName;
    private LocalDate dateUpdated;
    private Interpreter interpreter;


    //New Case Creation

    public void generateNewCase() throws ParseException, SQLException {
        Case newCase = new Case(inputRow);
        newCase.insertNewCase();
        createNewTest();
        ArrayList<List<SampleString>> newSamples = interpreter.consolidateSampleStrings();
        for (List<SampleString> sampleList: newSamples){
            createNewSamples(sampleList);
            createNewPatient(sampleList);
        }

    }
    public void createNewTest() throws SQLException {
        Test newTest = new Test(interpreter, caseID, testID, dateUpdated);
        newTest.insertNewTest();
    }

    private void createNewSamples(List<SampleString> samplesFromPatient) throws SQLException {
        String patientID = samplesFromPatient.get(0).getId();
        for (SampleString ss: samplesFromPatient){
            Sample newSample = new Sample(ss, patientID, testID, dateUpdated);
            newSample.insertNewSample();
        }
    }

    private void createNewPatient(List<SampleString> sampleList) throws SQLException{
        SampleString patientString = sampleList.get(0);
        if(patientString.getRelation() == SampleString.Relation.M){
            Patient newPatient = new Patient(patientString,motherLastName,motherFirstName);
            newPatient.insertNewPatient();
        } else {
            Patient newPatient = new Patient(patientString);
            newPatient.insertNewPatient();
        }
    }

        //Events will also be inserted here




    //Constructors
    public Update(SeparatedRow inputRow) throws ParseException{
        this.inputRow = inputRow;
        interpreter = new Interpreter(inputRow);
        caseID = interpreter.findFirstMaternalID();
        testID = this.caseID +"_" + "1";
        motherLastName = interpreter.findLastName();
        motherFirstName = interpreter.findFirstName();
        dateUpdated = interpreter.findRowDate();

    }

}
