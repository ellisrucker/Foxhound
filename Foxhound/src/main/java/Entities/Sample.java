package Entities;

import java.util.Date;
import java.util.HashSet;

public class Sample {

    private String id;
    private Date dateRecieved;
    private static HashSet<String> allSamples = new HashSet<String>();

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
