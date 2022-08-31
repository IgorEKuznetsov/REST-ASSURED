package API_tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
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
    private static int id;
    protected static ResponseSpecification recipeResponseSpecification;
    protected static RequestSpecification recipeRequestSpecification;
    protected static RequestSpecification shoppingListRequestSpecification;



    @BeforeAll
    static void initTest() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        in = new FileInputStream("src/main/resources/my.properties");
        properties.load(in);

        apiKey = properties.getProperty("apiKey");
        baseURl = properties.getProperty("baseURL");
        username = properties.getProperty("username");
        hash = properties.getProperty("hash");
        id = Integer.parseInt(properties.getProperty("id"));

        recipeResponseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        recipeRequestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        shoppingListRequestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .addQueryParam("hash", hash)
                .log(LogDetail.ALL)
                .build();


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

    public static int getId() {
        return id;
    }
}
