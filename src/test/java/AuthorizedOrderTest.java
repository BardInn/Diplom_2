import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class AuthorizedOrderTest {
	private CustomerClient customerClient;
	private Customer customer;
	private OrderClient orderClient;
	private  List<String> ingredientsList;


	@Before
	public void setUp() {
		customerClient = new CustomerClient();
		orderClient = new OrderClient();
		customer = Customer.getRandom();
		customerClient.create(customer);
		ValidatableResponse loginResponse = customerClient.login(new CustomerCredentials(customer.getEmail(), customer.getPassword()));
		customer.getCustomerToken().setAccessToken(loginResponse.extract().path("accessToken"));
		ValidatableResponse getIngredients = orderClient.getIngredient();
		ingredientsList = getIngredients.extract().jsonPath().getList("data._id") ;
	}

	@After
	public void tearDown() {
		ValidatableResponse tearDown =  customerClient.delete(customer);
		assertThat("Customer deleted", tearDown.extract().statusCode(), equalTo(202));
	}
	
	@Test
	@DisplayName("Set new order")
	@Description("Try to do new order to authorized customer")
	public void setOrderTest() {
		List<String> newOrder = new ArrayList<>(ingredientsList.subList(9, ingredientsList.size() - 1));
		ValidatableResponse setOrder = orderClient.setOrder(customer, new Ingredients(newOrder));
		boolean success = setOrder.extract().path("success");
		int statusCode = setOrder.extract().statusCode();
		assertEquals(200, statusCode);
		assertTrue(success);
	}

	@Test
	@DisplayName("Set new order")
	@Description("Try to do new order to authorized customer with non-existing ingredients")
	public void setOrderWithWrongHushTest() {
		List<String> wrongIngredientsList = new ArrayList<>();
		wrongIngredientsList.add("60d3b41abdac");
		wrongIngredientsList.add("609646e4dc916e0");
		ValidatableResponse setOrder = orderClient.setOrder(customer, new Ingredients(wrongIngredientsList));
		int statusCode = setOrder.extract().statusCode();
		assertEquals(500, statusCode);
	}

	@Test
	@DisplayName("Set new order")
	@Description("Try to do new order to authorized customer without ingredients")
	public void setOrderWithoutIngredientsTest() {
		List<String> emptyIngredientsList = new ArrayList<>();
		ValidatableResponse setOrder = orderClient.setOrder(customer, new Ingredients(emptyIngredientsList));
		int statusCode = setOrder.extract().statusCode();
		boolean success = setOrder.extract().path("success");
		assertEquals(400, statusCode);
		assertFalse(success);
	}

	@Test
	@DisplayName("Get customer order")
	@Description("Try to get orders from authorized customer")
	public void getCustomerOrdersTest(){
		List<String> newOrder = new ArrayList<>(ingredientsList.subList(9, ingredientsList.size() - 1));
		ValidatableResponse setOrder = orderClient.setOrder(customer, new Ingredients(ingredientsList));
		ValidatableResponse setOrder1 = orderClient.setOrder(customer, new Ingredients(newOrder));
		ValidatableResponse getOrders = orderClient.getOrders(customer);
		boolean success = getOrders.extract().path("success");
		assertTrue(success);
	}
}
