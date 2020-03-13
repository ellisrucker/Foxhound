package logic;

import dto.ExcelRow;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import utility.Configuration;
import utility.DbManager;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import java.util.Arrays;

import static utility.MySQL.selectFilteredCaseByID;

public class Main {


    public static void main(String [] args) throws IOException, SQLException {

        //Configuration
        System.out.println("Configuring properties");
        File targetFolder = new File(Configuration.getInstance().getTargetFolder());
        File terminalCSV = new File(Configuration.getInstance().getTerminalCSV());
        DbManager.configure();

        File [] csvList = targetFolder.listFiles();
        Arrays.sort(csvList);


        //Initialize Tables
        System.out.println("Initializing database tables");
        DbManager.initializeTables();

        //TODO: abstract filter functions into new Class
        //Pre-filter Complex Cases
        System.out.println("Pre-filtering fringe cases");
        CSVReader filterReader = new CSVReaderBuilder(new FileReader(terminalCSV)).withSkipLines(1).build();
        String[] currentFilterRow;
        while ((currentFilterRow = filterReader.readNext()) != null){
            ExcelRow newRow = new ExcelRow(currentFilterRow);
            Interpreter interpreter = new Interpreter(newRow);
            if(interpreter.caseIsComplex()){
                Connection dbConnection = DbManager.openConnection();
                try {
                    String caseID = interpreter.findFirstMaternalSampleID();

                    //Check to see if duplicate row has already been entered into db
                    PreparedStatement stmt = dbConnection.prepareStatement(selectFilteredCaseByID);
                    stmt.setString(1,caseID);
                    ResultSet rs = stmt.executeQuery();
                    if(!rs.next()) {
                        newRow.setCaseID(interpreter.findFirstMaternalSampleID());
                        newRow.insert(dbConnection);
                        dbConnection.commit();
                    }
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
