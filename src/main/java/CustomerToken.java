import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerToken {

	@JsonIgnore
	private String accessToken;
	@JsonIgnore
	private String refreshToken;

	public String getToken() {
		return accessToken.split(" ")[1];
	}

}
