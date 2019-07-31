package Test;

import static org.junit.Assert.*;

public class idToNumberTest {

    public static Integer idToNumber(String id) {

        id = id.replaceAll("\\D", "");
        return Integer.parseInt(id);

    }

}