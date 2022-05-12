import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;


import static io.restassured.RestAssured.given;

public class OrderClient extends BurgerRestClient {
	private static final String ORDER_PATH = "/api/orders/";
	private static final String INGREDIENTS = "api/ingredients";

	@Step("Get customer orders")
	public ValidatableResponse getOrders(Customer customer) {
		return given()
				.spec(getBaseSpec())
				.auth().oauth2(customer.getCustomerToken().getToken())
				.when()
				.get(ORDER_PATH)
				.then()
				.assertThat()
				.statusCode(200);
	}

	@Step("Get ingredients")
	public ValidatableResponse getIngredient() {
		return given()
				.spec(getBaseSpec())
				.get(INGREDIENTS)
				.then();
	}

	@Step("Set order from non-authorized customer")
	public ValidatableResponse setOrderWithoutAuthorization(Ingredients ingredients) {
		return given()
				.spec(getBaseSpec())
				.body(ingredients)
				.when()
				.post(ORDER_PATH)
				.then();
	}

	@Step("Set order from authorized customer")
	public ValidatableResponse setOrder(Customer customer, Ingredients ingredients) {
		return given()
				.spec(getBaseSpec())
				.auth().oauth2(customer.getCustomerToken().getToken())
				.and()
				.body(ingredients)
				.when()
				.post(ORDER_PATH)
				.then();
	}

	@Step("Set order from non-authorized customer")
	public ValidatableResponse getOrdersWithoutAuthorization(Customer customer) {
		return given()
				.spec(getBaseSpec())
				.get(ORDER_PATH)
				.then()
				.assertThat()
				.statusCode(401);
	}
}
