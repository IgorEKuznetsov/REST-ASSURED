package API_tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BaseTest {
    static Properties properties = new Properties();
    private static InputStream in;
    private static String apiKey;
    private static String baseURl;
    private static String username;
    private static String hash;



    @BeforeAll
    static void initTest() throws IOException {
        in = new FileInputStream("src/main/resources/my.properties");
        properties.load(in);

        apiKey = properties.getProperty("apiKey");
        baseURl = properties.getProperty("baseURL");
        username = properties.getProperty("username");
        hash = properties.getProperty("hash");

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getBaseURl() {
        return baseURl;
    }

    public static String getUsername() {
        return username;
    }

    public static String getHash() {
        return hash;
    }
}
