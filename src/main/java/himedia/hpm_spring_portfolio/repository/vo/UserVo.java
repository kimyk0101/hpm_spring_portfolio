package himedia.hpm_spring_portfolio.repository.vo;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserVo {

	private Long id;				// 	유저 고유번호
	private String name;			// 	이름
	private String nickname;		// 	닉네임
	
	@JsonProperty("user_id")
	private String userId;			//	아이디
	private String password;		//	비밀번호
	
	// 날짜 선택 시 시간대 오차 발생 떄문에 타임존 명시 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date birth;				//	생년월일
	
	@JsonProperty("phone_number")
	private String phoneNumber;		// 	전화번호
	private String email;			// 	이메일
	private String address;			// 	주소
	
	@JsonProperty("register_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime registerDate;		//	가입 시간
}
