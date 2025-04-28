package himedia.hpm_spring_portfolio.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.exception.UserNotFoundException;
import himedia.hpm_spring_portfolio.mappers.UserMapper;
import himedia.hpm_spring_portfolio.repository.vo.UserLoginData;
import himedia.hpm_spring_portfolio.repository.vo.UserVo;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	// 전체 유저 조회
	public List<UserVo> retrieveAllUsers() {
		return userMapper.retrieveAllUsers();
	}

	// 로그인
	public UserVo authenticateUser(UserLoginData loginData) {
		UserVo user = userMapper.authenticateUser(loginData.getUserId(), loginData.getPassword());

		return user;
	}

	// 회원가입
	public UserVo registerUser(UserVo user) {
		userMapper.registerUser(user);

		Long id = user.getId();

		return userMapper.retrieveUserById(id);
	}

	// 유저 정보 수정
	public UserVo updateUserFields(Long id, Map<String, Object> updates) {
		// 1️. 데이터베이스에서 해당 id를 가진 유저를 찾음
		UserVo user = userMapper.retrieveUserById(id);

		// 유저가 없다면 예외 처리
		if (user == null) {
			throw new UserNotFoundException("User with id " + id + " not found");
		}

		// 2️. updates에 있는 값만 수정
		updates.forEach((key, value) -> {
			if (value == null) {
				// null 값이 들어올 경우, 필수 필드는 예외 처리
				if ("name".equals(key) || "nickname".equals(key) || "userId".equals(key) || "password".equals(key)) {
					throw new IllegalArgumentException(key + " cannot be null");
				}
				// 선택적 필드들에 대해서는 null 값이 들어오면 빈 문자열("")로 처리
				value = "";
			}

			// 필드 값 수정
			switch (key) {
			case "name":
				user.setName((String) value);
				break;
			case "nickname":
				user.setNickname((String) value);
				break;
			case "user_id":
				user.setUserId((String) value);
				break;
			case "password":
				user.setPassword((String) value);
				break;
			case "birth":
				try {
					// 리액트에서 생년월일 문자열 받아오기
					String birthStr = (String) value;
					// 날짜 형식 지정자 생성
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// 문자열을 Date 객체로 변환
					Date birthDate = sdf.parse(birthStr);
					user.setBirth(birthDate);
				} catch (ParseException e) {
					// 날짜 포맷이 잘못된 경우 예외 처리
					throw new IllegalArgumentException("Invalid date format for birth", e);
				}
				break;
			case "phone_number":
				user.setPhoneNumber((String) value);
				break;
			case "email":
				user.setEmail((String) value);
				break;
			case "address":
				user.setAddress((String) value);
				break;
			case "register_date":
				user.setRegisterDate((LocalDateTime) value);
				break;
			case "update_date":
			    // 무시하거나 로그로 남기기만 해도 됨
			    break;
			default:
				throw new IllegalArgumentException("Invalid field name: " + key);
			}

		});
		// 3️. 수정된 데이터를 저장
		userMapper.updateUserFields(user);

		// 4️. 업데이트된 유저 정보 반환
		return user;
	}

	// 유저 삭제
	public int deleteUser(Long id) {
		return userMapper.deleteUser(id);
	}

	// 아이디 중복 체크 메서드
	public boolean checkUserIdInDatabase(String userId) {
		// MyBatis에서 countByUserId를 호출하여 아이디 존재 여부를 확인
		return userMapper.countByUserId(userId) == 0; // 0이면 사용 가능, 아니면 중복
	}

	// 닉네임 중복 체크 메서드
	public boolean checkNicknameInDatabase(String nickname) {
		return userMapper.countByNickname(nickname) == 0; // 0이면 사용 가능, 아니면 중복
	}
}
