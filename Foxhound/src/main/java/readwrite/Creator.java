package readwrite;

import DataTransferObject.*;
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
    private String caseID;
    private String testID;
    private String motherLastName;
    private String motherFirstName;
    private LocalDate dateUpdated;
    private Interpreter interpreter;
    private Connection dbConnection;


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
            insertNewCase(newCase);
            insertNewHash(hashRow);
            insertNewTest(newTest);
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
            //TODO: Create Error and add to Error Table, commit
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
            insertNewSample(s);
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
        insertNewPatient(newPatient);
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
    public void insertNewCase(Case c) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertCase);
        stmt.setString(1,c.getId());
        stmt.setString(2,c.getMotherLastName());
        stmt.setString(3,c.getMotherFirstName());
        stmt.setObject(4,c.getDateStarted());
        stmt.setString(5,c.getGender());
        stmt.setBoolean(6,c.getTwins());
        stmt.setString(7,c.getSource());
        stmt.setString(8,c.getResult());
        stmt.setInt(9,c.getRowHash());
        stmt.executeUpdate();
    }
    public void insertNewTest(Test t) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertTest);
        stmt.setString(1,t.getTestID());
        stmt.setObject(2,t.getDateStarted());
        stmt.setString(3,t.getType().name());
        stmt.setInt(4,t.getCost());
        stmt.setInt(5,t.getGestation());
        stmt.setBoolean(6,t.getLegal());
        stmt.setString(7,t.getCaseID());
        stmt.executeUpdate();
    }
    public void insertNewHash(HashRow h) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertHash);
        stmt.setString(1,caseID);
        stmt.setInt(2,h.getDate());
        stmt.setInt(3,h.getMotherName());
        stmt.setInt(4,h.getMaternalPatientId());
        stmt.setInt(5,h.getPaternalPatientId());
        stmt.setInt(6,h.getGestationGender());
        stmt.setInt(7,h.getTestTypeCost());
        stmt.setInt(8,h.getReferral());
        stmt.setInt(9,h.getGenotypeA());
        stmt.setInt(10,h.getGenotypeB());
        stmt.setInt(11,h.getFirstDraw());
        stmt.setInt(12,h.getSecondDraw());
        stmt.setInt(13,h.getThirdDraw());
        stmt.setInt(14,h.getResult());
        stmt.setInt(15,h.getConfirmation());
        stmt.executeUpdate();
    }
    public void insertNewSample(Sample s) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertSample);
        stmt.setString(1,s.getSampleID());
        stmt.setObject(2,s.getDateReceived());
        stmt.setString(3,s.getTestID());
        stmt.setString(4,s.getPatientID());
        stmt.executeUpdate();
    }
    public void insertNewPatient(Patient p) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertPatient);
        stmt.setString(1,p.getPatientID());
        stmt.setString(2,p.getLastName());
        stmt.setString(3,p.getFirstName());
        stmt.setString(4,p.getRelationship().name());
        stmt.executeUpdate();
    }
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
