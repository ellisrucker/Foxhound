package dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utility.MySQL.insertHash;

public class HashRow implements Insertable {

    private String caseID;
    private Integer date;
    private Integer motherName;
    private Integer maternalPatientId;
    private Integer paternalPatientId;
    private Integer gestationGender;
    private Integer testTypeCost;
    private Integer referral;
    private Integer genotypeA;
    private Integer genotypeB;
    private Integer firstDraw;
    private Integer secondDraw;
    private Integer thirdDraw;
    private Integer result;
    private Integer confirmation;

    //Constructors
    public HashRow(){
    }
    public HashRow(String caseID, ExcelRow inputRow){
        this.caseID = caseID;
        date = inputRow.getDate().hashCode();
        motherName = inputRow.getMotherName().hashCode();
        maternalPatientId = inputRow.getMaternalPatientId().hashCode();
        paternalPatientId = inputRow.getPaternalPatientId().hashCode();
        gestationGender = inputRow.getGestationGender().hashCode();
        testTypeCost = inputRow.getTestTypeCost().hashCode();
        referral = inputRow.getReferral().hashCode();
        genotypeA = inputRow.getGenotypeA().hashCode();
        genotypeB = inputRow.getGenotypeB().hashCode();
        firstDraw = inputRow.getFirstDraw().hashCode();
        secondDraw = inputRow.getSecondDraw().hashCode();
        thirdDraw = inputRow.getThirdDraw().hashCode();
        result = inputRow.getResult().hashCode();
        confirmation = inputRow.getConfirmation().hashCode();
    }
    //Update column labels if MySQL column names change
    public HashRow(ResultSet rs) throws SQLException{
        rs.first();
        caseID = rs.getString("caseID");
        date = rs.getInt("date");
        motherName = rs.getInt("motherName");
        maternalPatientId = rs.getInt("maternalPatientId");
        paternalPatientId = rs.getInt("paternalPatientId");
        gestationGender = rs.getInt("gestationGender");
        testTypeCost = rs.getInt("testTypeCost");
        referral = rs.getInt("referral");
        genotypeA = rs.getInt("genotypeA");
        genotypeB = rs.getInt("genotypeB");
        firstDraw = rs.getInt("firstDraw");
        secondDraw = rs.getInt("secondDraw");
        thirdDraw = rs.getInt("thirdDraw");
        result = rs.getInt("result");
        confirmation = rs.getInt("confirmation");
    }

    public void insert(Connection dbConnection) throws SQLException{
        PreparedStatement stmt = dbConnection.prepareStatement(insertHash);
        stmt.setString(1,caseID);
        stmt.setInt(2,date);
        stmt.setInt(3,motherName);
        stmt.setInt(4,maternalPatientId);
        stmt.setInt(5,paternalPatientId);
        stmt.setInt(6,gestationGender);
        stmt.setInt(7,testTypeCost);
        stmt.setInt(8,referral);
        stmt.setInt(9,genotypeA);
        stmt.setInt(10,genotypeB);
        stmt.setInt(11,firstDraw);
        stmt.setInt(12,secondDraw);
        stmt.setInt(13,thirdDraw);
        stmt.setInt(14,result);
        stmt.setInt(15,confirmation);
        stmt.executeUpdate();
    }

    public void setDate(Integer newDate){
        date = newDate;
    }
    public void setMotherName(Integer name){
        motherName = name;
    }
    public void setMaternalPatientId(Integer mID){
        maternalPatientId = mID;
    }
    public void setPaternalPatientId(Integer pID){
        paternalPatientId = pID;
    }
    public void setGestationGender(Integer gg){
        gestationGender = gg;
    }
    public void setTestTypeCost(Integer ttc){
        testTypeCost = ttc;
    }
    public void setReferral(Integer newReferral){
        referral = newReferral;
    }
    public void setGenotypeA(Integer geno){
        genotypeA = geno;
    }
    public void setGenotypeB(Integer geno){
        genotypeB = geno;
    }
    public void setFirstDraw(Integer newFirstDraw){
        firstDraw = newFirstDraw;
    }
    public void setSecondDraw(Integer newSecondDraw){
        secondDraw = newSecondDraw;
    }
    public void setThirdDraw(Integer newThirdDraw){
        thirdDraw = newThirdDraw;
    }
    public void setResult(Integer newResult){
        result = newResult;
    }
    public void setConfirmation(Integer newConfirmation){
        confirmation = newConfirmation;
    }
    public void setCaseID(String id) {
        caseID = id;
    }

    public Integer getDate(){
        return date;
    }
    public Integer getMotherName(){
        return motherName;
    }
    public Integer getMaternalPatientId(){
        return maternalPatientId;
    }
    public Integer getPaternalPatientId(){
        return paternalPatientId;
    }
    public Integer getGestationGender(){
        return gestationGender;
    }
    public Integer getTestTypeCost(){
        return testTypeCost;
    }
    public Integer getReferral(){
        return referral;
    }
    public Integer getGenotypeA(){
        return genotypeA;
    }
    public Integer getGenotypeB(){
        return genotypeB;
    }
    public Integer getFirstDraw(){
        return firstDraw;
    }
    public Integer getSecondDraw(){
        return secondDraw;
    }
    public Integer getThirdDraw(){
        return thirdDraw;
    }
    public Integer getResult(){
        return result;
    }
    public Integer getConfirmation(){
        return confirmation;
    }
    public String getCaseID(){
        return caseID;
    }
}
