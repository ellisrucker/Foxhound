package IntermediateObject;

import logic.Interpreter;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SampleString implements Comparable<SampleString>{

    private String sample;
    private String id;
    private Relation relation;

    @Override
    public String toString(){
        return String.format(id +": " + relation.name());
    }
    @Override
    public int compareTo(SampleString o) {
        return Interpreter.idToNumber(this.id) - Interpreter.idToNumber(o.id);
    }
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        SampleString ss = (SampleString) o;
        return ss.sample == this.sample
                && ss.id == this.id
                && ss.relation == this.relation;
    }
    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + sample.hashCode();
        hash = 31 * hash + id.hashCode();
        hash = 31 * hash + relation.hashCode();
        return hash;
    }

    public enum Relation {M,P1,P2,P3,P4,P5,UNKNOWN}

    private static Pattern paternalRelation = Pattern.compile("[^a-zA-Z][Pp][\\d]");

    public final Comparator<SampleString> COMPARE_BY_ID = (ss1, ss2) -> {
        Integer idNumber1 = Interpreter.idToNumber(ss1.id);
        Integer idNumber2 = Interpreter.idToNumber(ss2.id);
        if (idNumber1 == idNumber2){
            return 0;
        }
        else if(idNumber1 > idNumber2){
            return 1;
        }
        else {
            return -1;
        }
    };

    //Comparable? or stick with comparator above?

    public SampleString (String str){
        sample = str;
        id = Interpreter.findID(str);
        relation = findRelation(str);
    }
    public SampleString (String str, Relation relation){
        sample = str;
        id = Interpreter.findID(str);
        this.relation = relation;
    }

    public static Relation findRelation(String idString) {
        Matcher relationMatcher = paternalRelation.matcher(idString);
        String relation;
        if(!relationMatcher.find()){
            return Relation.P1;
        } else {
            relation = relationMatcher.group().toUpperCase();
        }
        if(relation.contains("P1")){
            return Relation.P1;
        } else if (relation.contains("P2")){
            return Relation.P2;
        } else if (relation.contains("P3")){
            return Relation.P3;
        } else if (relation.contains("P4")){
            return Relation.P4;
        } else if (relation.contains("P5")){
            return Relation.P5;
        } else return Relation.UNKNOWN;

    }

    //Setters & Getters

    public void setSample(String str){
        sample = str;
    }
    public void setId(String str){
        id = Interpreter.findID(str);
    }
    public void setRelation(String str){
        relation = findRelation(str);
    }
    public void setRelation(Relation relation){
        this.relation = relation;
    }

    public String getSample(){
        return this.sample;
    }
    public String getId(){
        return this.id;
    }
    public Relation getRelation(){
        return this.relation;
    }



}