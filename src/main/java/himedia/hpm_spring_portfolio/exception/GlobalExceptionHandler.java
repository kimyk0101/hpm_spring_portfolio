/**
 * 파일명: GlobalExceptionHandler.java
 * 설명: 애플리케이션 내에서 발생하는 예외를 전역적으로 처리하는 예외 처리 클래스
 *        	- 특정 예외가 발생하면 적절한 HTTP 상태 코드와 메시지를 반환하여 클라이언트에게 전달
 * 작성자: 김연경
 * 작성일: 2025-03-27
 */

package himedia.hpm_spring_portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//UserNotFoundException 클래스에서 예외 처리를 하는데에도 불구하고 전역 예외 처리에서 또 다루는 이유(응답)
//예외가 발생했을 때 어떤 HTTP 응답 코드와 메시지를 클라이언트에게 전달할지를 지정해주기 위해서
//-> UserNotFoundException이 발생하면 404 상태 코드와 함께 "User not found" 메시지 보냄
//모든 예외가 어떻게 처리될지 정의 -> 예외가 발생하면 어떤 방식으로 "응답"할지 관리

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// 사용자 정의 예외를 처리하는 메소드
		@ExceptionHandler(UserNotFoundException.class) // UserNotFoundException이 발생하면 이 메소드가 호출됨
		public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
			// 예외 발생 시 클라이언트에게 반환할 응답
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}

		// 기타 예외를 처리하는 메소드 (RuntimeException)
		@ExceptionHandler(RuntimeException.class)
		public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
			// 내부 서버 오류 (500) 상태 코드와 예외 메시지 반환
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}

		// 그 외 모든 예외 처리 (예: NullPointerException)
		@ExceptionHandler(Exception.class)
		public ResponseEntity<String> handleGenericException(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + ex.getMessage());
		}

}
