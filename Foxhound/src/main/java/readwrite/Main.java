package readwrite;

import DataTransferObject.ExcelRow;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import logic.Comparison;
import logic.Interpreter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.Arrays;

public class Main {


    public static void main(String [] args) throws IOException, ParseException, SQLException {

        File targetFolder = new File("C:\\Users\\Work\\IdeaProjects\\Foxhound\\Foxhound\\target\\mockData\\UpdateTest");
        File finalCSV = new File("C:\\Users\\Work\\IdeaProjects\\Foxhound\\Foxhound\\target\\mockData\\UpdateTest\\Update_Mock_2.csv");

        File [] csvList = targetFolder.listFiles();
        Arrays.sort(csvList);

        //TODO: Set properties from Properties file

        //Initialize Tables
        System.out.println("Initializing database tables");
        DbManager.initializeTables();

        //Pre-filter Complex Cases
        CSVReader filterReader = new CSVReaderBuilder(new FileReader(finalCSV)).withSkipLines(1).build();
        String[] currentFilterRow;
        while ((currentFilterRow = filterReader.readNext()) != null){
            ExcelRow newRow = new ExcelRow(currentFilterRow);
            Interpreter interpreter = new Interpreter(newRow);
            if(interpreter.caseIsComplex()){
                Connection dbConnection = DbManager.openConnection();
                try {
                    newRow.setCaseID(interpreter.findFirstMaternalSampleID());
                    newRow.insert(dbConnection);
                    dbConnection.commit();
                } catch(Exception e) {
                    e.printStackTrace();
                    dbConnection.rollback();
                } finally {
                    dbConnection.close();
                }
            }
        }

        //Main Loop
        for (File csv: csvList) {
            //File reader method, ignores header row of CSV
            System.out.println("Reading " + csv.getName());
            CSVReader reader = new CSVReaderBuilder(new FileReader(csv)).withSkipLines(1).build();

            String[] currentRow = null;

            while ((currentRow = reader.readNext()) != null) {
                ExcelRow newRow = new ExcelRow(currentRow);
                Comparison rowComparison = new Comparison(newRow,csv.getName());
                rowComparison.evaluateCase();
            }
        }
    }


}
