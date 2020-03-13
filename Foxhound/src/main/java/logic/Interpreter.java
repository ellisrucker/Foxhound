package logic;

import dto.*;
import dto.Test.TestType;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

public class Interpreter {


    private ExcelRow inputRow;

    //Constructors
    public Interpreter(ExcelRow newRow) {
        inputRow = newRow;
    }



    public static String findPersonnel(String str) {
        Map<Pattern, String> personnelMap = new HashMap<>();
        //Personnel Strings
        String john = "(?i)JV";
        String brett = "(?i)BV";
        String xin = "(?i)XG";
        String alex = "(?i)AH";
        String ellis = "(?i)ER|(?i)MR";
        String brina = "(?i)BS";
        String shane = "(?i)SB";

        personnelMap.put(Pattern.compile(john), "JV");
        personnelMap.put(Pattern.compile(brett), "BV");
        personnelMap.put(Pattern.compile(xin), "XG");
        personnelMap.put(Pattern.compile(alex), "AH");
        personnelMap.put(Pattern.compile(ellis), "ER");
        personnelMap.put(Pattern.compile(brina), "BS");
        personnelMap.put(Pattern.compile(shane), "SB");

        String initials = null;
        for (Map.Entry<Pattern, String> entry : personnelMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(str);
            if (matcher.find()) {
                initials = entry.getValue();
                break;
            }
        }
        return initials;
    }
    public static String findID(String str) {
        Matcher idMatcher = generalID.matcher(str);
        //May need to switch to while loop if it fails to find id
        if (idMatcher.find()) {
            return idMatcher.group();
        } else {
            return null;
        }
    }
    public static Patient.Relation findRelation(String idString) {
        Matcher relationMatcher = paternalRelation.matcher(idString);
        String relation;
        if (!relationMatcher.find()) {
            return Patient.Relation.P1;
        } else {
            relation = relationMatcher.group().toUpperCase();
        }
        if (relation.contains("P1")) {
            return Patient.Relation.P1;
        } else if (relation.contains("P2")) {
            return Patient.Relation.P2;
        } else if (relation.contains("P3")) {
            return Patient.Relation.P3;
        } else if (relation.contains("P4")) {
            return Patient.Relation.P4;
        } else if (relation.contains("P5")) {
            return Patient.Relation.P5;
        } else return Patient.Relation.UNKNOWN;

    }
    public static Integer idToNumber(String id) {
        id = id.replaceAll("\\D", "");
        return Integer.parseInt(id);
    }
    public static LocalDate findEventDate(String str, LocalDate testStartDate) {
        try {
            String mmddString = findmmddString(str);
            String[] monthAndDay = mmddString.split("/");
            int eventYear = testStartDate.getYear();
            int eventMonth = Integer.parseInt(monthAndDay[0]);
            int eventDay = Integer.parseInt(monthAndDay[1]);
            if (eventMonth < testStartDate.getMonthValue()) {
                eventYear++;
            }
            return LocalDate.of(eventYear, eventMonth, eventDay);
        } catch (Exception e) {
            System.out.println(str);
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<List<Sample>> consolidateSamples() {
        ArrayList<List<Sample>> samplesByPatient = new ArrayList<>();
        samplesByPatient.add(retrieveMaternalSamples());
        groupSamplesByPatient().forEach(samplesByPatient::add);
        return samplesByPatient;
    }
    public ArrayList<Genotype> consolidateAllGenotypes() {
        ArrayList<Genotype> allGenotypes = new ArrayList<>();
        allGenotypes.addAll(findGenotypeA());
        allGenotypes.addAll(findGenotypeB());
        return allGenotypes;
    }
    public ArrayList<Plasma> consolidateAllPlasmas() {
        ArrayList<Plasma> allPlasmas = new ArrayList<>();
        allPlasmas.addAll(findFirstDrawPlasmas());
        allPlasmas.addAll(findSecondDrawPlasmas());
        allPlasmas.addAll(findThirdDrawPlasmas());
        return allPlasmas;
    }
    public ArrayList<Genotype> findGenotypeA() {
        ArrayList<Genotype> genotypeList = isolateGenotypes(inputRow.getGenotypeA())
                .stream()
                .map(e -> new Genotype(e, Genotype.PrimerSet.A))
                .collect(toCollection(ArrayList::new));
        return genotypeList;
    }
    public ArrayList<Genotype> findGenotypeB() {
        ArrayList<Genotype> genotypeList = isolateGenotypes(inputRow.getGenotypeB())
                .stream()
                .map(e -> new Genotype(e, Genotype.PrimerSet.B))
                .collect(toCollection((ArrayList::new)));
        for (Genotype g : genotypeList) {
            Matcher matcher = Pattern.compile("96").matcher(g.getOriginalString());
            if (matcher.find()) {
                g.setPrimerSet(Genotype.PrimerSet.B96);
            }
        }
        return genotypeList;
    }
    /*
    Finding sampleID and gestation for First Draws may not work properly
    for cases with multiple maternal sampleIDs or multiple gestations
     */
    public ArrayList<Plasma> findFirstDrawPlasmas() {
        String sampleID = findFirstMaternalSampleID();
        Integer gestation = findFirstGestation();
        ArrayList<Plasma> plasmaList = isolatePlasmas(inputRow.getFirstDraw())
                .stream()
                .map(e -> new Plasma(e, Plasma.PlasmaNumber.FIRST))
                .collect(toCollection(ArrayList::new));
        for (Plasma p : plasmaList) {
            p.setPlasmaUsed(sampleID);
            p.setPlasmaGestation(gestation);
        }
        return plasmaList;
    }
    public ArrayList<Plasma> findSecondDrawPlasmas() {
        String cell = inputRow.getSecondDraw();
        String sampleID = Interpreter.findID(cell);
        Integer gestation = Interpreter.findGestation(cell);
        ArrayList<Plasma> plasmaList = isolatePlasmas(cell)
                .stream()
                .map(e -> new Plasma(e, Plasma.PlasmaNumber.SECOND))
                .collect(toCollection(ArrayList::new));
        for (Plasma p : plasmaList) {
            p.setPlasmaUsed(sampleID);
            p.setPlasmaGestation(gestation);
        }
        return plasmaList;
    }
    public ArrayList<Plasma> findThirdDrawPlasmas() {
        String cell = inputRow.getThirdDraw();
        String sampleID = Interpreter.findID(cell);
        Integer gestation = Interpreter.findGestation(cell);
        ArrayList<Plasma> plasmaList = isolatePlasmas(cell)
                .stream()
                .map(e -> new Plasma(e, Plasma.PlasmaNumber.THIRD))
                .collect(toCollection(ArrayList::new));
        for (Plasma p : plasmaList) {
            p.setPlasmaUsed(sampleID);
            p.setPlasmaGestation(gestation);
        }
        return plasmaList;
    }

    public String findFirstName() {
        String[] nameArray = splitFullName(inputRow.getMotherName());
        return nameArray[0];
    }
    public String findLastName() {
        String[] nameArray = splitFullName(inputRow.getMotherName());
        return nameArray[1];
    }
    public String findSource() {
        return inputRow.getReferral();
    }
    public LocalDate findRowDate() {
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("M[M]/d[d]/yyyy");
        return LocalDate.parse(inputRow.getDate(), datePattern);
    }
    public String findResultString() {
        return inputRow.getResult().trim();
    }
    public boolean isHavingTwins() {
        Matcher twinMatcher = twin.matcher(inputRow.getGestationGender());
        return twinMatcher.find();
    }
    public boolean caseIsComplex() {
        if (!(hasSingleMaternalID()) ||
                hasMultipleGestationGenders() ||
                hasMultipleTestTypeCosts() ||
                hasMultipleResults() ||
                hasConfirmation() ||
                hasUncommonGenotype()) {
            return true;
        } else return false;
    }


    /*
    Some spreadsheet cells often contain multiple entries. The following
    methods ensure that only the first instance in a cell is returned
     */
    public String findFirstMaternalSampleID() {
        //Use Matcher to find String? How would you sort? Comparator?
        ArrayList<Sample> maternalIDs = retrieveMaternalSamples();
        Sample firstMaternalSample = maternalIDs.get(0);
        return firstMaternalSample.getSampleID();
    }
    public Integer findFirstCost() {
        ArrayList<String> stringsFromCell = isolateTestTypeCost(inputRow.getTestTypeCost());
        return findCost(stringsFromCell.get(0));
    }
    public TestType findFirstTestType() {
        ArrayList<String> stringsFromCell = isolateTestTypeCost(inputRow.getTestTypeCost());
        return findTestType(stringsFromCell.get(0));
    }
    public String findFirstGender() {
        ArrayList<String> stringsFromCell = isolateGestationGender(inputRow.getGestationGender());
        if (stringsFromCell.size() != 0) {
            return findGender(stringsFromCell.get(0));
        } else return null;
    }
    public Integer findFirstGestation() {
        ArrayList<String> stringsFromCell = isolateGestationGender(inputRow.getGestationGender());
        if (stringsFromCell.size() != 0) {
            return findGestation(stringsFromCell.get(0));
        } else return 0;
    }

    //Private Methods

    /*
    Splits on zero-length matches that precede an id, but
    that do not follow a letter (to avoid splitting ids)
    */
    private static ArrayList<String> isolateSamples(String sampleCell) {
        ArrayList<String> onlySamples = new ArrayList<>();
        if (sampleCell != null) {
            String[] sampleArray = sampleCell.split("(?<![a-zA-z])(?=([a-zA-Z]{3,}[\\d]{5,}))");
            for (String element : sampleArray) {
                Matcher sampleMatcher = generalID.matcher(element);
                if (sampleMatcher.find()) {
                    onlySamples.add(element);
                }
            }
        }
        return onlySamples;
    }
    private static ArrayList<String> isolateGestationGender(String cell) {
        String[] gestationGenderArray = cell.split("(?<![\\d])(?=[\\d]{1,2}[Ww])");
        ArrayList<String> onlyGestationGender = new ArrayList<>();
        for (String element : gestationGenderArray) {
            Matcher gestationMatcher = gestationRegex.matcher(element);
            if (gestationMatcher.find()) {
                onlyGestationGender.add(element);
            }
        }
        return onlyGestationGender;
    }
    private static ArrayList<String> isolateTestTypeCost(String cell) {
        String[] testTypeCostArray = cell.split(testTypeDelimiters);
        ArrayList<String> onlyTestTypeCost = new ArrayList<>();
        for (String element : testTypeCostArray) {
            Matcher costMatcher = costRegex.matcher(element);
            if (costMatcher.find() | containsTestType(element)) {
                onlyTestTypeCost.add(element);
            }
        }
        return onlyTestTypeCost;
    }
    private static ArrayList<String> isolateResults(String cell) {
        String[] resultStrings = cell.split(resultDelimiters);
        ArrayList<String> onlyResults = new ArrayList<>();
        for (String element : resultStrings) {
            if (containsResult(element)) {
                onlyResults.add(element);
            }
        }
        return onlyResults;
    }
    private static ArrayList<String> isolateGenotypes(String cell) {
        String genotypeDelimiter = "(?<![\\d])(?=[\\d]{1,2}/[\\d]{1,2})";
        String[] genotypeArray = cell.split(genotypeDelimiter);
        ArrayList<String> onlyGenotypes = new ArrayList<>();
        for (String element : genotypeArray) {
            Matcher dateMatcher = mmddRegex.matcher(element);
            if (dateMatcher.find()) {
                onlyGenotypes.add(element);
            }
        }
        return onlyGenotypes;
    }
    private static ArrayList<String> isolatePlasmas(String cell) {
        String plasmaDelimiter = "(?=([Aa][[\\s*-][/(]]))";
        String[] plasmaArray = cell.split(plasmaDelimiter);
        ArrayList<String> onlyPlasmas = new ArrayList<>();
        for (String element : plasmaArray) {
            Matcher dateMatcher = mmddRegex.matcher(element);
            if (dateMatcher.find()) {
                onlyPlasmas.add(element);
            }
        }
        return onlyPlasmas;
    }


    private static TestType findTestType(String str) {
        Map<Pattern, TestType> testTypeMap = new HashMap<>();
        String oneWeek = "1[\\s]*[Ww]";
        String twoWeek = "2[\\s]*[Ww]";
        String threeWeek = "3[\\s]*[Ww]";

        testTypeMap.put(Pattern.compile(threeWeek), TestType.THREE_WEEK);
        testTypeMap.put(Pattern.compile(oneWeek), TestType.ONE_WEEK);
        testTypeMap.put(Pattern.compile(early), TestType.EARLY);
        testTypeMap.put(Pattern.compile(surrogate), TestType.SURROGATE);
        testTypeMap.put(Pattern.compile(ivf), TestType.IVF);
        testTypeMap.put(Pattern.compile(maternity), TestType.MATERNITY);
        testTypeMap.put(Pattern.compile(gender), TestType.GENDER);
        testTypeMap.put(Pattern.compile(twoWeek), TestType.TWO_WEEK);
        testTypeMap.put(Pattern.compile(matPat), TestType.MAT_PAT);
        testTypeMap.put(Pattern.compile(patMat), TestType.MAT_PAT);

        TestType tt = null;
        for (Map.Entry<Pattern, TestType> entry : testTypeMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(str);
            if (matcher.find()) {
                tt = entry.getValue();
                break;
            }
        }
        //Avoids Null Pointer Exception when inserting into DB
        if (tt == null) {
            tt = TestType.UNKNOWN;
        }
        return tt;
    }
    private static String findmmddString(String str) {
        Matcher matcher = mmddRegex.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        } else return null;
    }
    private static boolean containsTestType(String str) {
        boolean matchIsFound = false;
        for (String ttString : testTypeArray) {
            Matcher matcher = Pattern.compile(ttString).matcher(str);
            if (matcher.find()) {
                matchIsFound = true;
                break;
            }
        }
        return matchIsFound;
    }
    private static Integer findCost(String str) {
        Matcher costMatcher = costRegex.matcher(str);
        if (costMatcher.find()) {
            return Integer.parseInt(costMatcher.group());
        } else {
            return null;
        }
    }
    private static Integer findGestation(String str) {
        Matcher gestationMatcher = gestationRegex.matcher(str);
        if (gestationMatcher.find()) {
            String gestationString = gestationMatcher.group();
            String gestationNumber = gestationString.replaceAll("\\D", "");
            return Integer.parseInt(gestationNumber);
        } else {
            return null;
        }

    }
    private static String findGender(String cell) {
        Matcher boyMatcher = boy.matcher(cell);
        Matcher girlMatcher = girl.matcher(cell);
        Matcher boyGirlMatcher = boyGirl.matcher(cell);
        Matcher girlBoyMatcher = girlBoy.matcher(cell);
        //Heterowins are matched first to avoid false match by boy or girl matchers
        if (boyGirlMatcher.find()) {
            return "Heterotwins";
        } else if (girlBoyMatcher.find()) {
            return "Heterotwins";
        } else if (boyMatcher.find()) {
            return "Male";
        } else if (girlMatcher.find()) {
            return "Female";
        } else {
            return null;
        }
    }
    private static boolean containsResult(String str) {
        boolean resultIsFound = false;
        for (String resultString : resultArray) {
            Matcher matcher = Pattern.compile(resultString).matcher(str);
            if (matcher.find()) {
                resultIsFound = true;
                break;
            }
        }
        return resultIsFound;
    }

    private String[] splitFullName(String fullName) {
        return fullName.split(" ", 2);
    }
    private ArrayList<Sample> retrieveMaternalSamples() {
        ArrayList<String> maternalSampleStrings = isolateSamples(inputRow.getMaternalPatientId());
        maternalSampleStrings.addAll(isolateSamples(inputRow.getSecondDraw()));
        maternalSampleStrings.addAll(isolateSamples(inputRow.getThirdDraw()));

        ArrayList<Sample> maternalSamples = maternalSampleStrings
                .stream()
                .map(s -> new Sample(s, Patient.Relation.M))
                .sorted()
                .collect(toCollection(ArrayList::new));
        //Set PatientID for all Samples
        String patientID = maternalSamples.get(0).getSampleID();
        for (Sample s : maternalSamples) {
            s.setPatientID(patientID);
        }
        return maternalSamples;
    }
    private ArrayList<List<Sample>> groupSamplesByPatient() {
        //Create samples and group them by relationship to mother
        Map<Patient.Relation, List<Sample>> samplesByRelation = isolateSamples(inputRow.getPaternalPatientId())
                .stream()
                .map(s -> new Sample(s))
                .collect(groupingBy(
                        Sample::getRelation
                ));
        //Create sorted list of Samples grouped by Patient
        ArrayList<List<Sample>> samplesByPatient = samplesByRelation.values()
                .stream()
                .map(list -> list.stream()
                        .sorted()
                        .collect(toList()))
                .collect(toCollection(ArrayList::new));
        //Set PatientID for all Samples
        for (List<Sample> sampleList : samplesByPatient) {
            String patientID = sampleList.get(0).getSampleID();
            sampleList.stream()
                    .forEachOrdered(e -> e.setPatientID(patientID));
        }
        return samplesByPatient;
    }
    //Empty cell causes nullpointerexception, indicates extreme case
    private boolean hasSingleMaternalID() {
        try {
            ArrayList<String> maternalIDs = isolateSamples(inputRow.getMaternalPatientId());
            return maternalIDs.size() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    private boolean hasMultipleGestationGenders() {
        if (inputRow.getGestationGender() == null) {
            return false;
        }
        ArrayList<String> stringList = isolateGestationGender(inputRow.getGestationGender());
        return stringList.size() > 1;
    }
    private boolean hasMultipleTestTypeCosts() {
        if (inputRow.getTestTypeCost() == null) {
            return false;
        }
        ArrayList<String> stringList = isolateTestTypeCost(inputRow.getTestTypeCost());
        return stringList.size() > 1;
    }
    private boolean hasMultipleResults() {
        if (inputRow.getResult() == null) {
            return false;
        }
        ArrayList<String> stringList = isolateResults(inputRow.getResult());
        return stringList.size() > 1;
    }
    private boolean hasConfirmation() {
        String cell = inputRow.getConfirmation();
        if (cell == null) {
            return false;
        }
        Matcher matcher = Pattern.compile("[\\w]").matcher(cell);
        return matcher.find();
    }
    private boolean hasUncommonGenotype() {
        String genotypeA = inputRow.getGenotypeA();
        String genotypeB = inputRow.getGenotypeB();
        if (genotypeA != null) {
            Matcher matcherX = Pattern.compile("[Xx][^Gg]").matcher(genotypeA);
            if (matcherX.find()) {
                return true;
            }
        }
        if (genotypeB != null) {
            Matcher matcherY = Pattern.compile("(?<![Mm])[Yy]").matcher(genotypeB);
            return matcherY.find();
        }
        return false;
    }


    private static Pattern generalID = Pattern.compile("[a-zA-Z]{3,}[\\d]{5,}");
    private static Pattern gestationRegex = Pattern.compile("[\\d]{1,2}[Ww]");
    private static Pattern costRegex = Pattern.compile("[\\d]{3,4}");
    private static Pattern mmddRegex = Pattern.compile("[\\d]{1,2}/[\\d]{1,2}");

    private static Pattern boy = Pattern.compile("[Bb]oy");
    private static Pattern girl = Pattern.compile("[Gg]irl");
    private static Pattern boyGirl = Pattern.compile("[Bb]oy/[Gg]irl");
    private static Pattern girlBoy = Pattern.compile("[Gg]irl/[Bb]oy");
    private static Pattern twin = Pattern.compile("[Tt]win");

    private static Pattern paternalRelation = Pattern.compile("[^a-zA-Z][Pp][\\d]");


    //Result Strings and Delimiters
    private static String match = "(?i)match";
    private static String mismatch = "(?i)mismatch";
    private static String cancelled = "(?i)cancel";
    private static String miscarried = "(?i)miscar";
    private static String resultDelimiters = "(?=" +
            "[^a-zA-Z]" + match + "|" +
            mismatch + "|" +
            cancelled + "|" +
            miscarried + ")";

    private static String[] resultArray = new String[]{match, mismatch, cancelled, miscarried};

    //TestType Strings and Delimiters
    private static String early = "(?i)early";
    private static String turnaround = "[123][Ww]";
    private static String gender = "(?i)gender";
    private static String surrogate = "(?i)surrogate";
    private static String ivf = "(?i)IVF";
    private static String maternity = "(?i)maternity";
    private static String matPat = "(?i)mat.*?pat";
    private static String patMat = "(?i)pat.*?mat";
    private static String testTypeDelimiters = "(?=" +
            early + "|" +
            turnaround + "|" +
            gender + "|" +
            surrogate + "|" +
            ivf + "|" +
            maternity + "|" +
            matPat + "|" +
            patMat + ")";

    private static String[] testTypeArray = new String[]{early, turnaround, gender, surrogate, ivf, maternity,
            matPat, patMat};

}
