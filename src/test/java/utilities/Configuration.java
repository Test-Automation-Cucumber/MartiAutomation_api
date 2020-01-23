package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class Configuration {

    private static Configuration instance;
    private Properties configProps = new Properties();

//    private String testTarget;
    private String tenMinutesMail;
     
    public static Configuration getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private static synchronized void createInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
    }

    private Configuration() {

        InputStream is = null;
        try {
            is = ClassLoader.getSystemResourceAsStream("test_config.property");
            Reader reader = new InputStreamReader(is, "UTF-8");
            configProps.load(reader);

//            this.testTarget = configProps.getProperty("testTarget");
            this.tenMinutesMail = configProps.getProperty("tenMinutesMail");
            
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public String gettestTarget() { return testTarget; }
    public String gettenMinutesMail() { return tenMinutesMail; }

}
