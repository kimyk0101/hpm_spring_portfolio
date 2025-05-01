package himedia.hpm_spring_portfolio.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.dto.LoginRequestDto;
import himedia.hpm_spring_portfolio.exception.UserNotFoundException;
import himedia.hpm_spring_portfolio.mappers.UserMapper;
import himedia.hpm_spring_portfolio.repository.vo.UserVo;

@Service
public class UserService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}

	// 전체 유저 조회
	public List<UserVo> retrieveAllUsers() {
		return userMapper.retrieveAllUsers();
	}

	// 로그인
	public UserVo authenticateUser(LoginRequestDto loginData) {
		// 1. user_id로만 사용자 가져오기
		UserVo user = userMapper.findByUserId(loginData.getUserId());

		// 2. 가져온 user와 비밀번호 비교
		if (user != null && passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
			return user; // 로그인 성공
		} else {
			return null; // 로그인 실패
		}
	}

	// 회원가입
	public UserVo registerUser(UserVo user) {
	    // 1. 비밀번호 암호화
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);

	    // ✅ 2. 현재 시각을 가입일로 설정
	    user.setRegisterDate(LocalDateTime.now());

	    // 3. 저장
	    userMapper.registerUser(user);

	    // 4. 다시 조회해서 리턴
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
			case "birth":
				if (value != null && !value.toString().isBlank()) {
					user.setBirth(LocalDate.parse(value.toString()));
				} else {
					user.setBirth(null);
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
			    // 이미 setUpdateDate()로 설정할 것이므로 여기선 무시
			    break;
			default:
				throw new IllegalArgumentException("Invalid field name: " + key);
			}

		});
		
		user.setUpdateDate(LocalDateTime.now());

		// 3️. 수정된 데이터를 저장
		userMapper.updateUserFields(user);

		// 4️. 업데이트된 유저 정보 반환
		return user;
	}

	// 유저 정보 수정: 비밀번호 수정
	public int updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
		UserVo user = userMapper.retrieveUserById(id);

		if (user == null) {
			return 0; // 사용자 없음
		}

		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			return -1; // 현재 비번 불일치
		}

		if (!newPassword.equals(confirmPassword)) {
			return -2; // 새 비번 불일치
		}

		if (passwordEncoder.matches(newPassword, user.getPassword())) {
			return -3; // 새 비번 == 현재 비번 (변경 불가)
		}

		String encodedNewPassword = passwordEncoder.encode(newPassword);
		userMapper.updatePassword(id, encodedNewPassword);

		return 1; // 성공
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

	// 이메일 중복 체크 메서드
	public boolean checkEmailInDatabase(String email) {
		return userMapper.countByEmail(email) == 0; // 0이면 사용 가능, 아니면 중복
	}
}
