package API_tests;


import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class RecipeTests extends BaseTest {

    @Test
    void getRecipePositiveTest() {
        given()
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch?query=pasta&offset=5&number=15&cuisine=italian&apiKey=4807df7ec09c41dd8f9e7acd7d2a811a")
                .then()
                .statusCode(200);
    }

    @Test
    void getRecipeWithParametersPositiveTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pasta")
                .queryParam("number", "5")
                .when()
                .get(getBaseURl() + "recipes/complexSearch")
                .then()
                .log().all()
                .statusCode(200);

    }

    @Test
    void getInfoByIdTest() {
        String id = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pasta")
                .queryParam("cuisine", "italian")
                .queryParam("includeIngredients", "tomato,cheese")
                .when()
                .get(getBaseURl() + "recipes/complexSearch")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("results[0].id")
                .toString();

        System.out.println(id);

        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .when().log().all()
                .get(getBaseURl() + "recipes/{id}/information", id)
                .body().prettyPeek()
                .jsonPath();
        Assertions.assertAll(
                () -> Assertions.assertEquals(response.get("vegetarian"), false),
                () -> Assertions.assertEquals(response.get("vegan"), false),
                () -> Assertions.assertEquals((Integer) response.get("id"), Integer.parseInt(id)),
                () -> Assertions.assertEquals(response.get("license"), "CC BY 3.0"),
                () -> Assertions.assertEquals((Integer) response.get("extendedIngredients[0].id"), 4053)
        );

    }

    @Test
    void getRecipeInfoWithAssertionsTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .when().log().all()
                .get(getBaseURl() + "recipes/{id}/information", 654835)
                .then()
                .assertThat()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .contentType(ContentType.JSON);

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("cuisine", "italian")
                .queryParam("diet", "vegetarian")
                .response()
                .contentType(ContentType.JSON)
                .expect()
                .body("vegetarian", is(false))
                .body("glutenFree", is(false))
                .body("gaps", is("no"))
                .body("healthScore", is(8))
                .body("extendedIngredients[0].name", is("olive oil"))
                .when()
                .get(getBaseURl() + "recipes/{id}/information", 654835);
    }

    @Test
    void getTotalRecipeWithAssertionsTest() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pasta")
                .queryParam("cuisine", "italian")
                .queryParam("includeIngredients", "tomato,cheese")
                .when()
                .get(getBaseURl() + "recipes/complexSearch")
                .body().prettyPeek()
                .jsonPath();
        assertThat(response.get("totalResults"), equalTo(9));
        assertThat(response.get("number"), equalTo(10));
        assertThat(response.get("offset"), equalTo(0));
        assertThat(response.get("results[0].id"), equalTo(654835));
    }

    @Test
    void addCuisineTest() {
        JsonPath response = given()
                .queryParam("title", "burger")
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseURl() + "recipes/cuisine")
                .body().prettyPeek()
                .jsonPath();
        assertThat(response.get("cuisine"), equalTo("American"));
        assertThat(response.get("cuisines[0]"), equalTo("American"));
        assertThat(response.get("confidence"), equalTo(0.85F));
    }

}
