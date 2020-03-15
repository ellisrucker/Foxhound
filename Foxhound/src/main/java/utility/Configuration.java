package utility;



import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

//Singleton for Application settings
public class Configuration {

    private static Configuration instance;
    private String targetFolder;
    private String terminalCSV;
    private String url;
    private String user;
    private String password;

    private Configuration() throws IOException {
        Properties prop = new Properties();
        FileInputStream inputStream = new FileInputStream("C:\\Users\\Work\\IdeaProjects\\Foxhound\\Foxhound\\src\\main\\resources\\config.properties");
        prop.load(inputStream);

        targetFolder = prop.getProperty("targetFolder");
        terminalCSV = prop.getProperty("terminalCSV");
        url = prop.getProperty("url");
        user = prop.getProperty("user");
        password = prop.getProperty("password");
    }


    public static Configuration getInstance() throws IOException {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getTargetFolder(){
        return targetFolder;
    }
    public String getTerminalCSV(){
        return terminalCSV;
    }
    public String getUrl(){
        return url;
    }
    public String getUser(){
        return user;
    }
    public String getPassword(){
        return password;
    }
}
