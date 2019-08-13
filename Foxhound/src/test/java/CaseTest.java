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

    @Test
    public void fullNameSplit_returnsTwoNames_ifPassedTwoNames(){
        String fullName = "Ellis Rucker";
        String[] expected = new String[]{"Ellis","Rucker"};
        String[] actual = fullName.split(" ", 2);
        assertArrayEquals(expected,actual);
    }

    @Test
    public void fullNameSplit_returnsSingleName_ifPassedSingleName(){
        String fullName = "Ellis";
        String[] expected = new String[]{"Ellis"};
        String[] actual = fullName.split(" ", 2);
        assertArrayEquals(expected,actual);
    }

    @Test
    public void fullNameSplit_returnsStringArray_ifPassedThreeNames(){
        String fullName = "Matthew Ellis Rucker";
        String[] expected = new String[]{"Matthew","Ellis Rucker"};
        String[] actual = fullName.split(" ", 2);
        assertArrayEquals(expected,actual);
    }

    @Test
    public void constructor_setsFirstName_ifPassedTwoNames(){
        String[] nameArray = new String[]{"Ellis","Rucker"};
        String actual = null;
        if (nameArray.length == 2){
            actual = nameArray[0];
        }
        else if (nameArray.length == 1) {
            actual = nameArray[0];
        }
        String expected = "Ellis";
        assertTrue(expected == actual);
    }

    @Test
    public void constructor_setsFirstName_ifPassedSingleName(){
        String[] nameArray = new String[]{"Ellis"};
        String actual = null;
        if (nameArray.length == 2){
            actual = nameArray[0];
        }
        else if (nameArray.length == 1) {
            actual = nameArray[0];
        }
        String expected = "Ellis";
        assertTrue(expected == actual);
    }

    @Test
    public void constructor_setsLastName_ifPassedTwoNames(){
        String[] nameArray = new String[]{"Ellis","Rucker"};
        String actual = null;
        if (nameArray.length == 2){
            actual = nameArray[1];
        }
        String expected = "Rucker";
        assertTrue(expected == actual);
    }

    @Test
    public void constructor_leavesLastNameNull_ifPassedSingleName(){
        String[] nameArray = new String[]{"Ellis"};
        String actual = null;
        if (nameArray.length == 2){
            actual = nameArray[1];
        }
        assertTrue(actual == null);
    }

    @Test
    public void stringHash_returnsInteger_ifPassedString(){
        String str = "Ellis";
        Integer hash = 17;
        for (int i = 0; i < str.length(); i++){
            hash = ((hash * 31) + str.charAt(i));
        }
        System.out.println(hash);
        assertTrue(hash instanceof Integer);
    }

    @Test
    public void stringHash_returnsDifferentHashes_ifPassedMultipleStrings(){
        String str1 = "Ellis";
        String str2 = "Brett";
        Integer hash1 = 17;
        Integer hash2 = 17;
        for (int i = 0; i < str1.length(); i++) {
            hash1 = ((hash1 * 31) + str1.charAt(i));
        }
        for (int i = 0; i < str2.length(); i++) {
            hash2 = ((hash2 * 31) + str2.charAt(i));
        }
        System.out.println("Ellis: "+ hash1);
        System.out.println("Brett: "+ hash2);
        assertFalse(hash1.equals(hash2));
    }

    @Test
    public void stringHash_returnsSameHash_ifPassedIdenticalStrings(){
        String str1 = "Ellis";
        String str2 = "Ellis";
        Integer hash1 = 17;
        Integer hash2 = 17;
        for (int i = 0; i < str1.length(); i++) {
            hash1 = ((hash1 * 31) + str1.charAt(i));
        }
        for (int i = 0; i < str2.length(); i++) {
            hash2 = ((hash2 * 31) + str2.charAt(i));
        }
        System.out.println("Ellis: "+ hash1);
        System.out.println("Ellis: "+ hash2);
        assertTrue(hash1.equals(hash2));
    }

    //TODO: fix test
    @Before
    public Integer stringHash(String str){
        Integer hash = 17;
        for (int i = 0; i < str.length(); i++){
            hash = ((hash * 31) + str.charAt(i));
        }
        return hash;
    }
    @Test
    public void setCaseState_addsIntegersToArray_ifPassedStringArray(){
        String[] currentRow = new String[]{"Columbia","Maryland","21075","The Old Line State"};
        ArrayList<Integer> caseState = new ArrayList<>();
        for (String str : currentRow){
            caseState.add(stringHash(str));
        }
        for (Integer hash : caseState){
            System.out.println(hash.toString());
        }
        assertTrue((caseState.get(0) instanceof Integer) && (caseState.get(2) instanceof Integer));
    }


}
