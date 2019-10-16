package readwrite;

import DataTransferObject.Case;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.Arrays;

public class Iterator {



    public static void main(String [] args) throws IOException, ParseException, SQLException {

        File targetFolder = new File("C:\\Users\\Work\\IdeaProjects\\Foxhound\\Foxhound\\target\\mockData");
        File [] csvList = targetFolder.listFiles();
        Arrays.sort(csvList);

        ConnectionManager.initializeCaseTable();

        for (File csv: csvList) {
            //File reader method, ignores header row of CSV
            CSVReader reader = new CSVReaderBuilder(new FileReader(csv)).withSkipLines(1).build();

            String[] currentRow = null;
            while ((currentRow = reader.readNext()) != null) {
                SeparatedRow newRow = new SeparatedRow(currentRow);
                Case newCase = new Case(newRow);
                newCase.insertNewCase();


            }
        }

    }

}
