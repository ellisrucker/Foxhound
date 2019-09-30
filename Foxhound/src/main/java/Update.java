import java.util.ArrayList;
import java.util.Date;

public class Update {

    private Integer updateId;
    private String file;
    private Date date;
    private ArrayList<String> updatedFields;
    private static Integer i = 1;

    
    public Update (String file, Date date, ArrayList<String> updatedFields) {
        updateId = i;
        file = file;
        date = date;
        updatedFields = updatedFields;
        i++;


    }

}
