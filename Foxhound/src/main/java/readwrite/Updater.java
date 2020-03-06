package readwrite;

import DataTransferObject.*;
import DataTransferObject.Log;
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
    private Connection dbConnection;

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

    public void updateCase(Log log) throws SQLException, ParseException {
        dbConnection = DbManager.openConnection();
        try {
            updateChangedCells(log);
            updateCaseHash();
            log.setDateUpdated(interpreter.findRowDate());
            insertNewLog(log);
            dbConnection.commit();
        } catch(Exception e) {
            dbConnection.rollback();
            //TODO: Create Error and add to Error Table, commit
        } finally {
            dbConnection.close();
        }
    }

    //TODO: replace with Switch Statement
    private void updateChangedCells(Log log) throws SQLException{
        if(log.getMotherName()){
            updateMotherName();
        }
        if((log.getMaternalPatientId())||(log.getPaternalPatientId())){
            updatePatients();
            updateSamples();
        }
        if(log.getGestationGender()){
            updateGestationGender();
        }
        if(log.getTestTypeCost()){
            updateTestTypeCost();
        }
        if(log.getReferral()){
            updateSource();
        }
        if(log.getGenotypeA()){
            updateGenotypeA();
        }
        if(log.getGenotypeB()){
            updateGenotypeB();
        }
        if(log.getFirstDraw()){
            updateFirstDraw();
        }
        if(log.getSecondDraw()){
            updateSecondDraw();
        }
        if(log.getThirdDraw()){
            updateThirdDraw();
        }
        if(log.getResult()){
            updateResult();
        }
    }

    private void updateMotherName() throws SQLException {
        String motherLastName = interpreter.findLastName();
        String motherFirstName = interpreter.findFirstName();

        PreparedStatement stmt = dbConnection.prepareStatement(updateMotherName);
        stmt.setString(1,motherLastName);
        stmt.setString(2,motherFirstName);
        stmt.setString(3,caseID);
        stmt.executeUpdate();
    }
    private void updateSamples() throws SQLException{
        ArrayList<List<Sample>> samplesByPatient = interpreter.consolidateSamples();
        ArrayList<String> storedSampleIDs = retrieveStoredSampleIDs();
        //DELETE Samples who's IDs do not appear in cell
        deleteOutdatedSamples(samplesByPatient, storedSampleIDs);
        //If a stored sample ID is in newSampleIDs, UPDATE sample.patientID
        updateExistingSamples(samplesByPatient, storedSampleIDs);
        //If a new sample is not in db, INSERT into db
        insertAllNewSamples(samplesByPatient, storedSampleIDs);
    }
    private void updatePatients() throws SQLException{
        ArrayList<List<Sample>> samplesByPatient = interpreter.consolidateSamples();
        deleteAndReplacePatients(samplesByPatient);
    }
    private void updateGestationGender() throws SQLException{
        Integer gestation = interpreter.findFirstGestation();
        String gender = interpreter.findFirstGender();

        PreparedStatement gestationStmt = dbConnection.prepareStatement(updateGestation);
        PreparedStatement genderStmt = dbConnection.prepareStatement(updateGender);
        gestationStmt.setInt(1,gestation);
        gestationStmt.setString(2,testID);
        genderStmt.setString(1,gender);
        genderStmt.setString(2,testID);
        gestationStmt.executeUpdate();
        genderStmt.executeUpdate();
    }
    private void updateTestTypeCost() throws SQLException{
        Test.TestType type = interpreter.findFirstTestType();
        Integer cost = interpreter.findFirstCost();

        PreparedStatement stmt = dbConnection.prepareStatement(updateTestTypeCost);
        stmt.setString(1,type.name());
        stmt.setInt(2,cost);
        stmt.setString(3,testID);
        stmt.executeUpdate();
    }
    private void updateSource() throws SQLException{
        String source = interpreter.findSource();
        PreparedStatement stmt = dbConnection.prepareStatement(updateSource);
        stmt.setString(1,source);
        stmt.setString(2,caseID);
        stmt.executeUpdate();
    }
    private void updateGenotypeA() throws SQLException {
        //Delete from PrimerSet A
        deleteGenotypes(Event.PrimerSet.A);

        //Insert new Genotypes
        ArrayList<Event> genotypes = interpreter.findGenotypeA();
        insertGenotypes(genotypes);
    }
    private void updateGenotypeB() throws SQLException {
        //Delete from PrimerSets B and B96
        deleteGenotypes(Event.PrimerSet.B);
        deleteGenotypes(Event.PrimerSet.B96);

        //Insert new Genotypes
        ArrayList<Event> genotypes = interpreter.findGenotypeB();
        insertGenotypes(genotypes);
    }
    private void updateFirstDraw() throws SQLException{
        //Delete from First Draws
        deletePlasmas(Event.PlasmaNumber.FIRST);

        //Insert new Plasmas
        ArrayList<Event> plasmas = interpreter.findFirstDrawPlasmas();
        insertPlasmas(plasmas);
    }
    private void updateSecondDraw() throws SQLException {
        deletePlasmas(Event.PlasmaNumber.SECOND);
        ArrayList<Event> plasmas = interpreter.findSecondDrawPlasmas();
        insertPlasmas(plasmas);
        updateSamples();
    }
    private void updateThirdDraw() throws SQLException {
        deletePlasmas(Event.PlasmaNumber.THIRD);
        ArrayList<Event> plasmas = interpreter.findThirdDrawPlasmas();
        insertPlasmas(plasmas);
        updateSamples();
    }
    private void updateResult() throws SQLException {
        String result = inputRow.getResult();

        PreparedStatement stmt = dbConnection.prepareStatement(updateCaseResult);
        stmt.setString(1,result);
        stmt.setString(2, caseID);
        stmt.executeUpdate();
    }
    private void updateConfirmation(){}

    private void updateCaseHash() throws SQLException {
        Integer hash = inputRow.hashCode();

        PreparedStatement stmt = dbConnection.prepareStatement(updateCaseRowHash);
        stmt.setInt(1, hash);
        stmt.setString(2, caseID);
        stmt.executeUpdate();
    }


    //Sample and Patient Update Methods

    private ArrayList<String> listAllNewSampleIDs(ArrayList<List<Sample>> samplesByPatient){
        ArrayList<String> allSampleIDs = new ArrayList<>();
        for(List<Sample> sampleIDList: samplesByPatient){
            sampleIDList.stream()
                    .map(e -> e.getSampleID())
                    .forEach(allSampleIDs::add);
        }
        return allSampleIDs;
    }
    private Map<String,Sample> mapSamplesToSampleIDs(ArrayList<List<Sample>> samplesByPatient){
        Map<String,Sample> sampleIDMap = new HashMap<>();
        ArrayList<Sample> allSamples = new ArrayList<>();
        for(List<Sample> sampleList: samplesByPatient){
            sampleList.forEach(allSamples::add);
        }
        for(Sample sample: allSamples){
            sampleIDMap.put(sample.getSampleID(),sample);
        }
        return sampleIDMap;
    }
    private ArrayList<String> retrieveStoredSampleIDs() throws SQLException{
        ArrayList<String> storedSamples = new ArrayList<>();

        PreparedStatement stmt = dbConnection.prepareStatement(selectSampleIDsByTestID);
        stmt.setString(1,testID);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            storedSamples.add(rs.getString(1));
        }

        return storedSamples;
    }
    private void deleteOutdatedSamples(ArrayList<List<Sample>> samplesByPatient,ArrayList<String> storedSampleIDs) throws SQLException{
        ArrayList<String> newSampleIDs = listAllNewSampleIDs(samplesByPatient);
        for(String stored: storedSampleIDs){
          if(!(newSampleIDs.contains(stored))){
              PreparedStatement stmt = dbConnection.prepareStatement(deleteSample);
              stmt.setString(1,stored);
              stmt.executeUpdate();
            }
        }
    }
    private void updateExistingSamples(ArrayList<List<Sample>> samplesByPatient, ArrayList<String> storedSampleIDs) throws SQLException{
        Map<String,Sample> sampleIDMap = mapSamplesToSampleIDs(samplesByPatient);
        for(String newID: sampleIDMap.keySet()){
            if(storedSampleIDs.contains(newID)){
                PreparedStatement stmt = dbConnection.prepareStatement(updateSamplePatientID);
                stmt.setString(1,newID);
                stmt.setString(2,sampleIDMap.get(newID).getPatientID());
                stmt.executeUpdate();
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
                insertNewSample(newSample);
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
        PreparedStatement stmt = dbConnection.prepareStatement(deletePatient);
        stmt.setString(1,testID);
        stmt.executeUpdate();
        //Insert new Patients
        for(Patient patient: patientList){
            insertNewPatient(patient);
        }
    }

    //Event Update Methods

    private void deleteGenotypes(Event.PrimerSet primerSet) throws SQLException{
        PreparedStatement stmt = dbConnection.prepareStatement(deleteGenotype);
        stmt.setString(1,primerSet.name());
        stmt.setString(2,testID);
        stmt.executeUpdate();
    }
    private void deletePlasmas(Event.PlasmaNumber plasmaNumber) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(deletePlasma);
        stmt.setString(1, plasmaNumber.name());
        stmt.setString(2, testID);
        stmt.executeUpdate();
    }
    private void insertGenotypes(ArrayList<Event> genotypes) throws SQLException {
        for(Event g : genotypes){
            //Set date and testID
            LocalDate date = interpreter.findEventDate(g.getOriginalString(), dateUpdated);
            g.setDate(date);
            g.setTestID(testID);

            //Insert into Database
            insertNewGenotype(g);
        }
    }
    private void insertPlasmas(ArrayList<Event> plasmas) throws SQLException {
        for(Event p : plasmas){
            //Set date and testID
            LocalDate date = interpreter.findEventDate(p.getOriginalString(), dateUpdated);
            p.setDate(date);
            p.setTestID(testID);

            //Insert into Database
            insertNewPlasma(p);
        }
    }


    //DML
    private void insertNewSample(Sample s) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertSample);
        stmt.setString(1,s.getSampleID());
        stmt.setObject(2,s.getDateReceived());
        stmt.setString(3,s.getTestID());
        stmt.setString(4,s.getPatientID());
        stmt.executeUpdate();
    }
    private void insertNewPatient(Patient p) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertPatient);
        stmt.setString(1,p.getPatientID());
        stmt.setString(2,p.getLastName());
        stmt.setString(3,p.getFirstName());
        stmt.setString(4,p.getRelationship().name());
        stmt.executeUpdate();
    }
    private void insertNewGenotype(Event g) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertGenotype);
        stmt.setObject(1, g.getDate());
        stmt.setString(2, g.getPrimerSet().name());
        stmt.setString(3, g.getPerformedBy());
        stmt.setString(4, g.getTestID());
        stmt.executeUpdate();
    }
    private void insertNewPlasma(Event p) throws SQLException{
        PreparedStatement stmt = dbConnection.prepareStatement(insertPlasma);
        stmt.setObject(1, p.getDate());
        stmt.setString(2, p.getPlasmaNumber().name());
        stmt.setString(3, p.getPlasmaUsed());
        stmt.setInt(4, p.getPlasmaGestation());
        stmt.setString(5, p.getPerformedBy());
        stmt.setString(6, p.getTestID());
        stmt.executeUpdate();
    }
    private void insertNewLog(Log log) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertLog);
        stmt.setString(1,caseID);
        stmt.setObject(2,dateUpdated);
        stmt.setString(3,log.getFileName());
        stmt.setBoolean(4,log.getDate());
        stmt.setBoolean(5,log.getMotherName());
        stmt.setBoolean(6,log.getMaternalPatientId());
        stmt.setBoolean(7,log.getPaternalPatientId());
        stmt.setBoolean(8,log.getGestationGender());
        stmt.setBoolean(9,log.getTestTypeCost());
        stmt.setBoolean(10,log.getReferral());
        stmt.setBoolean(11,log.getGenotypeA());
        stmt.setBoolean(12,log.getGenotypeB());
        stmt.setBoolean(13,log.getFirstDraw());
        stmt.setBoolean(14,log.getSecondDraw());
        stmt.setBoolean(15,log.getThirdDraw());
        stmt.setBoolean(16,log.getResult());
        stmt.setBoolean(17,log.getConfirmation());
        stmt.executeUpdate();
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
