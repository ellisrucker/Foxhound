import logic.Interpreter;
import org.apache.commons.collections.ArrayStack;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
}
