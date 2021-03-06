package dto;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static utility.MySQL.insertFilteredCase;

public class ExcelRow implements Insertable {

    //Object representation of current row
    //Each cell of currentRow becomes a String attribute
    //caseID is used only when row is added to complex case table
    private String date;
    private String motherName;
    private String maternalPatientId;
    private String paternalPatientId;
    private String gestationGender;
    private String testTypeCost;
    private String referral;
    private String genotypeA;
    private String genotypeB;
    private String firstDraw;
    private String secondDraw;
    private String thirdDraw;
    private String result;
    private String confirmation;
    private String caseID;

    @Override
    public int hashCode(){
        int hash = Objects.hash(date,
                motherName,
                maternalPatientId,
                paternalPatientId,
                gestationGender,
                testTypeCost,
                referral,
                genotypeA,
                genotypeB,
                firstDraw,
                secondDraw,
                thirdDraw,
                result,
                confirmation);
        return hash;
    }

    //Constructors
    public ExcelRow(){
    }
    public ExcelRow(String[] currentRow){
        date = currentRow[0];
        motherName = currentRow[1];
        maternalPatientId = currentRow[2];
        paternalPatientId = currentRow[3];
        gestationGender = currentRow[4];
        testTypeCost = currentRow[5];
        referral = currentRow[6];
        genotypeA = currentRow[9];
        genotypeB = currentRow[10];
        firstDraw = currentRow[11];
        secondDraw = currentRow[12];
        thirdDraw = currentRow[13];
        result = currentRow[16];
        confirmation = currentRow[17];
    }

    public void insert(Connection dbConnection) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(insertFilteredCase);
        stmt.setString(1,caseID);
        stmt.setString(2,date);
        stmt.setString(3,motherName);
        stmt.setString(4,maternalPatientId);
        stmt.setString(5,paternalPatientId);
        stmt.setString(6,gestationGender);
        stmt.setString(7,testTypeCost);
        stmt.setString(8,referral);
        stmt.setString(9,genotypeA);
        stmt.setString(10,genotypeB);
        stmt.setString(11,firstDraw);
        stmt.setString(12,secondDraw);
        stmt.setString(13,thirdDraw);
        stmt.setString(14,result);
        stmt.setString(15,confirmation);
        stmt.executeUpdate();
    }


    //Setters and Getters

    //Setters are for Unit Testing Only
    public void setDate(String newDate){
        date = newDate;
    }
    public void setMotherName(String name){
        motherName = name;
    }
    public void setMaternalPatientId(String mID){
        maternalPatientId = mID;
    }
    public void setPaternalPatientId(String pID){
        paternalPatientId = pID;
    }
    public void setGestationGender(String gg){
        gestationGender = gg;
    }
    public void setTestTypeCost(String ttc){
        testTypeCost = ttc;
    }
    public void setReferral(String newReferral){
        referral = newReferral;
    }
    public void setGenotypeA(String geno){
        genotypeA = geno;
    }
    public void setGenotypeB(String geno){
        genotypeB = geno;
    }
    public void setFirstDraw(String newFirstDraw){
        firstDraw = newFirstDraw;
    }
    public void setSecondDraw(String newSecondDraw){
        secondDraw = newSecondDraw;
    }
    public void setThirdDraw(String newThirdDraw){
        thirdDraw = newThirdDraw;
    }
    public void setResult(String newResult){
        result = newResult;
    }
    public void setConfirmation(String newConfirmation){
        confirmation = newConfirmation;
    }
    public void setCaseID(String id) {
        caseID = id;
    }

    public String getDate(){
        return date;
    }
    public String getMotherName(){
        return motherName;
    }
    public String getMaternalPatientId(){
        return maternalPatientId;
    }
    public String getPaternalPatientId(){
        return paternalPatientId;
    }
    public String getGestationGender(){
        return gestationGender;
    }
    public String getTestTypeCost(){
        return testTypeCost;
    }
    public String getReferral(){
        return referral;
    }
    public String getGenotypeA(){
        return genotypeA;
    }
    public String getGenotypeB(){
        return genotypeB;
    }
    public String getFirstDraw(){
        return firstDraw;
    }
    public String getSecondDraw(){
        return secondDraw;
    }
    public String getThirdDraw(){
        return thirdDraw;
    }
    public String getResult(){
        return result;
    }
    public String getConfirmation(){
        return confirmation;
    }
    public String getCaseID(){
        return caseID;
    }

}

