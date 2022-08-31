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
    void getRecipeWithParametersPositiveTest() {
        given()
                .spec(recipeRequestSpecification)
                .queryParam("query", "pasta")
                .queryParam("number", "5")
                .when()
                .get(getBaseURl() + EndPoints.searchRecipes)
                .then()
                .spec(recipeResponseSpecification);

    }

    @Test
    void getInfoByIdTest() {
        String id = given()
                .spec(recipeRequestSpecification)
                .queryParam("query", "pasta")
                .queryParam("cuisine", "italian")
                .queryParam("includeIngredients", "tomato,cheese")
                .when()
                .get(getBaseURl() + EndPoints.searchRecipes)
                .then()
                .spec(recipeResponseSpecification)
                .extract()
                .jsonPath()
                .get("results[0].id")
                .toString();

        System.out.println(id);

        JsonPath response = given()
                .spec(recipeRequestSpecification)
                .when()
                .get(getBaseURl() + EndPoints.recipesInformation, id)
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
                .spec(recipeRequestSpecification)
                .when()
                .get(getBaseURl() + EndPoints.recipesInformation, getId())
                .then()
                .spec(recipeResponseSpecification);

        given()
                .spec(recipeRequestSpecification)
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
                .get(getBaseURl() + EndPoints.recipesInformation, getId());
    }

    @Test
    void getTotalRecipeWithAssertionsTest() {
        JsonPath response = given()
                .spec(recipeRequestSpecification)
                .queryParam("query", "pasta")
                .queryParam("cuisine", "italian")
                .queryParam("includeIngredients", "tomato,cheese")
                .when()
                .get(getBaseURl() + EndPoints.searchRecipes)
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
                .spec(recipeRequestSpecification)
                .queryParam("title", "burger")
                .when()
                .post(getBaseURl() + EndPoints.recipesCuisine)
                .body().prettyPeek()
                .jsonPath();
        assertThat(response.get("cuisine"), equalTo("American"));
        assertThat(response.get("cuisines[0]"), equalTo("American"));
        assertThat(response.get("confidence"), equalTo(0.85F));
    }

}
