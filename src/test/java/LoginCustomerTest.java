import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginCustomerTest {
	private CustomerClient customerClient;
	private Customer customer;

	@Before
	public void setUp() {
		customerClient = new CustomerClient();
		customer = Customer.getRandom();
		ValidatableResponse isCreated = customerClient.create(customer);
		customer.getCustomerToken().setAccessToken(isCreated.extract().path("accessToken"));
	}

	@After
	public void tearDown() {
		ValidatableResponse tearDown =  customerClient.delete(customer);
	}  

	@Test
	@DisplayName("Login customer")
	@Description("Login customer with valid data")
	public void courierCanLoginWithValidCredentialsTest() {
		CustomerCredentials credentials = new CustomerCredentials(customer.getEmail(), customer.getPassword());
		ValidatableResponse loginResponse = customerClient.login(credentials);
		int statusCode = loginResponse.extract().statusCode();
		boolean successMessage = loginResponse.extract().path("success");

		assertThat("Customer can login", statusCode, equalTo(SC_OK));
		assertTrue(successMessage);
	}

	@Test
	@DisplayName("Fail with wrong login")
	@Description("Try to login with non-registered login")
	public void courierLoginWithWrongLoginTest() {
		ValidatableResponse loginResponse = customerClient.login(new CustomerCredentials("ASDFDF@mail.ru", "hjcbdscghjsdhcgvs"));
		int statusCode = loginResponse.extract().statusCode();
		boolean successMessage = loginResponse.extract().path("success");

		assertThat("email or password are incorrect", statusCode, equalTo(401));
		assertFalse(successMessage);
	}
	
}
