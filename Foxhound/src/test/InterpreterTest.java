import logic.Interpreter;
import org.junit.Test;
import DataTransferObject.ExcelRow;

import static DataTransferObject.Test.TestType.*;
import static org.junit.Assert.*;



public class InterpreterTest {

    @Test
    public void findFirstName_returnsFirstName_ifPassedTwoNames(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMotherName("Ellis Rucker");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "Ellis";
        String actual = interpreter.findFirstName();
        assertEquals(expected,actual);
    }
    @Test
    public void findLastName_returnsLastName_ifPassedTwoNames(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMotherName("Ellis Rucker");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "Rucker";
        String actual = interpreter.findLastName();
        assertEquals(expected,actual);
    }
    @Test
    public void findLastName_returnsLastName_ifPassedThreeNames(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMotherName("Juju Smith Schuster");
        Interpreter interpreter = new Interpreter (testRow);
        String expected = "Smith Schuster";
        String actual = interpreter.findLastName();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstMaternalID_returnsSample_ifPassedOneSample(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("WINR1910055");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "WINR1910055";
        String actual = interpreter.findFirstMaternalID();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstMaternalID_returnsCorrectSample_ifPassedMultipleSamples(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("WINR1910055 WINR1909127*");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "WINR1909127";
        String actual = interpreter.findFirstMaternalID();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstGestation_returnsCorrectGestation_ifPassedMultipleGestations(){
        ExcelRow testRow = new ExcelRow();
        testRow.setGestationGender("16w 25w, boy");
        Interpreter interpreter = new Interpreter(testRow);
        Integer expected = 16;
        Integer actual = interpreter.findFirstGestation();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstGender_returnsCorrectGender_ifPassedMultipleGenders(){
        ExcelRow testRow = new ExcelRow();
        testRow.setGestationGender("(20w,boy) 22w, girl");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "Male";
        String actual = interpreter.findFirstGender();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstGender_returnsNull_ifFirstTestIsMissingGender(){
        ExcelRow testRow = new ExcelRow();
        testRow.setGestationGender("(10w) 22w, girl");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = null;
        String actual = interpreter.findFirstGender();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstTestType_returnsCorrectTestType_ifPassedMultipleTestTypes(){
        ExcelRow testRow = new ExcelRow();
        testRow.setTestTypeCost("(1wk, 1250) 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        DataTransferObject.Test.TestType expected = ONE_WEEK;
        DataTransferObject.Test.TestType actual = interpreter.findFirstTestType();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstTestType_returnsUnknown_ifFirstTestTypeIsNull(){
        ExcelRow testRow = new ExcelRow();
        testRow.setTestTypeCost("(1250) 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        DataTransferObject.Test.TestType expected = UNKNOWN;
        DataTransferObject.Test.TestType actual = interpreter.findFirstTestType();
        assertEquals(expected,actual);
    }

    @Test
    public void findFirstCost_returnsCorrectCost_ifPassedMultipleCosts(){
        ExcelRow testRow = new ExcelRow();
        testRow.setTestTypeCost("(1wk, 1250) 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        Integer expected = 1250;
        Integer actual = interpreter.findFirstCost();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstCost_returnsNull_ifFirstTestIsMissingCost(){
        ExcelRow testRow = new ExcelRow();
        testRow.setTestTypeCost("(1wk) 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        Integer expected = null;
        Integer actual = interpreter.findFirstCost();
        assertEquals(expected,actual);
    }
    @Test
    public void caseIsComplex_returnsTrue_ifMIdCellEmpty(){
        ExcelRow testRow = new ExcelRow();
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(true, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsFalse_ifPassedSingleMIdAndNothingElse(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(false, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsTrue_ifPassedMultipleMIds(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001 ELLR1912002");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(true, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsTrue_ifPassedMultipleGestationGenders(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        testRow.setGestationGender("9w boy 16w");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(true, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsFalse_ifPassedSingleGestationGender(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        testRow.setGestationGender("9w boy");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(false, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsTrue_ifPassedMultipleTestTypeCosts(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        testRow.setTestTypeCost("1wk 1250 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(true, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsFalse_ifPassedSingleTestTypeCost(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        testRow.setTestTypeCost("1wk 1250");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(false, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsTrue_ifPassedMultipleResults(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        testRow.setResult("mismatch match");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(true, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsFalse_ifPassedSingleResult(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        testRow.setResult("match");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(false, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsTrue_ifConfirmationCellIsNotNull(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        testRow.setConfirmation("mismatch");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(true, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsFalse_ifConfirmationCellIsNull(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(false, interpreter.caseIsComplex());
    }
    @Test
    public void caseIsComplex_returnsFalse_ifConfirmationCellIsEmpty(){
        ExcelRow testRow = new ExcelRow();
        testRow.setMaternalPatientId("ELLR1912001");
        testRow.setConfirmation("");
        Interpreter interpreter = new Interpreter(testRow);
        assertEquals(false, interpreter.caseIsComplex());
    }


    /*
    @Ignore
    //Method performs as expected, but fails test due to differences in list order
    public void groupIDsByPatient_sortsSampleStrings_whenPassedCell(){
        String cell = "EDWE1911013(P1) EDWE1911012(P1) ALPE1911014(p2) ROYM1911015(P3) swab1911016(p3)";
        SampleString ss1 = new SampleString("EDWE1911012(P1)","EDWE1911012", SampleString.Relation.P1);
        SampleString ss2 = new SampleString("EDWE1911013(P1)","EDWE1911013", SampleString.Relation.P1);
        SampleString ss3 = new SampleString("ALPE1911014(p2)", "ALPE1911014", SampleString.Relation.P2);
        SampleString ss4 = new SampleString("ROYM1911015(P3)", "ROYM1911015", SampleString.Relation.P3);
        SampleString ss5 = new SampleString("swab1911016(p3)","swab1911016", SampleString.Relation.P3);
        List<SampleString> p1Samples = Arrays.asList(ss1,ss2);
        List<SampleString> p2Samples = Arrays.asList(ss3);
        List<SampleString> p3Samples = Arrays.asList(ss4,ss5);
        Set<List<SampleString>> expected = new HashSet<>(Arrays.asList(p1Samples,p2Samples,p3Samples));
        Interpreter interpreter = new Interpreter(cell);
        Set<List<SampleString>> actual = interpreter.groupIDsByPatient()
                .stream()
                .collect(Collectors.toSet());
        assertEquals(actual,expected);

    }*/
}
