package IntermediateObject;


import DataTransferObject.Event;
import logic.Interpreter;

public class EventString {

    //TODO: Better attribute names?
    private String event;
    private Event.LabTest type;
    private Event.PrimerSet primerSet;
    private Event.PlasmaNumber plasmaNumber;
    private String performedBy;
    private String plasmaUsed;
    private Integer plasmaGestation;


    //Constructors
    public EventString (String str, Event.LabTest eventType, Event.PrimerSet eventPrimerSet){
        event = str;
        type = eventType;
        primerSet = eventPrimerSet;
        performedBy = Interpreter.findPersonnel(str);
    }
    public EventString (String str, Event.LabTest eventType, Event.PlasmaNumber eventPlasmaNumber){
        event = str;
        type = eventType;
        plasmaNumber = eventPlasmaNumber;
        performedBy = Interpreter.findPersonnel(str);
    }


    //Setters and Getters
    public void setEvent(String str){
        event = str;
    }
    public void setType(Event.LabTest eventType){
        type = eventType;
    }
    public void setPrimerSet(Event.PrimerSet eventPrimerSet){
        primerSet = eventPrimerSet;
    }
    public void setPlasmaNumber(Event.PlasmaNumber eventPlasmaNumber){
        plasmaNumber = eventPlasmaNumber;
    }
    public void setPerformedBy(String str){
        performedBy = str;
    }
    public void setPlasmaUsed(String str){
        plasmaUsed = str;
    }
    public void setPlasmaGestation(Integer gestation){
        plasmaGestation = gestation;
    }
    public String getEvent(){
        return event;
    }
    public Event.LabTest getType(){
        return type;
    }
    public Event.PrimerSet getPrimerSet(){
        return primerSet;
    }
    public Event.PlasmaNumber getPlasmaNumber() {return plasmaNumber;}
    public String getPerformedBy() {
        return performedBy;
    }
    public String getPlasmaUsed(){
        return plasmaUsed;
    }
    public Integer getPlasmaGestation(){
        return plasmaGestation;
    }
}
