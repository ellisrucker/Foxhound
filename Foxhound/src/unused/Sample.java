package DataTransferObject;

import java.util.Date;

public class Sample {

    private String id;
    private Date dateRecieved;

    public void setId(String newId) {
        this.id = newId;
    }
    public String getId() {
        return this.id;
    }
    public boolean sampleExists(String newId) {
        return allSamples.contains(newId);
    }

}
