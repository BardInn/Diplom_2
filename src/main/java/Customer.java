import com.fasterxml.jackson.annotation.JsonIgnore;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import static net.andreinc.mockneat.unit.user.Emails.emails;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer {

	private String email;
	private String password;
	private String name;
	@JsonIgnore
	private CustomerToken customerToken;


	@Step("Generating random data to create a customer")
	public static Customer getRandom() {
		final String email = emails().val();
		final String password = RandomStringUtils.randomAlphabetic(10);
		final String name = RandomStringUtils.randomAlphabetic(10);

		Allure.addAttachment("Email", email);
		Allure.addAttachment("Password", password);
		Allure.addAttachment("FirstName", name);

		return new Customer(email, password, name,new CustomerToken());
	}
}
