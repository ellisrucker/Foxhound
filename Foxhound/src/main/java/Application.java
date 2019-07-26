import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Application {

    Pattern normalIdPattern = Pattern.compile("[a-zA-Z]{4}[\\d]{7}");

    //TODO: what ID finder methods do you need? Limit code repetition!
    public String findFirstId() {
        //TODO: finish method
    }

    public static void main(String [] args) throws IOException {

        File targetFolder = new File("C:\\Users\\brett.vallecillo\\Desktop\\Ellis's Secret Stuff\\CSVs 2016-2019");

        File [] csvList = targetFolder.listFiles();
        Arrays.sort(csvList);

        for (File csv: csvList) {
            //File reader method, ignores header row of CSV
            CSVReader reader = new CSVReaderBuilder(new FileReader(csv)).withSkipLines(1).build();

            String[] currentRow = null;
            while ((currentRow = reader.readNext()) != null) {
                System.out.println(csv.getName());
            }
        }

    }

}
