package IntermediateObject;

import DataTransferObject.HashRow;

public class ChangeMap {

    //TODO: Refactor class as DTO Log class?
    private String caseID;
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
    public ChangeMap(){}
    public ChangeMap(HashRow newHash, HashRow storedHash){
        caseID = newHash.getCaseID();
        date = (newHash.getDate() != storedHash.getDate());
        motherName = (newHash.getMotherName() != storedHash.getMotherName());
        maternalPatientId = (newHash.getMaternalPatientId() != storedHash.getMaternalPatientId());
        paternalPatientId = (newHash.getPaternalPatientId() != storedHash.getPaternalPatientId());
        gestationGender = (newHash.getGestationGender() != storedHash.getGestationGender());
        testTypeCost = (newHash.getTestTypeCost() != storedHash.getTestTypeCost());
        referral = (newHash.getReferral() != storedHash.getReferral());
        genotypeA = (newHash.getGenotypeA() != storedHash.getGenotypeA());
        genotypeB = (newHash.getGenotypeB() != storedHash.getGenotypeB());
        firstDraw = (newHash.getFirstDraw() != storedHash.getFirstDraw());
        secondDraw = (newHash.getSecondDraw() != storedHash.getSecondDraw());
        thirdDraw = (newHash.getThirdDraw() != storedHash.getThirdDraw());
        result = (newHash.getResult() != storedHash.getResult());
        confirmation = (newHash.getConfirmation() != storedHash.getConfirmation());

    }

    //Setters & Getters
    public void setCaseID(String id) {
        caseID = id;
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
