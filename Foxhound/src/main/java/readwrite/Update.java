package readwrite;

import DataTransferObject.Case;
import DataTransferObject.Sample;
import logic.Interpreter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Update {

    private SeparatedRow inputRow;
    private String caseID;
    private LocalDate dateUpdated;

    private void createNewSamples(String sampleCell) throws SQLException {
        Interpreter sampleInterpreter = new Interpreter(sampleCell);
        ArrayList<String> sampleArray = sampleInterpreter.isolateSamples();
        for(String str: sampleArray){
            Sample newSample = new Sample(str, this.caseID, this.dateUpdated);
            newSample.insertNewSample();
        }
    }

    public void createNewCase() throws ParseException, SQLException {
        Case newCase = new Case(inputRow);
        newCase.insertNewCase();
        createNewSamples(inputRow.getMaternalPatientId());
        createNewSamples(inputRow.getPaternalPatientId());

        //Patients and Events
        //will also be inserted here
    }



    //Constructors
    public Update(SeparatedRow inputRow) throws ParseException{
        this.inputRow = inputRow;
        caseID = inputRow.getMaternalPatientId();
        String date = inputRow.getDate();
        Interpreter dateInterpreter = new Interpreter(date);
        dateUpdated = dateInterpreter.stringToDate();

    }

}
