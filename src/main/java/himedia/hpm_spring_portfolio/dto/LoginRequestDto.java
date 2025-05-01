package himedia.hpm_spring_portfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

	@JsonProperty("user_id")
	private String userId;			//	아이디
	private String password;		//	비밀번호
}
