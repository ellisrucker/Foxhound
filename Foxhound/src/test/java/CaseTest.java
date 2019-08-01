import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaseTest {

    //Test Naming Conventions:
    //MethodName_ExpectedBehavior_StateUnderTest

    @Test
    public void idToNumber_RemovesNonDigits_IfIdIsValid() {
        String id = "MONH1903022";
        id = id.replaceAll( "\\D", "");
        Integer.parseInt(id);
        assertEquals(1903022,id);
    }
}
