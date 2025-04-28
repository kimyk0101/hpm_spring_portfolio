package himedia.hpm_spring_portfolio.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/sendbird") // 이 컨트롤러의 기본 URL 경로
public class SendbirdController {

    // ★ Sendbird API 통신에 필요한 고정 토큰 및 앱 ID
    private static final String API_TOKEN = "YOUR_API_TOKEN"; // Sendbird에서 발급한 서버 API 토큰
    private static final String APP_ID = "YOUR_APP_ID";       // Sendbird 앱 ID (프로젝트 단위로 발급)

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody Map<String, String> user) {
        // 클라이언트에서 전달된 사용자 정보 추출
        String userId = user.get("userId");     // 필수: 사용자 고유 ID (Sendbird에서 유일해야 함)
        String nickname = user.get("nickname"); // 필수: 사용자 닉네임

        // Sendbird API 요청에 필요한 HTTP 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Token", API_TOKEN);                     // 인증용 서버 토큰
        headers.setContentType(MediaType.APPLICATION_JSON);      // 요청 데이터는 JSON 형식

        // 요청 본문 구성
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);     // Sendbird에 맞는 키 이름으로 사용자 ID 전달
        body.put("nickname", nickname);  // 닉네임도 함께 전달

        // 위에서 만든 headers + body를 HttpEntity로 묶어 하나의 요청 객체 생성
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // Sendbird 사용자 생성 API의 전체 URL
        String url = "https://api-" + APP_ID + ".sendbird.com/v3/users";

        try {
            // Spring에서 외부 API 호출을 도와주는 유틸리티 객체
            RestTemplate restTemplate = new RestTemplate();

            // POST 요청 전송. 반환 값은 사용하지 않으므로 무시하고 성공 응답만 보냄
            restTemplate.postForEntity(url, request, String.class);

            // 사용자 생성 성공
            return ResponseEntity.ok("User created");

        } catch (HttpClientErrorException e) {
            // 클라이언트 오류(4xx) 예외 발생 시
            if (e.getStatusCode().value() == 400) {
                // 400 에러는 "이미 존재하는 사용자"일 가능성 있음 → 성공처럼 처리
                return ResponseEntity.ok("User may already exist (ignored)");
            }

            // 다른 클라이언트 오류는 그대로 반환
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());

        } catch (Exception e) {
            // 그 외 모든 예외 (예: 서버 연결 불가, JSON 파싱 오류 등)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }
}
