package readwrite;

import DataTransferObject.Case;
import DataTransferObject.Sample;
import IntermediateObject.SampleString;
import logic.Interpreter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Update {

    private SeparatedRow inputRow;
    private String caseID;
    private LocalDate dateUpdated;

    private ArrayList<List<SampleString>> consolidateSampleStrings(){
        ArrayList<List<SampleString>> samplesByPatient = new ArrayList<>();
        Interpreter mCellInterpreter = new Interpreter(inputRow.getMaternalPatientId());
        Interpreter pCellInterpreter = new Interpreter(inputRow.getPaternalPatientId());
        samplesByPatient.add(mCellInterpreter.retrieveMaternalIDs());
        pCellInterpreter.groupIDsByPatient()
                .forEach(samplesByPatient::add);
        return samplesByPatient;
    }

    private void createNewSamples(String sampleCell) throws SQLException {
        Interpreter sampleInterpreter = new Interpreter(sampleCell);
        ArrayList<String> sampleArray = sampleInterpreter.isolateSamples();
        for (String str: sampleArray){
            Sample newSample = new Sample(str, this.caseID, this.dateUpdated);
            newSample.insertNewSample();
        }
    }
    private void createNewSamples(List<SampleString> samplesFromPatient) throws SQLException {
        String patientID = samplesFromPatient.get(0).getId();
        for (SampleString ss: samplesFromPatient){
            Sample newSample = new Sample(ss, patientID, caseID, dateUpdated);
            newSample.insertNewSample();
        }
    }

    public void createNewCase() throws ParseException, SQLException {
        Case newCase = new Case(inputRow);
        newCase.insertNewCase();
        ArrayList<List<SampleString>> newSamples = consolidateSampleStrings();
        for (List<SampleString> sampleList: newSamples){
            createNewSamples(sampleList);
        }

    }

    public void createNewPatients(){

    }

        //Patients and Events
        //will also be inserted here




    //Constructors
    public Update(SeparatedRow inputRow) throws ParseException{
        Interpreter mIDinterpreter = new Interpreter(inputRow.getMaternalPatientId());
        this.inputRow = inputRow;
        caseID = mIDinterpreter.findFirstMaternalID();
        String date = inputRow.getDate();
        Interpreter dateInterpreter = new Interpreter(date);
        dateUpdated = dateInterpreter.stringToDate();

    }

}
