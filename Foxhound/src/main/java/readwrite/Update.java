package readwrite;

import DataTransferObject.*;
import IntermediateObject.EventString;
import IntermediateObject.SampleString;
import logic.Interpreter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Update {

    private ExcelRow inputRow;
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
        ArrayList<EventString> newEvents = interpreter.consolidateAllEvents();
        for (List<SampleString> sampleList: newSamples){
            createNewSamples(sampleList);
            createNewPatient(sampleList);
        }
        for (EventString e: newEvents){
            createNewEvent(e);
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

    private void createNewEvent(EventString eventString) throws SQLException {
        Event e = new Event(testID, dateUpdated, eventString);
        if(e.getType() == Event.LabTest.GENOTYPE){
            e.insertNewGenotype();
        }
        if(e.getType() == Event.LabTest.PLASMA){
            if(e.getPlasmaNumber() == Event.PlasmaNumber.FIRST){
                e.setPlasmaUsed(caseID);
                e.setPlasmaGestation(interpreter.findFirstGestation());
            } else if(e.getPlasmaNumber() == Event.PlasmaNumber.SECOND){
                e.setPlasmaUsed(Interpreter.findID(inputRow.getSecondDraw()));
                e.setPlasmaGestation(Interpreter.findGestation(inputRow.getSecondDraw()));
            } else if(e.getPlasmaNumber() == Event.PlasmaNumber.THIRD){
                e.setPlasmaUsed(Interpreter.findID(inputRow.getThirdDraw()));
                e.setPlasmaGestation(Interpreter.findGestation(inputRow.getThirdDraw()));
            }
            e.insertNewPlasma();
        }

    }





    //Constructors
    public Update(ExcelRow inputRow) throws ParseException{
        this.inputRow = inputRow;
        interpreter = new Interpreter(inputRow);
        caseID = interpreter.findFirstMaternalID();
        testID = this.caseID +"_" + "1";
        motherLastName = interpreter.findLastName();
        motherFirstName = interpreter.findFirstName();
        dateUpdated = interpreter.findRowDate();

    }

}
