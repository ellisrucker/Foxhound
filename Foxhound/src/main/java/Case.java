import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;


public class Case {

    private String id;
    private String motherFirstName;
    private String motherLastName;
    private Date dateStarted;
    private Source sourceType;
    private Gender gender;
    private boolean twins;
    //List of all Case IDs
    private static HashSet<String> allCases = new HashSet<>();

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

    //Setters and Getters
    public String getMotherName(){
        return this.motherFirstName + " " + this.motherLastName;
    }
    // TODO: create boolean compareStates Method

    public void setCaseState(String[] currentRow){
        this.caseState.clear();
        for (String str : currentRow){
            this.caseState.add(str.hashCode());
        }

    }

}