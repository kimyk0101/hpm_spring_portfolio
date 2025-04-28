package himedia.hpm_spring_portfolio.repository.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserLoginData {

	@JsonProperty("user_id")
	private String userId;			//	아이디
	private String password;		//	비밀번호
}
