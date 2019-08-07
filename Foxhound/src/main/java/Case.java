import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Case {

    private String id;
    private int idNumber;
    private String motherFirstName;
    private String motherLastName;
    private Date dateStarted;
    private Source sourceType;
    private Gender gender;
    private boolean twins = false;
    //List of all Case IDs
    private static HashSet<Case> allCases = new HashSet<>(500);

    //Stores hashed representation of Case's last row
    private ArrayList<Integer> caseState;

    //Record of each time Case is updated, use in Update's constructor!
    private ArrayList<Update> caseLog;

    //Becomes Update's ID, increment with every new Update
    private Integer updateNumber = 1;

    enum Gender {
        MALE, FEMALE, HETEROTWINS
    }
    enum Source {
        RAVGEN, REFERRAL, SELFSELL
    }
    //overriding Object's equals method only overrides it
    //for Case class right?
    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (o == null) {
            return false;
        }
        //Typecast Object down to a Case
        Case c = (Case) o;
        return c.idNumber == this.idNumber;
    }
    @Override
    public int hashCode() {
        return this.idNumber;
    }
    //TODO: create public int caseCellHash method

    public static String[] fullNameSplit(String fullName){
        return fullName.split(" ", 2);
    }
    public static Date stringToDate (String stringDate) throws ParseException {
        SimpleDateFormat datePattern = new SimpleDateFormat("MM/dd/yyyy");
        return datePattern.parse(stringDate);
    }
    public static boolean isHavingTwins(String gestationCell){
        Pattern twin = Pattern.compile("[Tt]win");
        Matcher twinMatcher = twin.matcher(gestationCell);
        return twinMatcher.find();
    }
    public static Gender findGender(String genderCell){
        Pattern boy = Pattern.compile("[Bb]oy");
        Matcher boyMatcher = boy.matcher(genderCell);
        Pattern girl = Pattern.compile("[Gg]irl");
        Matcher girlMatcher = girl.matcher(genderCell);
        Pattern boyGirl = Pattern.compile("[Bb]oy/[Gg]irl");
        Matcher boyGirlMatcher = boyGirl.matcher(genderCell);
        Pattern girlBoy = Pattern.compile("[Gg]irl/[Bb]oy");
        Matcher girlBoyMatcher = girlBoy.matcher(genderCell);

        if (boyGirlMatcher.find()) {
            return Gender.HETEROTWINS;
        }
        else if (girlBoyMatcher.find()){
            return Gender.HETEROTWINS;
        }
        else if (boyMatcher.find()){
            return Gender.MALE;
        }
        else if (girlMatcher.find()){
            return Gender.FEMALE;
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
    public static boolean caseExists(String stringId){
        Integer id = idToNumber(stringId);
        return allCases.contains(id);
    }

    //TODO: unit test each constructor component
    public Case (String caseID, String[] currentRow) throws ParseException {
        id = caseID;
        idNumber = idToNumber(caseID);
        String fullName = currentRow[1];
        String[] nameArray = fullNameSplit(fullName);
        if (nameArray.length == 2){
            motherFirstName = nameArray[0];
            motherLastName = nameArray[1];
        }
        else if (nameArray.length == 1) {
            motherFirstName = nameArray[0];
        }
        dateStarted = stringToDate(currentRow[0]);
        twins = isHavingTwins(currentRow[4]);
        gender = findGender(currentRow[4]);

        //TODO: create pattern matchers for sourceType field
    }



    //Setters and Getters
    public void setId(String id) {
        this.id = id;
    }

    // TODO: create setName method: separates full name into first and last


    public String getMotherFullName() {
        return this.motherFirstName + " " + this.motherLastName;
    }
    //TODO: create boolean compareStates Method


    public void setCaseState(String[] currentRow){
        this.caseState.clear();
        for (String str : currentRow){
            this.caseState.add(str.hashCode());
        }

    }

}
