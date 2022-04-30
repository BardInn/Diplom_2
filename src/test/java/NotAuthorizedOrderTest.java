import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NotAuthorizedOrderTest {
	private Customer customer;
	private OrderClient orderClient;
	private List<String> ingredientsList;

	@Before
	public void setUp() {
		CustomerClient customerClient = new CustomerClient();
		orderClient = new OrderClient();
		customer = Customer.getRandom();
		ValidatableResponse isCreated = customerClient.create(customer);
		ValidatableResponse getIngredients = orderClient.getIngredient();
		customer.getCustomerToken().setAccessToken(isCreated.extract().path("accessToken"));
		ingredientsList = getIngredients.extract().jsonPath().getList("data._id");
	}

	@Test
	@DisplayName("Set customer order")
	@Description("Try to set orders to non-authorized customer")
	public void setOrderTest() {
		List<String> newOrder = new ArrayList<>(ingredientsList.subList(12, ingredientsList.size() - 1));
		ValidatableResponse setOrder = orderClient.setOrderWithoutAuthorization(new Ingredients(newOrder));
		boolean success = setOrder.extract().path("success");
		int statusCode = setOrder.extract().statusCode();
		assertEquals(401, statusCode);
		assertFalse(success);
	}

	@Test
	@DisplayName("Get customer order")
	@Description("Try to get orders from non-authorized customer")
	public void getCustomerOrdersTest() {
		ValidatableResponse getOrders = orderClient.getOrdersWithoutAuthorization(customer);
		boolean success = getOrders.extract().path("success");
		assertFalse(success);
	}
}
