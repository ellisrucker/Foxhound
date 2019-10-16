import logic.Interpreter;
import org.junit.Before;
import org.junit.Test;

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

}
