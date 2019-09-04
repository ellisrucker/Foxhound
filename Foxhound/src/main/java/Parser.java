import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static Pattern generalId = Pattern.compile("[a-zA-Z]{3,}[\\d]{5,}");


    public static String findFirstId(String[] currentRow) {
        if (currentRow[2].length() == 11) {
            return currentRow[2];
        }
        else {
            Matcher matcher = generalId.matcher(currentRow[2]);
            if (matcher.find()) {
                return matcher.group();
            }
            else {
                return null;
            }
        }
    }

}
