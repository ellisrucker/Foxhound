package logic;

import DataTransferObject.Sample;
import IntermediateObject.SampleString;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.EnumMap;

import static java.util.stream.Collectors.*;

public class Interpreter {

    //TODO: Refactor class to accept SeparatedRow instead of String? Or make everything static?

    private String str;

    private static Pattern generalID = Pattern.compile("[a-zA-Z]{3,}[\\d]{5,}");
    private static Pattern relationString = Pattern.compile("[^a-zA-Z][PpCc][\\d]");
    private static Pattern gestationString = Pattern.compile("[\\d][Ww]");

    private static String idRgx = "[a-zA-Z]{3,}[\\d]{5,}";
    private static String relationRgx = "[PpCc][\\d]";
    private static String gestationRgx = "[\\d][Ww]";

    private static String[] relationRgxArray = new String[]{"[Pp]1","[Pp]2","[Pp]3","[Pp]4","[Pp]5"};


    private static Pattern P1 = Pattern.compile("[^a-zA-Z][Pp]1");
    private static Pattern P2 = Pattern.compile("[^a-zA-Z][Pp]2");
    private static Pattern P3 = Pattern.compile("[^a-zA-Z][Pp]3");
    private static Pattern P4 = Pattern.compile("[^a-zA-Z][Pp]4");
    private static Pattern P5 = Pattern.compile("[^a-zA-Z][Pp]5");


    private static Pattern boy = Pattern.compile("[Bb]oy");
    private static Pattern girl = Pattern.compile("[Gg]irl");
    private static Pattern boyGirl = Pattern.compile("[Bb]oy/[Gg]irl");
    private static Pattern girlBoy = Pattern.compile("[Gg]irl/[Bb]oy");
    private static Pattern twin = Pattern.compile("[Tt]win");


    public final Comparator<SampleString> SS_COMPARATOR = (ss1, ss2) -> {
        Integer idNumber1 = idToNumber(ss1.getId());
        Integer idNumber2 = idToNumber(ss2.getId());
        if(idNumber1 == idNumber2){
            return 0;
        } else if(idNumber1 > idNumber2){
            return 1;
        } else{
            return -1;
        }
    };


    private final Comparator<String> ID_COMPARATOR = (id1, id2) -> {
        Integer idNumber1 = idToNumber(findID(id1));
        Integer idNumber2 = idToNumber(findID(id2));
        if(idNumber1 == idNumber2){
            return 0;
        } else if(idNumber1 > idNumber2){
            return 1;
        } else{
            return -1;
        }
    };


    private String[] splitFullName(String fullName){
        return fullName.split(" ", 2);
    }

    //Splits on zero-length matches that precede an id, but
    //that do not follow a letter (to avoid splitting ids)
    public ArrayList<String> isolateSamples(){
        String[] sampleArray = str.split("(?<![a-zA-z])(?=([a-zA-Z]{3,}[\\d]{5,}))");
        ArrayList<String> onlySamples = new ArrayList<>();
        for(String element: sampleArray){
            Matcher sampleMatcher = generalID.matcher(element);
            if(sampleMatcher.find()){
                onlySamples.add(element);
            }
        }
        return onlySamples;
    }
    public String findFirstName(){
        String[] nameArray = splitFullName(str);
        return nameArray[0];
    }
    public String findLastName(){
        String[] nameArray = splitFullName(str);
        return nameArray[1];
    }

    public LocalDate stringToDate() throws ParseException {
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("M[M]/d[d]/yyyy");
        return LocalDate.parse(str,datePattern);
    }



    public ArrayList<List<SampleString>> groupIDsByPatient(){
        Map<SampleString.RELATION, List<SampleString>> idsByPatient = this.isolateSamples()
                .stream()
                .map(s -> new SampleString(s))
                .collect(groupingBy(
                        SampleString::getRelation
                ));

        ArrayList<List<SampleString>> sortedIDsByPatient =
                idsByPatient.values()
                .stream()
                .map(list -> list.stream()
                        .sorted()
                        .collect(toList()))
                .collect(toCollection(ArrayList::new));

        return sortedIDsByPatient;

    }

    public ArrayList<SampleString> retrieveMaternalIDs(){
        ArrayList<SampleString> maternalIDs = this.isolateSamples()
                .stream()
                .map(s -> new SampleString(s, SampleString.RELATION.M))
                .sorted()
                .collect(toCollection(ArrayList::new));
        return maternalIDs;
    }

    public static String findFirstID(ArrayList<SampleString> patientSamples){
        SampleString firstSample = patientSamples.get(0);
        return firstSample.getId();
    }
    public String findFirstMaternalID(){
        ArrayList<SampleString> maternalIDs = this.retrieveMaternalIDs();
        SampleString firstMaternalSample = maternalIDs.get(0);
        return firstMaternalSample.getId();
    }

    public String findFirstID(){
        ArrayList<String> idArray = new ArrayList<>();
        Matcher idMatcher = generalID.matcher(str);
        while(idMatcher.find()){
            idArray.add(idMatcher.group());
        }
        String firstID;
        if(idArray.size() == 1){
            firstID = idArray.get(0);

        } else {
            Collections.sort(idArray, ID_COMPARATOR);
            firstID = idArray.get(0);
        }
        return firstID;
    }

    public static String findID(String str){
        String id = null;
        Matcher idMatcher = generalID.matcher(str);
        //May need to switch to while loop if it fails to find id
        if (idMatcher.find()) {
            return idMatcher.group();
        } else {
            return null;
        }
    }

    public String findGender(){
        Matcher boyMatcher = boy.matcher(str);
        Matcher girlMatcher = girl.matcher(str);
        Matcher boyGirlMatcher = boyGirl.matcher(str);
        Matcher girlBoyMatcher = girlBoy.matcher(str);

        if (boyGirlMatcher.find()) {
            return "Heterotwins";
        }
        else if (girlBoyMatcher.find()){
            return "Heterotwins";
        }
        else if (boyMatcher.find()){
            return "Male";
        }
        else if (girlMatcher.find()){
            return "Female";
        }
        else {
            return null;
        }
    }
    public static Integer idToNumber(String id) {
        id = id.replaceAll( "\\D", "");
        Integer idNumber = Integer.parseInt(id);
        return idNumber;
    }

    public boolean isHavingTwins(){
        Matcher twinMatcher = twin.matcher(str);
        return twinMatcher.find();
    }

    public Interpreter (String rawString){
        str = rawString;
    }
}
