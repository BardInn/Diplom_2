import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static net.andreinc.mockneat.unit.user.Emails.emails;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CustomerCreateTest {
	private CustomerClient customerClient;
	int statusCode;

	@Before
	public void setUp() {
		customerClient = new CustomerClient();
	}


	@Test
	@DisplayName("Create new customer")
	@Description("Create new customer and verify success creations")
	public void customerCreateRegisterTest() {
		Customer customer = Customer.getRandom();
		ValidatableResponse isCreated = customerClient.create(customer);
		customer.getCustomerToken().setAccessToken(isCreated.extract().path("accessToken"));
		customer.getCustomerToken().setRefreshToken(isCreated.extract().path("refreshToken"));
		statusCode = isCreated.extract().statusCode();
		boolean successMessage = isCreated.extract().path("success");

		assertTrue(successMessage);
		assertThat("Customer can register", statusCode, equalTo(200));
		assertNotNull(customer.getCustomerToken().getAccessToken());
		assertNotNull(customer.getCustomerToken().getRefreshToken());

		ValidatableResponse tearDown = customerClient.delete(customer);
	}

	@Test
	@DisplayName("The login is already in use")
	@Description("Create new customer and verify success creations")
	public void doubleCustomerRegisterTest() {
		Customer customer = Customer.getRandom();
		ValidatableResponse isCreated = customerClient.create(customer);
		ValidatableResponse doubleCreate = customerClient.create(customer);
		int statusCodeFirst = isCreated.extract().statusCode();
		int statusCodeDouble = doubleCreate.extract().statusCode();
		boolean successMessageFirst = isCreated.extract().path("success");
		boolean successMessageDouble = doubleCreate.extract().path("success");

		assertTrue(successMessageFirst);
		assertFalse(successMessageDouble);
		assertThat("First customer created", statusCodeFirst, equalTo(200));
		assertThat("Double customer cannot registered", statusCodeDouble, equalTo(403));

		customer.getCustomerToken().setAccessToken(isCreated.extract().path("accessToken"));
		ValidatableResponse tearDown = customerClient.delete(customer);
	}

	@Test
	@DisplayName("Email is required")
	@Description("Try to create new customer without email field")
	public void requestWithoutEmailTest() {
		Customer customer = Customer.builder()
				.password(RandomStringUtils.randomAlphabetic(10))
				.name(RandomStringUtils.randomAlphabetic(10))
				.build();

		String faultMessage = customerClient.createCustomerWithMissingField(customer);
		assertEquals("Email, password and name are required fields", faultMessage);
	}

	@Test
	@DisplayName("Password is required")
	@Description("Try to create new customer without password field")
	public void requestWithoutPasswordTest() {
		Customer customer = Customer.builder()
				.email(emails().val())
				.name(RandomStringUtils.randomAlphabetic(10))
				.build();

		String faultMessage = customerClient.createCustomerWithMissingField(customer);
		assertEquals("Email, password and name are required fields", faultMessage);
	}

	@Test
	@DisplayName("Name is required")
	@Description("Try to create new customer without name field")
	public void requestWithoutNameTest() {
		Customer customer = Customer.builder()
				.email(emails().val())
				.password(RandomStringUtils.randomAlphabetic(10))
				.build();

		String faultMessage = customerClient.createCustomerWithMissingField(customer);
		assertEquals("Email, password and name are required fields", faultMessage);
	}
}
