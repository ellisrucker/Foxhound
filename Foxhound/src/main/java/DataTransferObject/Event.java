package DataTransferObject;

import logic.Interpreter;
import readwrite.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static readwrite.MySQL.*;

public class Event {

    //TODO: Refactor into abstract class Event, with classes Plasma and Genotype?
    private Integer eventID;
    private LocalDate date;
    private LabTest type;
    private String performedBy;
    private String originalString;

    private PrimerSet primerSet;
    private PlasmaNumber plasmaNumber;
    private String plasmaUsed;
    //Initialization avoids null pointer in stmt.setInt(): switch to int?
    private Integer plasmaGestation = 0;

    //Future Foreign Key
    private String testID;

    public enum LabTest {
        GENOTYPE, PLASMA, CONFIRMATION, UNKNOWN
    }
    public enum PlasmaNumber {
        FIRST, SECOND, THIRD, FOURTH, FIFTH, UNKNOWN
    }

    public enum PrimerSet {
        A, B, A96, B96, X, Y, CFTR, UNKNOWN
    }

    //Constructors
    public Event(){
    }
    public Event(String str, LabTest eventType, PrimerSet primerSet){
        type = eventType;
        this.primerSet = primerSet;
        performedBy = Interpreter.findPersonnel(str);
        originalString = str;
    }
    public Event(String str, LabTest eventType, PlasmaNumber plasmaNumber){
        type = eventType;
        this.plasmaNumber = plasmaNumber;
        performedBy = Interpreter.findPersonnel(str);
        originalString = str;
    }

    //Setters & Getters
    public void setEventID(Integer id){
        eventID = id;
    }
    public void setDate(LocalDate newDate){
        date = newDate;
    }
    public void setType(LabTest testType){
        type = testType;
    }
    public void setPrimerSet(PrimerSet set){
        primerSet = set;
    }
    public void setPlasmaNumber(PlasmaNumber number){
        plasmaNumber = number;
    }
    public void setPerformedBy(String name){
        performedBy = name;
    }
    public void setPlasmaUsed(String sample){
        plasmaUsed = sample;
    }
    public void setPlasmaGestation(Integer gestation){
        plasmaGestation = gestation;
    }
    public void setTestID(String str){
        testID = str;
    }
    public void setOriginalString(String str){
        originalString = str;
    }

    public Integer getEventID(){
        return eventID;
    }
    public LocalDate getDate(){
        return date;
    }
    public LabTest getType(){
        return type;
    }
    public PrimerSet getPrimerSet(){
        return primerSet;
    }
    public PlasmaNumber getPlasmaNumber(){
        return plasmaNumber;
    }
    public String getPerformedBy(){
        return performedBy;
    }
    public String getPlasmaUsed(){
        return plasmaUsed;
    }
    public Integer getPlasmaGestation(){
        return plasmaGestation;
    }
    public String getTestID(){
        return testID;
    }
    public String getOriginalString(){
        return originalString;
    }
}
