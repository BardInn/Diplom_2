import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.andreinc.mockneat.unit.user.Emails.emails;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CustomerDataChangeTest {
	private CustomerClient customerClient;
	private Customer customer;
	private CustomerCredentials credentials;

	@Before
	public void setUp() {
		customerClient = new CustomerClient();
		customer = Customer.getRandom();
		customerClient.create(customer);
		ValidatableResponse loginResponse = customerClient.login(new CustomerCredentials(customer.getEmail(), customer.getPassword()));
		customer.getCustomerToken().setAccessToken(loginResponse.extract().path("accessToken"));
	}

	@After
	public void tearDown() {
		ValidatableResponse tearDown = customerClient.delete(customer);
		assertThat("Customer deleted", tearDown.extract().statusCode(), equalTo(202));
	}

	@Test
	@DisplayName("Change customer field email")
	@Description("Try to change authorized customer email")
	public void changeEmailWithAuthorizationTest() {
		customer.setEmail(emails().val());
		ValidatableResponse changeCustomerData = customerClient.changeCustomerData(customer);
		boolean message = changeCustomerData.extract().path("success");
		assertTrue(message);
	}

	@Test
	@DisplayName("Change customer field password")
	@Description("Try to change authorized customer password")
	public void changePasswordWithAuthorizationTest() {
		customer.setPassword(RandomStringUtils.randomAlphabetic(10));
		ValidatableResponse changeCustomerData = customerClient.changeCustomerData(customer);
		boolean message = changeCustomerData.extract().path("success");
		assertTrue(message);
	}

	@Test
	@DisplayName("Change customer field name")
	@Description("Try to change authorized customer name")
	public void changeNameWithAuthorizationTest() {
		customer.setName(RandomStringUtils.randomAlphabetic(10));
		ValidatableResponse changeCustomerData = customerClient.changeCustomerData(customer);
		boolean message = changeCustomerData.extract().path("success");
		assertTrue(message);
	}

	@Test
	@DisplayName("Change customer field email")
	@Description("Try to change non authorized customer email")
	public void changeEmailWithOutAuthorizationTest() {
		customer.setEmail(emails().val());
		ValidatableResponse changeCustomerDataWithOutAuthorization = customerClient.changeCustomerDataWithOutAuthorization(customer);
		boolean success = changeCustomerDataWithOutAuthorization.extract().path("success");
		String message = changeCustomerDataWithOutAuthorization.extract().path("message");
		assertFalse(success);
		assertEquals("You should be authorised", message);
	}

	@Test
	@DisplayName("Change customer field password")
	@Description("Try to change non authorized customer password")
	public void changePasswordWithOutAuthorizationTest() {
		customer.setPassword(RandomStringUtils.randomAlphabetic(10));
		ValidatableResponse changeCustomerDataWithOutAuthorization = customerClient.changeCustomerDataWithOutAuthorization(customer);
		boolean success = changeCustomerDataWithOutAuthorization.extract().path("success");
		String message = changeCustomerDataWithOutAuthorization.extract().path("message");
		assertFalse(success);
		assertEquals("You should be authorised", message);
	}

	@Test
	@DisplayName("Change customer field name")
	@Description("Try to change non authorized customer name")
	public void changeNamedWithOutAuthorizationTest() {
		customer.setName(RandomStringUtils.randomAlphabetic(10));
		ValidatableResponse changeCustomerDataWithOutAuthorization = customerClient.changeCustomerDataWithOutAuthorization(customer);
		boolean success = changeCustomerDataWithOutAuthorization.extract().path("success");
		String message = changeCustomerDataWithOutAuthorization.extract().path("message");
		assertFalse(success);
		assertEquals("You should be authorised", message);
	}
}
