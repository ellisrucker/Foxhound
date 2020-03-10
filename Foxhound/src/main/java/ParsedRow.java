import Entities.Event;
import Entities.Sample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class ParsedRow {

    //Constructed from a SeparatedRow object
    //Used to construct a construct a Case object
    //TODO: should there be a class of parsed strings first?
    private Date date;
    private String motherName;
    private ArrayList<Sample> maternalSampleList;
    private ArrayList<Sample> paternalSampleList;
    private ArrayList<Integer> gestationList;
    private ArrayList<String> genderList;
    private ArrayList<String> turnaroundList;
    private ArrayList<Integer> costList;
    private String referral;
    private ArrayList<Event> genotypeListA;
    private ArrayList<Event> genotypeListB;
    private ArrayList<Event> firstDrawList;
    private ArrayList<Event> secondDrawList;
    private ArrayList<Event> thirdDrawList;
    private ArrayList<String> resultList;
    private ArrayList<Event> confirmationList;
    private ArrayList<Integer> hashedRow;

}
