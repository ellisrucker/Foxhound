package readwrite;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import logic.Comparison;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.Arrays;

public class Iterator {



    public static void main(String [] args) throws IOException, ParseException, SQLException {

        File targetFolder = new File("C:\\Users\\Work\\IdeaProjects\\Foxhound\\Foxhound\\target\\mockData\\MultipleSampleTest");
        File [] csvList = targetFolder.listFiles();
        Arrays.sort(csvList);

        DbManager.initializeCaseTable();
        DbManager.initializeSampleTable();
        DbManager.initializePatientTable();

        for (File csv: csvList) {
            //File reader method, ignores header row of CSV
            CSVReader reader = new CSVReaderBuilder(new FileReader(csv)).withSkipLines(1).build();

            String[] currentRow = null;

            //TODO: Turn into addNewCases() to clean up code?
            while ((currentRow = reader.readNext()) != null) {
                SeparatedRow newRow = new SeparatedRow(currentRow);
                Comparison rowComparison = new Comparison(newRow);
                if (rowComparison.caseExists() == false){
                    Update update = new Update(newRow);
                    update.generateNewCase();

                }



            }
        }

    }

}
