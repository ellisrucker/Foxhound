package logic;

import DataTransferObject.Test;
import DataTransferObject.Test.TestType;
import IntermediateObject.SampleString;
import readwrite.SeparatedRow;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

public class Interpreter {


    private SeparatedRow inputRow;

    private static Pattern generalID = Pattern.compile("[a-zA-Z]{3,}[\\d]{5,}");
    private static Pattern relationRegex = Pattern.compile("[^a-zA-Z][PpCc][\\d]");
    private static Pattern gestationRegex = Pattern.compile("[\\d]{1,2}[Ww]");
    private static Pattern costRegex = Pattern.compile("[\\d]{3,4}");


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


    //TestType Strings and Delimiters
    private static String early = "(?i)early";
    private static String turnaround = "[123][Ww]";
    private static String oneWeek = "1[\\s]*[Ww]";
    private static String twoWeek = "2[\\s]*[Ww]";
    private static String threeWeek = "3[\\s]*[Ww]";
    private static String gender = "(?i)gender";
    private static String surrogate = "(?i)surrogate";
    private static String ivf = "(?i)IVF";
    private static String maternity = "(?i)maternity";
    private static String matPat = "(?i)mat.*?pat";
    private static String patMat = "(?i)pat.*?mat";
    private static String testTypeDelimiters = "(?="+
            early + "|" +
            turnaround + "|"+
            gender + "|"+
            surrogate + "|"+
            ivf + "|"+
            maternity + "|"+
            matPat + "|"+
            patMat + ")";

    private static String[] testTypeArray = new String[]{early, turnaround, gender, surrogate, ivf, maternity,
            matPat, patMat};

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

    //Name Processing

    private String[] splitFullName(String fullName){
        return fullName.split(" ", 2);
    }

    public String findFirstName(){
        String[] nameArray = splitFullName(inputRow.getMotherName());
        return nameArray[0];
    }
    public String findLastName(){
        String[] nameArray = splitFullName(inputRow.getMotherName());
        return nameArray[1];
    }

    //Date Processing

    public LocalDate findRowDate() throws ParseException {
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("M[M]/d[d]/yyyy");
        return LocalDate.parse(inputRow.getDate(),datePattern);
    }


    //Identifier Processing

    public ArrayList<List<SampleString>> consolidateSampleStrings(){
        ArrayList<List<SampleString>> samplesByPatient = new ArrayList<>();
        samplesByPatient.add(retrieveMaternalIDs());
        groupIDsByPatient().forEach(samplesByPatient::add);
        return samplesByPatient;
    }

    public ArrayList<SampleString> retrieveMaternalIDs(){
        ArrayList<SampleString> maternalIDs = isolateSamples(inputRow.getMaternalPatientId())
                .stream()
                .map(s -> new SampleString(s, SampleString.Relation.M))
                .sorted()
                .collect(toCollection(ArrayList::new));
        return maternalIDs;
    }

    public ArrayList<List<SampleString>> groupIDsByPatient(){
        Map<SampleString.Relation, List<SampleString>> idsByPatient = isolateSamples(inputRow.getPaternalPatientId())
                .stream()
                .map(s -> new SampleString(s))
                .collect(groupingBy(
                        SampleString::getRelation
                ));

        return idsByPatient.values()
                .stream()
                .map(list -> list.stream()
                        .sorted()
                        .collect(toList()))
                .collect(toCollection(ArrayList::new));


    }

    //Splits on zero-length matches that precede an id, but
    //that do not follow a letter (to avoid splitting ids)
    public static ArrayList<String> isolateSamples(String sampleCell){
        String[] sampleArray = sampleCell.split("(?<![a-zA-z])(?=([a-zA-Z]{3,}[\\d]{5,}))");
        ArrayList<String> onlySamples = new ArrayList<>();
        for(String element: sampleArray){
            Matcher sampleMatcher = generalID.matcher(element);
            if(sampleMatcher.find()){
                onlySamples.add(element);
            }
        }
        return onlySamples;
    }


    public String findFirstMaternalID(){
        ArrayList<SampleString> maternalIDs = this.retrieveMaternalIDs();
        SampleString firstMaternalSample = maternalIDs.get(0);
        return firstMaternalSample.getId();
    }

    public static String findID(String str){
        Matcher idMatcher = generalID.matcher(str);
        //May need to switch to while loop if it fails to find id
        if (idMatcher.find()) {
            return idMatcher.group();
        } else {
            return null;
        }
    }


    //Gestation Processing

    //TODO: Temporary method to add tests from simple cases
    public Integer findFirstGestation(){
        ArrayList<String> stringsFromCell = isolateGestationGender(inputRow.getGestationGender());
        return findGestation(stringsFromCell.get(0));
    }

    //May not need (?<![\\d]) condition
    public static ArrayList<String> isolateGestationGender(String cell){
        String[] gestationGenderArray = cell.split("(?<![\\d])(?=[\\d]{1,2}[Ww])");
        ArrayList<String> onlyGestationGender = new ArrayList<>();
        for(String element: gestationGenderArray){
            Matcher gestationMatcher = gestationRegex.matcher(element);
            if (gestationMatcher.find()){
                onlyGestationGender.add(element);
            }
        }
        return onlyGestationGender;
    }
    public Integer findFirstCost(){
        ArrayList<String> stringsFromCell = isolateTestTypeCost(inputRow.getTestTypeCost());
        return findCost(stringsFromCell.get(0));
    }

    public TestType findFirstTestType(){
        ArrayList<String> stringsFromCell = isolateTestTypeCost(inputRow.getTestTypeCost());
        return findTestType(stringsFromCell.get(0));
    }

    public static TestType findTestType(String str){
        Map<Pattern,TestType> testTypeMap = new HashMap<>();
        testTypeMap.put(Pattern.compile(threeWeek),TestType.THREE_WEEK);
        testTypeMap.put(Pattern.compile(oneWeek),TestType.ONE_WEEK);
        testTypeMap.put(Pattern.compile(early),TestType.EARLY);
        testTypeMap.put(Pattern.compile(surrogate),TestType.SURROGATE);
        testTypeMap.put(Pattern.compile(ivf),TestType.IVF);
        testTypeMap.put(Pattern.compile(maternity),TestType.MATERNITY);
        testTypeMap.put(Pattern.compile(gender),TestType.GENDER);
        testTypeMap.put(Pattern.compile(twoWeek),TestType.TWO_WEEK);
        testTypeMap.put(Pattern.compile(matPat),TestType.MAT_PAT);
        testTypeMap.put(Pattern.compile(patMat),TestType.MAT_PAT);

        TestType tt = null;
        for (Map.Entry<Pattern,TestType> entry: testTypeMap.entrySet()){
            Matcher matcher = entry.getKey().matcher(str);
            if (matcher.find()){
                tt = entry.getValue();
                break;
            } else {
                continue;
            }
        }
        //Avoids Null Pointer Exception when inserting into DB
        if(tt == null){
            tt = TestType.UNKNOWN;
        }
        return tt;
    }


    public static ArrayList<String> isolateTestTypeCost(String cell){
        String[] testTypeCostArray = cell.split(testTypeDelimiters);
        ArrayList<String> onlyTestTypeCost = new ArrayList<>();
        for(String element: testTypeCostArray){
            Matcher costMatcher = costRegex.matcher(element);
            if(costMatcher.find() | containsTestType(element)){
                onlyTestTypeCost.add(element);
            }
        }
        return onlyTestTypeCost;
    }

    public static boolean containsTestType(String str){
        boolean matchIsFound = false;
        for(String ttString: testTypeArray){
            Matcher matcher = Pattern.compile(ttString).matcher(str);
            if(matcher.find()){
                matchIsFound = true;
                break;
            }
        }
        return matchIsFound;
    }

    public static Integer findCost(String str){
        Matcher costMatcher = costRegex.matcher(str);
        if(costMatcher.find()){
            return Integer.parseInt(costMatcher.group());
        } else {
            return null;
        }
    }

    public static Integer findGestation (String str){
        Matcher gestationMatcher = gestationRegex.matcher(str);
        if(gestationMatcher.find()) {
            String gestationString = gestationMatcher.group();
            String gestationNumber = gestationString.replaceAll("\\D", "");
            Integer gestationInWeeks = Integer.parseInt(gestationNumber);
            return gestationInWeeks;
        } else{
            return null;
        }

    }
    public String findFirstGender(){
        ArrayList<String> stringsFromCell = isolateGestationGender(inputRow.getGestationGender());
        return findGender(stringsFromCell.get(0));
    }
    public static String findGender(String cell){
        Matcher boyMatcher = boy.matcher(cell);
        Matcher girlMatcher = girl.matcher(cell);
        Matcher boyGirlMatcher = boyGirl.matcher(cell);
        Matcher girlBoyMatcher = girlBoy.matcher(cell);
        //Heterowins are matched first to avoid false match by boy or girl matchers
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
        Matcher twinMatcher = twin.matcher(inputRow.getGestationGender());
        return twinMatcher.find();
    }

    public Interpreter (SeparatedRow newRow){
        inputRow = newRow;
    }
}
