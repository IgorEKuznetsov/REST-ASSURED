package API_tests;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ShoppingListTests extends BaseTest {
    String id;

    /*
     Shopping list tests
     */
    @Test
    void addReadShoppingListTest() {
        id = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .body("{\"item\":\"1packagebakingpowder\",\"aisle\":\"Baking\",\"parse\":true}")
                .when()
                .post(getBaseURl() + "mealplanner/{username}/shopping-list/items", getUsername())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        System.out.println(id);

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when().log().all()
                .get(getBaseURl() + "mealplanner/{username}/shopping-list", getUsername())
                .body().prettyPeek()
                .jsonPath();
        assertThat(response.get("aisles[0].items[0].id"), equalTo(Integer.parseInt(id)));
        assertThat(response.get("aisles[0].items[0].name"), equalTo("bakingpowder"));
        assertThat(response.get("aisles[0].items[0].measures.original.amount"), equalTo(Float.parseFloat("1.0")));
    }
    @AfterEach
    void killShoppingItemById(){   //:=)
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("hash", getHash())
                .when().log().all()
                .delete(getBaseURl() + "mealplanner/{username}/shopping-list/items/{id}", getUsername(), id)
                .then()
                .statusCode(200);
    }
}
