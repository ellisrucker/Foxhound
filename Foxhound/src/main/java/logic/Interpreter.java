package logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

    private String str;

    private static Pattern generalID = Pattern.compile("[a-zA-Z]{3,}[\\d]{5,}");
    private static Pattern relation = Pattern.compile("[PpCc][\\d]");
    private static Pattern gestation = Pattern.compile("[\\d][Ww]");

    private static String idRgx = "[a-zA-Z]{3,}[\\d]{5,}";
    private static String relationRgx = "[PpCc][\\d]";
    private static String gestationRgx = "[\\d][Ww]";


    private static Pattern boy = Pattern.compile("[Bb]oy");
    private static Pattern girl = Pattern.compile("[Gg]irl");
    private static Pattern boyGirl = Pattern.compile("[Bb]oy/[Gg]irl");
    private static Pattern girlBoy = Pattern.compile("[Gg]irl/[Bb]oy");
    private static Pattern twin = Pattern.compile("[Tt]win");


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

    public String findID(){
        String id = null;
        Matcher idMatcher = generalID.matcher(str);
        //May need to switch to while loop if it fails to find id
        if (idMatcher.find()) {
            return idMatcher.group();
        } else {
            return null;
        }
    }



    public static String findFirstId(String[] currentRow) {
        if (currentRow[2].length() == 11) {
            return currentRow[2];
        }
        else {
            Matcher matcher = generalID.matcher(currentRow[2]);
            if (matcher.find()) {
                return matcher.group();
            }
            else {
                return null;
            }
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
