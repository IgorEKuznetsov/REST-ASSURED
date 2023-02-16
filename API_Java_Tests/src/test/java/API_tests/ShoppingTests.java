package API_tests;

import dto.Item;

import dto.Response;
import dto.ShoppingList;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ShoppingTests extends BaseTest {
    static String itemId;

    @Test
    void createGetListTest() {
        ShoppingList list = new ShoppingList();
        list.setItem("black tea");
        list.setAisle("tea");
        list.setParse(true);

        Item postResponse = given()
                .spec(shoppingListRequestSpecification)
                .body(list)
                .when()
                .post(getBaseURl() + EndPoints.addMealPlanItem, getUsername())
                .then()
                .statusCode(200)
                .extract()
                .as(Item.class);

        assertThat(list.getItem(), equalTo(postResponse.getName()));
        assertThat(list.getAisle(), containsString("tea"));

        Item response = given()
                .spec(shoppingListRequestSpecification)
                .when().log().all()
                .get(getBaseURl() + EndPoints.mealPlanList, getUsername())
                .then()
                .extract()
                // .body().asString();
                //Item res = JsonPath.from(response).getObject("items[0]", Item.class);
                // .jsonPath("items[0]", Item.class);
                .body()
                .as(Response.class).getAisles().get(0).getItems().get(0);

        itemId = response.getId().toString();

        Assertions.assertThat(response).isEqualTo(postResponse);
    }

    @Test
    void killShoppingItemByIdAndCheckTest() {
        given()
                .spec(shoppingListRequestSpecification)
                .when().log().all()
                .delete(getBaseURl() + EndPoints.delMealPlanItem, getUsername(), itemId)
                .then()
                .statusCode(200);

        given()
                .spec(shoppingListRequestSpecification)
                .when().log().all()
                .delete(getBaseURl() + EndPoints.delMealPlanItem, getUsername(), itemId).prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
