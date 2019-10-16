package logic;

import readwrite.SeparatedRow;
import readwrite.Update;

import java.text.ParseException;

public class Comparison {

    private SeparatedRow inputRow;

    //Implement Comparison Functionality later
    /*public boolean caseStateChanged(String[] currentRow){
        ArrayList<Integer> newState = new ArrayList<>();
        for (String str : currentRow){
            newState.add(stringHash(str));
        }
        return this.caseState != newState;
    }
    public static boolean caseExists(String stringId){
        Integer id = idToNumber(stringId);
        return allCases.contains(id);
    }
    */


    public Comparison (SeparatedRow inputRow){
        this.inputRow = inputRow;
    }
}
