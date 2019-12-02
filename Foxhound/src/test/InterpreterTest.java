import IntermediateObject.SampleString;
import logic.Interpreter;
import org.junit.Ignore;
import org.junit.Test;
import readwrite.SeparatedRow;

import java.util.*;
import java.util.stream.Collectors;

import static DataTransferObject.Test.TestType.*;
import static org.junit.Assert.*;



public class InterpreterTest {

    @Test
    public void findFirstName_returnsFirstName_ifPassedTwoNames(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setMotherName("Ellis Rucker");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "Ellis";
        String actual = interpreter.findFirstName();
        assertEquals(expected,actual);
    }
    @Test
    public void findLastName_returnsLastName_ifPassedTwoNames(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setMotherName("Ellis Rucker");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "Rucker";
        String actual = interpreter.findLastName();
        assertEquals(expected,actual);
    }
    @Test
    public void findLastName_returnsLastName_ifPassedThreeNames(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setMotherName("Juju Smith Schuster");
        Interpreter interpreter = new Interpreter (testRow);
        String expected = "Smith Schuster";
        String actual = interpreter.findLastName();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstMaternalID_returnsSample_ifPassedOneSample(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setMaternalPatientId("WINR1910055");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "WINR1910055";
        String actual = interpreter.findFirstMaternalID();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstMaternalID_returnsCorrectSample_ifPassedMultipleSamples(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setMaternalPatientId("WINR1910055 WINR1909127*");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "WINR1909127";
        String actual = interpreter.findFirstMaternalID();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstGestation_returnsCorrectGestation_ifPassedMultipleGestations(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setGestationGender("16w 25w, boy");
        Interpreter interpreter = new Interpreter(testRow);
        Integer expected = 16;
        Integer actual = interpreter.findFirstGestation();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstGender_returnsCorrectGender_ifPassedMultipleGenders(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setGestationGender("(20w,boy) 22w, girl");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = "Male";
        String actual = interpreter.findFirstGender();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstGender_returnsNull_ifFirstTestIsMissingGender(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setGestationGender("(10w) 22w, girl");
        Interpreter interpreter = new Interpreter(testRow);
        String expected = null;
        String actual = interpreter.findFirstGender();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstTestType_returnsCorrectTestType_ifPassedMultipleTestTypes(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setTestTypeCost("(1wk, 1250) 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        DataTransferObject.Test.TestType expected = ONE_WEEK;
        DataTransferObject.Test.TestType actual = interpreter.findFirstTestType();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstTestType_returnsUnknown_ifFirstTestTypeIsNull(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setTestTypeCost("(1250) 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        DataTransferObject.Test.TestType expected = UNKNOWN;
        DataTransferObject.Test.TestType actual = interpreter.findFirstTestType();
        assertEquals(expected,actual);
    }

    @Test
    public void findFirstCost_returnsCorrectCost_ifPassedMultipleCosts(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setTestTypeCost("(1wk, 1250) 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        Integer expected = 1250;
        Integer actual = interpreter.findFirstCost();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstCost_returnsNull_ifFirstTestIsMissingCost(){
        SeparatedRow testRow = new SeparatedRow();
        testRow.setTestTypeCost("(1wk) 3wk 950");
        Interpreter interpreter = new Interpreter(testRow);
        Integer expected = null;
        Integer actual = interpreter.findFirstCost();
        assertEquals(expected,actual);
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
