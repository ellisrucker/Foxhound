package readwrite;


import java.time.LocalDate;
import java.util.Objects;

public class SeparatedRow {

    //Object representation of current row
    //Each cell of currentRow becomes a String attribute
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

    public SeparatedRow (){

    }

    public SeparatedRow (String[] currentRow){
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

    //Setters and Getters

    //Setters are for Unit Testing Only
    //Class fields will ultimately be declared final
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

}

