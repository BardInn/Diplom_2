import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;

import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;
public class BurgerRestClient {
	public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

	protected static RequestSpecification getBaseSpec() {
		return new RequestSpecBuilder()
				.addFilter(new AllureRestAssured())
				.setContentType(JSON)
				.setBaseUri(BASE_URL)
				.build();
	}
}
