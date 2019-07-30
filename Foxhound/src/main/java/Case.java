import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;


public class Case {

    private String id;
    private int idNumber;
    private String motherFirstName;
    private String motherLastName;
    private Date dateStarted;
    private Source sourceType;
    private Gender gender;
    private boolean twins;
    //List of all Case IDs
    private static HashSet<Case> allCases = new HashSet<>(500);

    //Stores hashed representation of Case's last row
    private ArrayList<Integer> caseState;

    //Record of each time Case is updated, use in Update's constructor!
    private ArrayList<Update> caseLog;

    //Becomes Update's ID, increment with every new Update
    private Integer updateNumber = 1;

    enum Gender {
        Male, Female, MFTWINS
    }
    enum Source {
        RAVGEN, REFERRAL, SELFSELL
    }
    //overriding Object's equals method only overrides it
    //for Case class right?
    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (o == null) {
            return false;
        }
        //Typecast Object down to a Case
        Case c = (Case) o;
        return c.idNumber == this.idNumber;
    }
    @Override
    public int hashCode() {
        return this.idNumber;
    }
    public int idToNumber(String id) {
        id = id.replaceAll( "\\D", "");
        return Integer.parseInt(id);
    }
    public boolean caseExists(String stringId){
        int id = idToNumber(stringId);
        return allCases.contains(id);

    }



    //Setters and Getters
    public void setId(String id) {
        this.id = id;
    }

    // TODO: create setName method: separates full name into first and last


    public String getMotherName() {
        return this.motherFirstName + " " + this.motherLastName;
    }
    //TODO: create boolean compareStates Method

    public void setCaseState(String[] currentRow){
        this.caseState.clear();
        for (String str : currentRow){
            this.caseState.add(str.hashCode());
        }

    }

}
