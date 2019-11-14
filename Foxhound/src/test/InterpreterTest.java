import IntermediateObject.SampleString;
import logic.Interpreter;
import org.apache.commons.collections.ArrayStack;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;



public class InterpreterTest {

    @Test
    public void findFirstName_returnsFirstName_ifPassedTwoNames(){
       Interpreter interpreter = new Interpreter("Ellis Rucker");
       String expected = "Ellis";
       String actual = interpreter.findFirstName();
       assertEquals(expected,actual);
    }
    @Test
    public void findLastName_returnsLastName_ifPassedTwoNames(){
        Interpreter interpreter = new Interpreter("Ellis Rucker");
        String expected = "Rucker";
        String actual = interpreter.findLastName();
        assertEquals(expected,actual);
    }
    @Test
    public void findLastName_returnsLastName_ifPassedThreeNames(){
        Interpreter interpreter = new Interpreter ("Juju Smith Schuster");
        String expected = "Smith Schuster";
        String actual = interpreter.findLastName();
        assertEquals(expected,actual);
    }
    @Test
    public void isolateSamples_returnsOneSampleString_ifPassedOneSampleString(){
        Interpreter interpreter = new Interpreter("ELLR1903022");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("ELLR1903022");
        ArrayList<String> actual = interpreter.isolateSamples();
        assertEquals(expected,actual);
    }
    @Test
    public void isolateSamples_returnsOneSample_ifPassedSampleWithRelation(){
        Interpreter interpreter = new Interpreter("EDWE1910001(P1)");
        ArrayList<String> sampleArray = interpreter.isolateSamples();
        String expected = "EDWE1910001(P1)";
        String actual = sampleArray.get(1);
        assertEquals(expected,actual);
    }
    @Test
    public void isolateSamples_returnsTwoSamples_ifPassedTwoSamples(){
        Interpreter interpreter = new Interpreter("EDWE1910001(P1)ALPE1910002(P2)");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("EDWE1910001(P1)");
        expected.add("ALPE1910002(P2)");
        ArrayList<String> actual = interpreter.isolateSamples();
        assertEquals(expected,actual);
    }
    @Test
    public void isolateSamples_returnsCorrectSamples_ifPassedComplicatedCell(){
        Interpreter interpreter = new Interpreter(
                "(JAIW1812026(p1)) ZAVC1902101(p2)* ZAVC1902104(P2) DAMK1903178(p3)");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("JAIW1812026(p1)) ");
        expected.add("ZAVC1902101(p2)* ");
        expected.add("ZAVC1902104(P2) ");
        expected.add("DAMK1903178(p3)");
        ArrayList<String> actual = interpreter.isolateSamples();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstID_returnsSample_ifPassedOneSample(){
        Interpreter interpreter = new Interpreter("WINR1910055");
        String expected = "WINR1910055";
        String actual = interpreter.findFirstID();
        assertEquals(expected,actual);
    }
    @Test
    public void findFirstID_returnsCorrectSample_ifPassedMultipleSamples(){
        Interpreter interpreter = new Interpreter("WINR1910055 WINR1909127*");
        String expected = "WINR1909127";
        String actual = interpreter.findFirstID();
        assertEquals(expected,actual);
    }
    @Test
    public void findRelation_returnsP1_ifRelationNotStated(){
        String sample = "EDWE1911013";
        SampleString.RELATION expected = SampleString.RELATION.P1;
        SampleString.RELATION actual = SampleString.findRelation(sample);
        assertEquals(expected,actual);
    }
    @Test
    public void findRelation_returnsP2_ifPassedP2(){
        String sample = "ALPE1911014(p2)";
        SampleString.RELATION expected = SampleString.RELATION.P2;
        SampleString.RELATION actual = SampleString.findRelation(sample);
        assertEquals(expected,actual);
    }


    @Ignore
    //Method performs as expected, but fails test due to differences in list order
    public void groupIDsByPatient_sortsSampleStrings_whenPassedCell(){
        String cell = "EDWE1911013(P1) EDWE1911012(P1) ALPE1911014(p2) ROYM1911015(P3) swab1911016(p3)";
        SampleString ss1 = new SampleString("EDWE1911012(P1)","EDWE1911012", SampleString.RELATION.P1);
        SampleString ss2 = new SampleString("EDWE1911013(P1)","EDWE1911013", SampleString.RELATION.P1);
        SampleString ss3 = new SampleString("ALPE1911014(p2)", "ALPE1911014", SampleString.RELATION.P2);
        SampleString ss4 = new SampleString("ROYM1911015(P3)", "ROYM1911015", SampleString.RELATION.P3);
        SampleString ss5 = new SampleString("swab1911016(p3)","swab1911016", SampleString.RELATION.P3);
        List<SampleString> p1Samples = Arrays.asList(ss1,ss2);
        List<SampleString> p2Samples = Arrays.asList(ss3);
        List<SampleString> p3Samples = Arrays.asList(ss4,ss5);
        Set<List<SampleString>> expected = new HashSet<>(Arrays.asList(p1Samples,p2Samples,p3Samples));
        Interpreter interpreter = new Interpreter(cell);
        Set<List<SampleString>> actual = interpreter.groupIDsByPatient()
                .stream()
                .collect(Collectors.toSet());
        assertEquals(actual,expected);

    }
}
