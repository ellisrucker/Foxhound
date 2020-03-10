import DataTransferObject.Case;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CaseTest {

    //Test Naming Conventions:
    //MethodName_ExpectedBehavior_StateUnderTest

    @Test
    public void idToNumber_returnsInteger_ifPassedString() {
        String id = "MONH1903022";
        Integer result = Case.idToNumber(id);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void idToNumber_removesLetters_ifPassedString() {
        String id = "MONH1903022";
        Integer expectedResult = 1903022;
        Integer actualResult = Case.idToNumber(id);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void idToNumber_throwsException_ifPassedNull() {
        String id = null;
        Integer result = Case.idToNumber(id);
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
        String[] actual = Case.fullNameSplit(fullName);
        assertArrayEquals(expected,actual);
    }

    @Test
    public void fullNameSplit_returnsSingleName_ifPassedSingleName(){
        String fullName = "Ellis";
        String[] expected = new String[]{"Ellis"};
        String[] actual = Case.fullNameSplit(fullName);
        assertArrayEquals(expected,actual);
    }

    @Test
    public void fullNameSplit_returnsStringArray_ifPassedThreeNames(){
        String fullName = "Matthew Ellis Rucker";
        String[] expected = new String[]{"Matthew","Ellis Rucker"};
        String[] actual = Case.fullNameSplit(fullName);
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
        Integer hash = Case.stringHash(str);
        assertTrue(hash instanceof Integer);
    }

    @Test
    public void stringHash_returnsDifferentHashes_ifPassedMultipleStrings(){
        String str1 = "Ellis";
        String str2 = "Brett";
        Integer hash1 = Case.stringHash(str1);
        Integer hash2 = Case.stringHash(str2);
        System.out.println("Ellis: "+ hash1);
        System.out.println("Brett: "+ hash2);
        assertFalse(hash1.equals(hash2));
    }

    @Test
    public void stringHash_returnsSameHash_ifPassedIdenticalStrings(){
        String str1 = "Ellis";
        String str2 = "Ellis";
        Integer hash1 = Case.stringHash(str1);
        Integer hash2 = Case.stringHash(str2);
        System.out.println("Ellis: "+ hash1);
        System.out.println("Ellis: "+ hash2);
        assertTrue(hash1.equals(hash2));
    }

    @Test
    public void setCaseState_addsIntegersToArray_ifPassedStringArray() throws ParseException {
        String[] currentRow = new String[]{"Columbia","Maryland","21075","The Old Line State"};
        Case testCase = new Case();
        testCase.setCaseState(currentRow);
        ArrayList<Integer> result = testCase.getCaseState();
        assertTrue((result.get(0) instanceof Integer) && (result.get(2) instanceof Integer));
    }


}
