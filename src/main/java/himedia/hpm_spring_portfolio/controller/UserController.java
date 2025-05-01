/**
 * 기능명: User
 * 파일명: UserController.java
 * 작성자: 김연경
 * 설명: 사용자 관련 API 요청을 처리하는 컨트롤러 클래스
 *         - 사용자 조회, 로그인, 로그아웃, 회원 가입, 정보 수정, 삭제 등의 기능 제공
 *         - 로그인 시 세션을 관리
 *         - 닉네임, 아이디 중복 체크 기능
 * 작성일: 2025-03-20
 * 수정자: 김경민
 * 수정내용: 정보 수정 시 업데이트 날짜 갱신 기능 추가
 * 수정일: 2025-03-21
 */

package himedia.hpm_spring_portfolio.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import himedia.hpm_spring_portfolio.dto.LoginRequestDto;
import himedia.hpm_spring_portfolio.dto.PasswordUpdateRequestDto;
import himedia.hpm_spring_portfolio.repository.vo.UserVo;
import himedia.hpm_spring_portfolio.service.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	// GET : /api/users -> 모든 유저 조회
	@GetMapping
	public ResponseEntity<List<UserVo>> retrieveAllUsers() {
		List<UserVo> users = userService.retrieveAllUsers();
		return ResponseEntity.ok(users);
	}

	// POST : /api/users/login -> 로그인 (인증)
	@PostMapping("/login")
	public ResponseEntity<UserVo> authenticateUser(@RequestBody LoginRequestDto loginData, HttpSession session) {
		if (session != null && session.getAttribute("loginUser") != null) {
			UserVo loginUser = (UserVo) session.getAttribute("loginUser");
			return ResponseEntity.ok(loginUser);
		}

		if (loginData.getUserId().isEmpty() || loginData.getPassword().isEmpty()) {
			System.err.println("no user_id or password");
			return ResponseEntity.ofNullable(null);
		}

		UserVo loginUser = userService.authenticateUser(loginData);

		if (loginUser != null) {
			loginUser.setPassword(""); // 비밀번호 숨김 처리
			session.setAttribute("loginUser", loginUser);
			System.out.println("user_id: " + loginUser.getUserId());
			System.out.println("nickname: " + loginUser.getNickname());
		
			return ResponseEntity.ok(loginUser);
		} else {
			return ResponseEntity.ofNullable(null);
		}
	}

	// POST : /api/users/logout -> 로그아웃 (세션 무효화)
	@PostMapping("/logout")
	public void invalidateSession(HttpSession session) {
		session.removeAttribute("loginUser");
		session.invalidate();
	}

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	// POST : /api/users -> 새로운 유저 생성
	@PostMapping
	public ResponseEntity<UserVo> registerUser(@RequestBody UserVo user) {
		if (user.getPhoneNumber() != null && user.getPhoneNumber().trim().isEmpty()) {
			user.setPhoneNumber(null);
		}

		UserVo savedUser = userService.registerUser(user);

		return ResponseEntity.ok(savedUser);
	}

	// PATCH : /api/users/{id} -> 기존 유저 정보 수정
	@PatchMapping("/{id}")
	public ResponseEntity<UserVo> updateUserFields(@RequestBody Map<String, Object> updates, @PathVariable Long id) {
		// 업데이트 날짜를 DB에 저장하기 위해 백엔드에서 직접 추가
		updates.put("update_date", java.sql.Date.valueOf(LocalDate.now()));
		UserVo updatedUser = userService.updateUserFields(id, updates);
		return ResponseEntity.ok(updatedUser);
	}

	// PATCH : /api/users/{id}/password -> 기존 유저 정보 수정: 비밀번호 수정
	@PatchMapping("/{id}/password")
	public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateRequestDto passwords) {
		String currentPassword = passwords.getCurrentPassword();
		String newPassword = passwords.getNewPassword();
		String confirmPassword = passwords.getConfirmPassword();

		int result = userService.updatePassword(id, currentPassword, newPassword, confirmPassword);

		if (result == 1) {
		    return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
		    
		} else if (result == -1) {
		    return ResponseEntity.badRequest().body("현재 비밀번호가 틀립니다.");
		    
		} else if (result == -2) {
		    return ResponseEntity.badRequest().body("새 비밀번호와 확인이 일치하지 않습니다.");
		    
		} else if (result == -3) {
		    return ResponseEntity.badRequest().body("현재 비밀번호와 새 비밀번호가 동일합니다. 다른 비밀번호를 입력해주세요.");
		    
		} else {
		    return ResponseEntity.badRequest().body("비밀번호 변경 실패");
		}

	}

	// DELETE : /api/users/{id} -> 기존 유저 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok().<Void>build();
	}

	// GET : /api/users/session -> 로그인 상태 확인
	@GetMapping("/session")
	public ResponseEntity<UserVo> retrieveSessionUser(HttpSession session) {
		UserVo loginUser = (UserVo) session.getAttribute("loginUser");

		if (loginUser != null) {
			return ResponseEntity.ok(loginUser);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	// GET : /api/users/check-user-id/?userId=입력된 값 -> 아이디 중복 체크
	@GetMapping("/check-user-id")
	public ResponseEntity<Boolean> checkUserId(@RequestParam("userId") String userId) {
		// 클라이언트에서 보낸 userId를 처리
		boolean isTaken = userService.checkUserIdInDatabase(userId);
		return ResponseEntity.ok(isTaken);
	}

	// GET : /api/users/check-user-nickname/?nickname=입력된 값 -> 닉네임 중복 체크
	@GetMapping("/check-user-nickname")
	public ResponseEntity<Boolean> checkNickname(@RequestParam("nickname") String nickname) {
		// 클라이언트에서 보낸 userId를 처리
		boolean isTaken = userService.checkNicknameInDatabase(nickname);
		return ResponseEntity.ok(isTaken);
	}

	// GET : /api/users/check-user-email/?email=입력된 값 -> 이메일 중복 체크
	@GetMapping("/check-user-email")
	public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email) {
		// 클라이언트에서 보낸 userId를 처리
		boolean isTaken = userService.checkEmailInDatabase(email);
		return ResponseEntity.ok(isTaken);
	}

}
