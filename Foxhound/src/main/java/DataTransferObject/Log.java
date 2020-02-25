package DataTransferObject;

import readwrite.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static readwrite.MySQL.insertLog;

public class Log {

    private String caseID;
    private LocalDate dateUpdated;
    private String fileName;

    private Boolean date;
    private Boolean motherName;
    private Boolean maternalPatientId;
    private Boolean paternalPatientId;
    private Boolean gestationGender;
    private Boolean testTypeCost;
    private Boolean referral;
    private Boolean genotypeA;
    private Boolean genotypeB;
    private Boolean firstDraw;
    private Boolean secondDraw;
    private Boolean thirdDraw;
    private Boolean result;
    private Boolean confirmation;


    //Constructors
    public Log(){}
    public Log(HashRow newHash, HashRow storedHash,String fileName){
        caseID = newHash.getCaseID();
        this.fileName = fileName;
        date = (!newHash.getDate().equals(storedHash.getDate()));
        motherName = (!newHash.getMotherName().equals(storedHash.getMotherName()));
        maternalPatientId = (!newHash.getMaternalPatientId().equals(storedHash.getMaternalPatientId()));
        paternalPatientId = (!newHash.getPaternalPatientId().equals(storedHash.getPaternalPatientId()));
        gestationGender = (!newHash.getGestationGender().equals(storedHash.getGestationGender()));
        testTypeCost = (!newHash.getTestTypeCost().equals(storedHash.getTestTypeCost()));
        referral = (!newHash.getReferral().equals(storedHash.getReferral()));
        genotypeA = (!newHash.getGenotypeA().equals(storedHash.getGenotypeA()));
        genotypeB = (!newHash.getGenotypeB().equals(storedHash.getGenotypeB()));
        firstDraw = (!newHash.getFirstDraw().equals(storedHash.getFirstDraw()));
        secondDraw = (!newHash.getSecondDraw().equals(storedHash.getSecondDraw()));
        thirdDraw = (!newHash.getThirdDraw().equals(storedHash.getThirdDraw()));
        result = (!newHash.getResult().equals(storedHash.getResult()));
        confirmation = (!newHash.getConfirmation().equals(storedHash.getConfirmation()));
    }

    //Data Access
    public void insertNewLog() throws SQLException {
        Connection connection = DbManager.openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(insertLog);
            stmt.setString(1,caseID);
            stmt.setObject(2,dateUpdated);
            stmt.setString(3,fileName);
            stmt.setBoolean(4,date);
            stmt.setBoolean(5,motherName);
            stmt.setBoolean(6,maternalPatientId);
            stmt.setBoolean(7,paternalPatientId);
            stmt.setBoolean(8,gestationGender);
            stmt.setBoolean(9,testTypeCost);
            stmt.setBoolean(10,referral);
            stmt.setBoolean(11,genotypeA);
            stmt.setBoolean(12,genotypeB);
            stmt.setBoolean(13,firstDraw);
            stmt.setBoolean(14,secondDraw);
            stmt.setBoolean(15,thirdDraw);
            stmt.setBoolean(16,result);
            stmt.setBoolean(17,confirmation);
            stmt.executeUpdate();
        } finally {
            connection.close();
        }
    }

    //Setters & Getters
    public void setCaseID(String id) {
        caseID = id;
    }
    public void setDateUpdated(LocalDate localDate) {
        dateUpdated = localDate;
    }
    public void setFileName(String file){
        fileName = file;
    }
    public void setDate(Boolean hasChanged){
        date = hasChanged;
    }
    public void setMotherName(Boolean hasChanged){
        motherName = hasChanged;
    }
    public void setMaternalPatientId(Boolean hasChanged){
        maternalPatientId = hasChanged;
    }
    public void setPaternalPatientId(Boolean hasChanged){
        paternalPatientId = hasChanged;
    }
    public void setGestationGender(Boolean hasChanged){
        gestationGender = hasChanged;
    }
    public void setTestTypeCost(Boolean hasChanged){
        testTypeCost = hasChanged;
    }
    public void setReferral(Boolean hasChanged){
        referral = hasChanged;
    }
    public void setGenotypeA(Boolean hasChanged){
        genotypeA = hasChanged;
    }
    public void setGenotypeB(Boolean hasChanged){
        genotypeB = hasChanged;
    }
    public void setFirstDraw(Boolean hasChanged){
        firstDraw = hasChanged;
    }
    public void setSecondDraw(Boolean hasChanged){
        secondDraw = hasChanged;
    }
    public void setThirdDraw(Boolean hasChanged){
        thirdDraw = hasChanged;
    }
    public void setResult(Boolean hasChanged){
        result = hasChanged;
    }
    public void setConfirmation(Boolean hasChanged){
        confirmation = hasChanged;
    }

    public String getCaseID() {
        return caseID;
    }
    public LocalDate getDateUpdated(){
        return dateUpdated;
    }
    public String getFileName(){
        return fileName;
    }
    public Boolean getDate() {
        return date;
    }
    public Boolean getMotherName() {
        return motherName;
    }
    public Boolean getMaternalPatientId() {
        return maternalPatientId;
    }
    public Boolean getPaternalPatientId() {
        return paternalPatientId;
    }
    public Boolean getGestationGender() {
        return gestationGender;
    }
    public Boolean getTestTypeCost() {
        return testTypeCost;
    }
    public Boolean getReferral() {
        return referral;
    }
    public Boolean getGenotypeA() {
        return genotypeA;
    }
    public Boolean getGenotypeB() {
        return genotypeB;
    }
    public Boolean getFirstDraw() {
        return firstDraw;
    }
    public Boolean getSecondDraw() {
        return secondDraw;
    }
    public Boolean getThirdDraw() {
        return thirdDraw;
    }
    public Boolean getResult() {
        return result;
    }
    public Boolean getConfirmation() {
        return confirmation;
    }
}
