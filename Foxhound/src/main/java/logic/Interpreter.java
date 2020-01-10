package logic;

import DataTransferObject.Event;
import DataTransferObject.Test;
import DataTransferObject.Test.TestType;
import IntermediateObject.EventString;
import IntermediateObject.SampleString;
import DataTransferObject.ExcelRow;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Interpreter {


    private ExcelRow inputRow;

    private static Pattern generalID = Pattern.compile("[a-zA-Z]{3,}[\\d]{5,}");
    private static Pattern relationRegex = Pattern.compile("[^a-zA-Z][PpCc][\\d]");
    private static Pattern gestationRegex = Pattern.compile("[\\d]{1,2}[Ww]");
    private static Pattern costRegex = Pattern.compile("[\\d]{3,4}");
    private static Pattern mmddRegex = Pattern.compile("[\\d]{1,2}/[\\d]{1,2}");


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



    //Result Strings and Delimiters
    private static String match = "(?i)match";
    private static String mismatch = "(?i)mismatch";
    private static String cancelled = "(?i)cancel";
    private static String miscarried = "(?i)miscar";
    private static String resultDelimiters = "(?="+
            "[^a-zA-Z]" + match + "|" +
            mismatch + "|" +
            cancelled + "|" +
            miscarried + ")";

    private static String[] resultArray = new String[]{match, mismatch, cancelled, miscarried};

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

    //Personnel Strings
    //Be sure to update findPersonnel if more are added
    private static String john = "(?i)JV";
    private static String brett = "(?i)BV";
    private static String xin = "(?i)XG";
    private static String alex = "(?i)AH";
    private static String ellis = "(?i)ER|(?i)MR";
    private static String brina = "(?i)BS";
    private static String shane = "(?i)SB";


    //Event Delimiters
    private static String genotypeDelimiter = "(?<![\\d])(?=[\\d]{1,2}/[\\d]{1,2})";
    private static String plasmaDelimiter = "(?=([Aa][[\\s*-][/(]]))";




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

    public String findSource(){
        return inputRow.getReferral();
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

    public ArrayList<EventString> consolidateAllEvents(){
        ArrayList<EventString> allEvents = new ArrayList<>();
        findGenotypeA().stream().forEachOrdered(allEvents::add);
        findGenotypeB().stream().forEachOrdered(allEvents::add);
        findFirstDrawPlasmas().stream().forEachOrdered(allEvents::add);
        findSecondDrawPlasmas().stream().forEachOrdered(allEvents::add);
        findThirdDrawPlasmas().stream().forEachOrdered(allEvents::add);
        return allEvents;
    }
    public static ArrayList<String> isolateGenotypes (String cell){
        String[] genotypeArray = cell.split(genotypeDelimiter);
        ArrayList<String> onlyGenotypes = new ArrayList<>();
        for(String element: genotypeArray){
            Matcher dateMatcher = mmddRegex.matcher(element);
            if(dateMatcher.find()){
                onlyGenotypes.add(element);
            }
        }
        return onlyGenotypes;
    }
    public ArrayList<EventString> findGenotypeA() {
        return isolateGenotypes(inputRow.getGenotypeA())
                .stream()
                .map(e -> new EventString(e, Event.LabTest.GENOTYPE, Event.PrimerSet.A))
                .collect(toCollection(ArrayList::new));
    }
    public ArrayList<EventString> findGenotypeB() {
        ArrayList<EventString> eventList = isolateGenotypes(inputRow.getGenotypeB())
                .stream()
                .map(e -> new EventString(e, Event.LabTest.GENOTYPE, Event.PrimerSet.B))
                .collect(toCollection((ArrayList::new)));
        for(EventString e: eventList){
            Matcher matcher = Pattern.compile("96").matcher(e.getEvent());
            if(matcher.find()){
                e.setPrimerSet(Event.PrimerSet.B96);
            }
        }
        return eventList;
    }

    public static ArrayList<String> isolatePlasmas (String cell){
        String[] plasmaArray = cell.split(plasmaDelimiter);
        ArrayList<String> onlyPlasmas = new ArrayList<>();
        for(String element: plasmaArray){
            Matcher dateMatcher = mmddRegex.matcher(element);
            if(dateMatcher.find()){
                onlyPlasmas.add(element);
            }
        }
        return onlyPlasmas;
    }
    //TODO: Abstract Method from plasmaFinders to reduce duplication
    public ArrayList<EventString> findFirstDrawPlasmas(){
       return isolatePlasmas(inputRow.getFirstDraw())
               .stream()
               .map(e -> new EventString(e, Event.LabTest.PLASMA, Event.PlasmaNumber.FIRST))
               .collect(toCollection(ArrayList::new));
    }
    public ArrayList<EventString> findSecondDrawPlasmas(){
        return isolatePlasmas(inputRow.getSecondDraw())
                .stream()
                .map(e -> new EventString(e, Event.LabTest.PLASMA, Event.PlasmaNumber.SECOND))
                .collect(toCollection(ArrayList::new));
    }
    public ArrayList<EventString> findThirdDrawPlasmas(){
        return isolatePlasmas(inputRow.getThirdDraw())
                .stream()
                .map(e -> new EventString(e, Event.LabTest.PLASMA, Event.PlasmaNumber.THIRD))
                .collect(toCollection(ArrayList::new));
    }
    public static LocalDate findEventDate(String str, LocalDate testStartDate) {
        try {
            String mmddString = findmmddString(str);
            String[] monthAndDay = mmddString.split("/");
            Integer eventYear = testStartDate.getYear();
            Integer eventMonth = Integer.parseInt(monthAndDay[0]);
            Integer eventDay = Integer.parseInt(monthAndDay[1]);
            if (eventMonth < testStartDate.getMonthValue()) {
                eventYear++;
            }
            return LocalDate.of(eventYear, eventMonth, eventDay);
        } catch(Exception e){
            System.out.println(str);
            e.printStackTrace();
            return null;
        }

    }

    public static String findmmddString(String str){
        Matcher matcher = mmddRegex.matcher(str);
        if(matcher.find()){
            return matcher.group();
        } else return null;
    }

    public static String findPersonnel(String str){
        Map<Pattern,String> personnelMap = new HashMap<>();
        personnelMap.put(Pattern.compile(john),"JV");
        personnelMap.put(Pattern.compile(brett),"BV");
        personnelMap.put(Pattern.compile(xin),"XG");
        personnelMap.put(Pattern.compile(alex),"AH");
        personnelMap.put(Pattern.compile(ellis),"ER");
        personnelMap.put(Pattern.compile(brina),"BS");
        personnelMap.put(Pattern.compile(shane), "SB");

        String initials = null;
        for(Map.Entry<Pattern,String> entry: personnelMap.entrySet()){
            Matcher matcher = entry.getKey().matcher(str);
            if(matcher.find()){
                initials = entry.getValue();
                break;
            }
        }
        return initials;
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

    //Quantitative Methods

    //Empty cell causes nullpointerexception, indicates extreme case
    public boolean hasSingleMaternalID(){
        try {
            ArrayList<SampleString> maternalSamples = retrieveMaternalIDs();
            if (maternalSamples.size() == 1){
                return true;
            }
            else {
                System.out.println(inputRow.getMotherName() + ": Irregular ID Quantity");
                return false;
            }
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public boolean hasMultipleGestationGenders(){
        if(inputRow.getGestationGender() == null){
            return false;
        }
        ArrayList<String> stringList = isolateGestationGender(inputRow.getGestationGender());
        if(stringList.size() > 1){
            System.out.println(inputRow.getMotherName() + ": MultipleGestationGenders");
            return true;
        }
        else {
            return false;
        }
    }
    public boolean hasMultipleTestTypeCosts(){
        if(inputRow.getTestTypeCost() == null){
           return false;
        }
        ArrayList<String> stringList = isolateTestTypeCost(inputRow.getTestTypeCost());
        if(stringList.size() > 1){
            System.out.println(inputRow.getMotherName() + ": MultipleTestTypeCosts");
            return true;
        }
        else {
            return false;
        }
    }
    public boolean hasMultipleResults(){
        if(inputRow.getResult() == null){
            return false;
        }
        ArrayList<String> stringList = isolateResults(inputRow.getResult());
        if(stringList.size() > 1){
            System.out.println(inputRow.getMotherName() + ": Multiple Results");
            return true;
        }
        else {
            return false;
        }
    }
    public boolean hasConfirmation(){
        String cell = inputRow.getConfirmation();
        if(cell == null){
            return false;
        }
        Matcher matcher = Pattern.compile("[\\w]").matcher(cell);
        if (matcher.find()) {
            System.out.println(inputRow.getMotherName() + ": Confirmation Detected");
            return true;
        } else {
            return false;
        }
    }
    public boolean hasUncommonGenotype(){
        Matcher matcherX = Pattern.compile("[Xx][^Gg]").matcher(inputRow.getGenotypeA());
        Matcher matcherY = Pattern.compile("(?<![Mm])[Yy]").matcher(inputRow.getGenotypeA());
        if(matcherX.find()){
            System.out.println("X Genotype found");
            return true;
        } else if(matcherY.find()){
            System.out.println("Y Genotype found");
            return true;
        } else return false;
    }

    //Complexity

    //Used to filter out extreme cases, edit as functionality is added
    //Remember to adjust unit tests after editing
    public boolean caseIsComplex(){
        if(!(hasSingleMaternalID())||
        hasMultipleGestationGenders()||
        hasMultipleTestTypeCosts()||
        hasMultipleResults()||
        hasConfirmation()||
        hasUncommonGenotype()){
           return true;
        }
        else return false;
    }

    //Result Processing

    public static ArrayList<String> isolateResults(String cell){
        String[] resultStrings = cell.split(resultDelimiters);
        ArrayList<String> onlyResults = new ArrayList<>();
        for (String element: resultStrings){
            if (containsResult(element)){
                onlyResults.add(element);
            }
        }
        return onlyResults;
    }
    public static boolean containsResult(String str){
        boolean resultIsFound = false;
        for (String resultString: resultArray){
            Matcher matcher = Pattern.compile(resultString).matcher(str);
            if(matcher.find()){
                resultIsFound = true;
                break;
            }
        }
        return resultIsFound;
    }

    public Test.Result findFirstResult(){
        ArrayList<String> stringsFromCell = isolateResults(inputRow.getResult());
        return findResult(stringsFromCell.get(0));
    }

    public static Test.Result findResult (String str){
        Matcher matchMatcher = Pattern.compile(match).matcher(str);
        Matcher mismatchMatcher = Pattern.compile(mismatch).matcher(str);
        Matcher cancelledMatcher = Pattern.compile(cancelled).matcher(str);
        Matcher miscarriedMatcher = Pattern.compile(miscarried).matcher(str);
        if (matchMatcher.find()){
            return Test.Result.MATCH;
        }
        else if (mismatchMatcher.find()){
            return Test.Result.MISMATCH;
        }
        else if (cancelledMatcher.find()){
            return Test.Result.CANCELLED;
        }
        else if (miscarriedMatcher.find()) {
            return Test.Result.MISCARRIAGE;
        }
        else {
            return Test.Result.UNKNOWN;
        }

    }

    public Interpreter (ExcelRow newRow){
        inputRow = newRow;
    }
}
