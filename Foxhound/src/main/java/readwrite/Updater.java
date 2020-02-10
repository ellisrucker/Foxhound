package readwrite;

import DataTransferObject.ExcelRow;
import DataTransferObject.Test;
import IntermediateObject.ChangeMap;
import logic.Interpreter;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

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

    public void updateCase(ChangeMap changeMap) throws SQLException {
        if(changeMap.getMotherName()){
            updateMotherName();
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
    //Will be called if either maternalID or paternalID cells are changed
    //Not pretty. Could use some cleaning up. Maybe make sampleUpdater class?
    /*
    public void updateSamplesAndPatients() throws SQLException{
        ArrayList<List<SampleString>> samplesByPatient = interpreter.consolidateSampleStrings();
        ArrayList<SampleString> allSampleStrings = listAllSampleStrings(samplesByPatient);
        Map<String,SampleString> sampleStringMap = new HashMap<>();
        allSampleStrings.stream()
                .forEachOrdered(e -> sampleStringMap.put(e.getId(),e));
        ArrayList<String> newSampleIDs = listAllSampleIDs(samplesByPatient);
        ArrayList<String> storedSampleIDs = retrieveStoredSampleIDs();

        deleteOutdatedSamples(newSampleIDs,storedSampleIDs);
        //if a stored sampleID is in newSampleIDs, UPDATE sample.patientID
        for(String stored: storedSampleIDs){
            if(newSampleIDs.contains(stored)){
                String newPatientID = sampleStringMap.get(stored).getPatientID();
                updateExistingSample(newPatientID,stored);
            }
        }
        //if a new sample is not in db, INSERT into db
        for(String newSample: newSampleIDs){
           if(!(storedSampleIDs.contains(newSample))){
               SampleString ss = sampleStringMap.get(newSample);
               Sample sampleDTO = new Sample(ss, ss.getPatientID(), testID, dateUpdated);
               sampleDTO.insertNewSample();
           }
        }
        //Delete all patients under given testID, and insert new patients

    }
     */

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
    /*
    private ArrayList<String> listAllSampleIDs(ArrayList<List<SampleString>> samplesByPatient){
        ArrayList<String> allSamples = new ArrayList<>();
        for(List<SampleString> sampleList : samplesByPatient){
            sampleList.stream()
                    .map(e -> e.getId())
                    .forEachOrdered(allSamples::add);
        }
        return allSamples;
    }
    private ArrayList<String> listAllPatients(ArrayList<List<SampleString>> samplesByPatient){
        ArrayList<String> allPatients = new ArrayList<>();

    }
     */
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
    private void deleteOutdatedSamples(ArrayList<String> newSamples,ArrayList<String> storedSamples) throws SQLException{
        for(String stored: storedSamples){
          if(!(newSamples.contains(stored))){
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
    private void updateExistingSample(String patientID, String sampleID) throws SQLException{
        Connection connection = DbManager.openConnection();
        PreparedStatement stmt = connection.prepareStatement(updateSamplePatientID);
        try{
            stmt.setString(1,patientID);
            stmt.setString(2,sampleID);
            stmt.executeUpdate();
        } finally {
            connection.close();
        }
    }
    private void updateSamplesInDB(ArrayList<String> newSamples,ArrayList<String> storedSamples) throws SQLException{
        for(String stored: storedSamples){
            if(newSamples.contains(stored)){

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
