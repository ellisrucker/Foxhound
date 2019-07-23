import com.opencsv.CSVReader;
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


    public static void main(String [] args) throws IOException {

        File targetFolder = new File("C:\\Users\\brett.vallecillo\\Desktop\\Ellis's Secret Stuff\\CSVs 2016-2019");

        File [] csvList = targetFolder.listFiles();
        Arrays.sort(csvList);

        for (File csv: csvList) {
            //TODO: create File reader method that ignores header row of CSV
            System.out.println(csv.getName());

        }

    }

}
