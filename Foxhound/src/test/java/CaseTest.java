import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CaseTest {

    //Test Naming Conventions:
    //MethodName_ExpectedBehavior_StateUnderTest

    @Test
    public void idToNumber_returnsInteger_ifPassedString() {
        String id = "MONH1903022";
        id = id.replaceAll("\\D", "");
        Integer idNumber = Integer.parseInt(id);
        assertTrue(idNumber instanceof Integer);
    }

    @Test
    public void idToNumber_removesLetters_ifPassedString() {
        String id = "MONH1903022";
        Integer expectedResult = 1903022;
        id = id.replaceAll("\\D", "");
        Integer idNumber = Integer.parseInt(id);
        assertEquals(expectedResult, idNumber);
    }

    @Test(expected = NullPointerException.class)
    public void idToNumber_throwsException_ifPassedNull() {
        String id = null;
        id = id.replaceAll("\\D", "");
        Integer idNumber = Integer.parseInt(id);
    }

    @Test
    public void caseExists_returnsTrue_ifInputInArrayList() {
        ArrayList<Integer> allCases = new ArrayList<>();
        allCases.add(1903001);
        allCases.add(1903002);
        allCases.add(1903003);
        Integer idNumber = 1903002;
        assertTrue(allCases.contains(idNumber));
    }

    @Test
    public void caseExists_returnsFalse_ifInputNotInArrayList() {
        ArrayList<Integer> allCases = new ArrayList<>();
        allCases.add(1903001);
        allCases.add(1903002);
        allCases.add(1903003);
        Integer idNumber = 1903004;
        assertFalse(allCases.contains(idNumber));
    }
}
