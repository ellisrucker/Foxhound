import IntermediateObject.SampleString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SampleStringTest {
    @Test
    public void findRelation_returnsP1_ifRelationNotStated(){
        String sample = "EDWE1911013";
        SampleString.Relation expected = SampleString.Relation.P1;
        SampleString.Relation actual = SampleString.findRelation(sample);
        assertEquals(expected,actual);
    }
    @Test
    public void findRelation_returnsP2_ifPassedP2(){
        String sample = "ALPE1911014(p2)";
        SampleString.Relation expected = SampleString.Relation.P2;
        SampleString.Relation actual = SampleString.findRelation(sample);
        assertEquals(expected,actual);
    }

}
