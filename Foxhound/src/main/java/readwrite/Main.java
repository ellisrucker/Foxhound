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

import static readwrite.MySQL.insertFilteredCase;

public class Main {



    public static void main(String [] args) throws IOException, ParseException, SQLException {

        File targetFolder = new File("C:\\Users\\Work\\IdeaProjects\\Foxhound\\Foxhound\\target\\mockData\\UpdateTest");
        File finalCSV = new File("C:\\Users\\Work\\IdeaProjects\\Foxhound\\Foxhound\\target\\mockData\\UpdateTest\\Update_Mock_2.csv");

        File [] csvList = targetFolder.listFiles();
        Arrays.sort(csvList);

        //Initialize Tables
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
                    insertNewFilteredCase(newRow, dbConnection);
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
            CSVReader reader = new CSVReaderBuilder(new FileReader(csv)).withSkipLines(1).build();

            String[] currentRow = null;

            while ((currentRow = reader.readNext()) != null) {
                ExcelRow newRow = new ExcelRow(currentRow);
                Comparison rowComparison = new Comparison(newRow,csv.getName());
                rowComparison.evaluateCase();
            }
        }
    }

    //DML
    private static void insertNewFilteredCase(ExcelRow row, Connection dbConnection) throws SQLException {
            PreparedStatement stmt = dbConnection.prepareStatement(insertFilteredCase);
            stmt.setString(1, row.getCaseID());
            stmt.setString(2, row.getDate());
            stmt.setString(3, row.getMotherName());
            stmt.setString(4, row.getMaternalPatientId());
            stmt.setString(5, row.getPaternalPatientId());
            stmt.setString(6, row.getGestationGender());
            stmt.setString(7, row.getTestTypeCost());
            stmt.setString(8, row.getReferral());
            stmt.setString(9, row.getGenotypeA());
            stmt.setString(10, row.getGenotypeB());
            stmt.setString(11, row.getFirstDraw());
            stmt.setString(12, row.getSecondDraw());
            stmt.setString(13, row.getThirdDraw());
            stmt.setString(14, row.getResult());
            stmt.setString(15, row.getConfirmation());
            stmt.executeUpdate();
    }

}
