import org.junit.Test;
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
}
