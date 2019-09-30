
public class SeparatedRow {

    //Object representation of current row
    //Each cell of currentRow becomes a String attribute
    private String date;
    private String motherName;
    private String maternalPatientId;
    private String paternalPatientId;
    private String gestationGender;
    private String testTypeCost;
    private String referral;
    private String genotypeA;
    private String genotypeB;
    private String firstDraw;
    private String secondDraw;
    private String thirdDraw;
    private String result;
    private String confirmation;

    public SeparatedRow (String[] currentRow){
        date = currentRow[0];
        motherName = currentRow[1];
        maternalPatientId = currentRow[2];
        paternalPatientId = currentRow[3];
        gestationGender = currentRow[4];
        testTypeCost = currentRow[5];
        referral = currentRow[6];
        genotypeA = currentRow[9];
        genotypeB = currentRow[10];
        firstDraw = currentRow[11];
        secondDraw = currentRow[12];
        thirdDraw = currentRow[13];
        result = currentRow[16];
        confirmation = currentRow[17];
    }

}

