import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CustomerCredentials {
	private final String email;
	private final String password;

	public static CustomerCredentials from (Customer customer) {
		return new CustomerCredentials(customer.getEmail(), customer.getPassword());
	}
}
