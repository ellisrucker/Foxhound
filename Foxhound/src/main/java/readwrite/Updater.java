package readwrite;

import DataTransferObject.*;
import IntermediateObject.ChangeMap;
import logic.Interpreter;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static readwrite.MySQL.*;

public class Updater {

    //Primarily handles the Updating of cells within a database row

    private ExcelRow inputRow;
    private Interpreter interpreter;
    private String caseID;
    private String testID;
    private LocalDate dateUpdated;

    //Constructors
    public Updater(){
    }
    public Updater(ExcelRow inputRow, String caseID, String testID){
        this.inputRow = inputRow;
        interpreter = new Interpreter(inputRow);
        this.caseID = caseID;
        this.testID = testID;
    }
    public Updater(ExcelRow inputRow) throws ParseException {
        this.inputRow = inputRow;
        interpreter = new Interpreter(inputRow);
        caseID = interpreter.findFirstMaternalSampleID();
        testID = caseID + "_1";
        dateUpdated = interpreter.findRowDate();
    }
    //TODO: replace with Switch Statement
    public void updateCase(ChangeMap changeMap) throws SQLException {
        if(changeMap.getMotherName()){
            updateMotherName();
        }
        if((changeMap.getMaternalPatientId())||(changeMap.getPaternalPatientId())){
            updateSamplesAndPatients();
        }
        if(changeMap.getGestationGender()){
            updateGestationGender();
        }
        if(changeMap.getTestTypeCost()){
            updateTestTypeCost();
        }
        if(changeMap.getReferral()){
            updateSource();
        }
        if((changeMap.getGenotypeA())||
                (changeMap.getGenotypeB()) ||
                (changeMap.getFirstDraw()) ||
                (changeMap.getSecondDraw()) ||
                (changeMap.getThirdDraw()))
        {
         updateEvents();
        }
        //Update Samples and Plasmas from 2nd Draw
        //Update Samples and Plasmas from 3rd Draw
        if(changeMap.getResult()){
            updateResult();
        }
    }

    public void updateMotherName() throws SQLException {
        String motherLastName = interpreter.findLastName();
        String motherFirstName = interpreter.findFirstName();
        Connection connection = DbManager.openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(updateMotherName);
            stmt.setString(1,motherLastName);
            stmt.setString(2,motherFirstName);
            stmt.setString(3,caseID);
            stmt.executeUpdate();
        } catch(Exception e) {
            System.out.println(e);
        } finally {
            connection.close();
        }
    }

    public void updateSamplesAndPatients() throws SQLException{
        ArrayList<List<Sample>> samplesByPatient = interpreter.consolidateSamples();

        ArrayList<String> storedSampleIDs = retrieveStoredSampleIDs();
        //Delete all patients under given testID, and insert new patients
        deleteAndReplacePatients(samplesByPatient);

        //Delete Samples who's IDs do not appear in cell
        deleteOutdatedSamples(samplesByPatient,storedSampleIDs);
        //if a stored sampleID is in newSampleIDs, UPDATE sample.patientID

        updateExistingSamples(samplesByPatient,storedSampleIDs);

        //if a new sample is not in db, INSERT into db
        insertAllNewSamples(samplesByPatient,storedSampleIDs);

    }

    public void updateGestationGender() throws SQLException{
        Integer gestation = interpreter.findFirstGestation();
        String gender = interpreter.findFirstGender();
        Connection connection = DbManager.openConnection();
        try{
            PreparedStatement gestationStmt = connection.prepareStatement(updateGestation);
            PreparedStatement genderStmt = connection.prepareStatement(updateGender);
            gestationStmt.setInt(1,gestation);
            gestationStmt.setString(2,testID);
            genderStmt.setString(1,gender);
            genderStmt.setString(2,testID);
            gestationStmt.executeUpdate();
            genderStmt.executeUpdate();
        } finally {
            connection.close();
        }

    }
    public void updateTestTypeCost() throws SQLException{
        Test.TestType type = interpreter.findFirstTestType();
        Integer cost = interpreter.findFirstCost();
        Connection connection = DbManager.openConnection();
        try{
            PreparedStatement stmt = connection.prepareStatement(updateTestTypeCost);
            stmt.setString(1,type.name());
            stmt.setInt(2,cost);
            stmt.setString(3,testID);
            stmt.executeUpdate();
        } finally {
            connection.close();
        }
    }
    public void updateSource() throws SQLException{
        String source = interpreter.findSource();
        Connection connection = DbManager.openConnection();
        try{
            PreparedStatement stmt = connection.prepareStatement(updateSource);
            stmt.setString(1,source);
            stmt.setString(2,caseID);
            stmt.executeUpdate();
        } finally {
            connection.close();
        }
    }
    //TODO: Separate: make update protocol for each event cell?
    public void updateEvents() throws SQLException {
        deleteOldEvents();
        insertNewEvents();
    }
    //TODO: Remember to set date and testID for Event Update pipeline
    public void updateGenotypeA(){}
    public void updateGenotypeB(){}
    public void updateFirstDraw(){}
    public void updateSecondDraw(){}
    public void updateThirdDraw(){}
    public void updateResult() throws SQLException {
        String result = inputRow.getResult();
        Connection connection = DbManager.openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(updateCaseResult);
            stmt.setString(1,result);
            stmt.setString(2, caseID);
            stmt.executeUpdate();
        } catch(Exception e) {
            System.out.println(e);
        } finally {
            connection.close();
        }
    }
    public void updateConfirmation(){}


    //Sample Update Methods

    private ArrayList<String> listAllNewSampleIDs(ArrayList<List<Sample>> samplesByPatient){
        ArrayList<String> allSampleIDs = new ArrayList<>();
        for(List<Sample> sampleIDList: samplesByPatient){
            sampleIDList.stream()
                    .map(e -> e.getSampleID())
                    .forEachOrdered(allSampleIDs::add);
        }
        return allSampleIDs;
    }
    private Map<String,Sample> mapSamplesToSampleIDs(ArrayList<List<Sample>> samplesByPatient){
        Map<String,Sample> sampleIDMap = new HashMap<>();
        ArrayList<Sample> allSamples = new ArrayList<>();
        for(List<Sample> sampleList: samplesByPatient){
            sampleList.stream()
                    .forEachOrdered(allSamples::add);
        }
        for(Sample sample: allSamples){
            sampleIDMap.put(sample.getSampleID(),sample);
        }
        return sampleIDMap;
    }
    private ArrayList<String> retrieveStoredSampleIDs() throws SQLException{
        ArrayList<String> storedSamples = new ArrayList<>();
        Connection connection = DbManager.openConnection();
        try{
            PreparedStatement stmt = connection.prepareStatement(selectSampleIDsByTestID);
            stmt.setString(1,testID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                storedSamples.add(rs.getString(1));
            }
        } finally {
            connection.close();
        }
        return storedSamples;
    }
    private void deleteOutdatedSamples(ArrayList<List<Sample>> samplesByPatient,ArrayList<String> storedSampleIDs) throws SQLException{
        ArrayList<String> newSampleIDs = listAllNewSampleIDs(samplesByPatient);
        for(String stored: storedSampleIDs){
          if(!(newSampleIDs.contains(stored))){
              Connection connection = DbManager.openConnection();
              try{
                  PreparedStatement stmt = connection.prepareStatement(deleteSample);
                  stmt.setString(1,stored);
                  stmt.executeUpdate();
              } finally {
                  connection.close();
              }
            }
        }
    }
    private void updateExistingSamples(ArrayList<List<Sample>> samplesByPatient, ArrayList<String> storedSampleIDs) throws SQLException{
        Map<String,Sample> sampleIDMap = mapSamplesToSampleIDs(samplesByPatient);
        for(String newID: sampleIDMap.keySet()){
            if(storedSampleIDs.contains(newID)){
                Connection connection = DbManager.openConnection();
                PreparedStatement stmt = connection.prepareStatement(updateSamplePatientID);
                try{
                    stmt.setString(1,newID);
                    stmt.setString(2,sampleIDMap.get(newID).getPatientID());
                    stmt.executeUpdate();
                } finally {
                    connection.close();
                }
            }
        }
    }
    private void insertAllNewSamples(ArrayList<List<Sample>> samplesByPatient, ArrayList<String> storedSampleIDs) throws SQLException{
        Map<String,Sample> sampleIDMap = mapSamplesToSampleIDs(samplesByPatient);
        for(String newID: sampleIDMap.keySet()){
            if(!(storedSampleIDs.contains(newID))){
                Sample newSample = sampleIDMap.get(newID);
                newSample.setDateReceived(dateUpdated);
                newSample.setTestID(testID);
                newSample.insertNewSample();
            }
        }
    }
    private void deleteAndReplacePatients(ArrayList<List<Sample>> samplesByPatient) throws SQLException{
        //Create Patient List from Samples arranged by Patient
        ArrayList<Patient> patientList = new ArrayList<>();
        samplesByPatient.stream()
                .map(e -> new Patient(e.get(0)))
                .forEachOrdered(patientList::add);
        //Add Mother Name to Maternal Patients
        for(Patient p : patientList){
            if(p.getRelationship() == Patient.Relation.M){
                p.setLastName(interpreter.findLastName());
                p.setFirstName(interpreter.findFirstName());
            }
        }
        //Delete existing Patients
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(deletePatient);
        try{
            stmt.setString(1,testID);
            stmt.executeUpdate();
        } finally {
            connection.close();
        }
        //Insert new Patients
        for(Patient patient: patientList){
            try{
                patient.insertNewPatient();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void deleteOldEvents() throws SQLException{
        Connection connection = DbManager.openConnection();
        PreparedStatement genotypeStmt = connection.prepareStatement(deleteGenotype);
        PreparedStatement plasmaStmt = connection.prepareStatement(deletePlasma);
        try{
            genotypeStmt.setString(1,testID);
            plasmaStmt.setString(1,testID);
            genotypeStmt.executeUpdate();
            plasmaStmt.executeUpdate();
        } finally {
            connection.close();
        }
    }
    private void insertNewEvents() throws SQLException {
        //Set date and testID for each event
        ArrayList<Event> eventList = interpreter.consolidateAllEvents();
        for(Event event : eventList){
            LocalDate date = interpreter.findEventDate(event.getOriginalString(), dateUpdated);
            event.setDate(date);
            event.setTestID(testID);
        }
        //Insert Genotypes and Plasmas into their respective tables
        for(Event event : eventList){
            if(event.getType() == Event.LabTest.GENOTYPE){
                event.insertNewGenotype();
            }
            if(event.getType() == Event.LabTest.PLASMA){
                event.insertNewPlasma();
            }
        }
    }

    //Setters & Getters
    public void setCaseID(String id){
        caseID = id;
    }
    public void setTestID(String id){
        testID = id;
    }
    public void setDateUpdated(LocalDate date){
        dateUpdated = date;
    }

    public String getCaseID(){
        return caseID;
    }
    public String getTestID(){
        return testID;
    }
    public LocalDate getDateUpdated(){
        return dateUpdated;
    }

}
