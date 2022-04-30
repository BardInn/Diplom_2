import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CustomerClient extends BurgerRestClient {
	private static final String CUSTOMER_PATH = "/api/auth/";

	@Step("Create new customer")
	public ValidatableResponse create(Customer customer) {
		return given()
				.spec(getBaseSpec())
				.body(customer)
				.when()
				.post(CUSTOMER_PATH + "register")
				.then();
	}

	@Step("Create customer with missing field")
	public String createCustomerWithMissingField(Customer customer) {
		return given()
				.spec(getBaseSpec())
				.body(customer)
				.when()
				.post(CUSTOMER_PATH + "register")
				.then().log().all()
				.assertThat()
				.statusCode(403)
				.extract()
				.path("message");
	}

	@Step("Login customer")
	public ValidatableResponse login(CustomerCredentials credentials) {
		return given()
				.spec(getBaseSpec())
				.body(credentials)
				.when()
				.post(CUSTOMER_PATH + "login")
				.then();
	}

	@Step("Delete customer")
	public ValidatableResponse delete(Customer customer) {
		return given()
				.spec(getBaseSpec())
				.auth().oauth2(customer.getCustomerToken().getToken())
				.when()
				.delete(CUSTOMER_PATH + "user")
				.then()
				.assertThat()
				.statusCode(202);
	}

	@Step("Change customer data")
	public ValidatableResponse changeCustomerData(Customer customer) {
		return given()
				.spec(getBaseSpec())
				.auth().oauth2(customer.getCustomerToken().getToken())
				.and()
				.body(customer)
				.when()
				.patch(CUSTOMER_PATH + "user")
				.then()
				.assertThat().body("user", notNullValue())
				.and()
				.statusCode(200);
	}

	@Step("Change non-authorized customer data")
	public ValidatableResponse changeCustomerDataWithOutAuthorization(Customer customer) {
		return given()
				.spec(getBaseSpec())
				.body(customer)
				.when()
				.patch(CUSTOMER_PATH + "user")
				.then()
				.assertThat()
				.statusCode(401);
	}

}
