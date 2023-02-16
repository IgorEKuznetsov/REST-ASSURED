package API_tests;

import dto.Response;
import dto.ShoppingList;

import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ShoppingListTests extends BaseTest {
    static String id;

    /*
     Shopping list tests
     */
    @Test
    void addReadShoppingListTest() {
        ShoppingList list = new ShoppingList();
        list.setItem("fresh water");
        list.setAisle("water");
        list.setParse(true);

       id = given()
                .spec(shoppingListRequestSpecification)
                .body(list)
                .when()
                .post(getBaseURl() + EndPoints.addMealPlanItem, getUsername())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        Response response = given()
                .spec(shoppingListRequestSpecification)
                .when().log().all()
                .get(getBaseURl() + EndPoints.mealPlanList, getUsername())
                .then()
                .extract()
                .body()
                .as(Response.class);

        List<String> aisleList = response.getAisles()
                .stream()
                .map(a -> a.getAisle())
                .collect(Collectors.toList());
        System.out.println(aisleList);

        assertThat(aisleList.get(0), equalTo(list.getAisle()));
        assertThat(response.getAisles().toString(), containsString(list.getItem()));

    }

    @Test
    void checkIDTest() {
        ShoppingList list = new ShoppingList();
        list.setItem("black tea");
        list.setAisle("tea");
        list.setParse(true);

        id = given()
                .spec(shoppingListRequestSpecification)
                .body(list)
                .when()
                .post(getBaseURl() + EndPoints.addMealPlanItem, getUsername())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        Response response = given()
                .spec(shoppingListRequestSpecification)
                .when().log().all()
                .get(getBaseURl() + EndPoints.mealPlanList, getUsername())
                .then()
                .extract()
                .body()
                .as(Response.class);

        List<Response.Aisle> aisles = response.getAisles();
        String itemId = aisles.get(0).getItems().get(0).getId().toString();

        assertThat(itemId, equalTo(id));

    }

    @AfterEach
    void killShoppingItemById() {
        given()
                .spec(shoppingListRequestSpecification)
                .when().log().all()
                .delete(getBaseURl() + EndPoints.delMealPlanItem, getUsername(), id)
                .then()
                .statusCode(200);
    }
}
