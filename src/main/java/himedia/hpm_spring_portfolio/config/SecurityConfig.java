package himedia.hpm_spring_portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	spring-boot-starter-security를 프로젝트에 추가하면 spring-boot는 자동으로 모든 api를 보호하려고 함
//	그래서 임시로 생성된 관리자 비밀번호(UUID 형태)를 출력해주고, 브라우저에서 API를 호출하거나 페이지를 접근하려고 하면, 
// 	로그인 화면(Username/Password 입력창)이 뜨는 게 기본 동작 -> 프로젝트의 방향과 맞지 않으니 기본 설정을 꺼야 함 
	
	// 기본 시큐리티 설정 끄기 추가
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // CSRF 비활성화
            .authorizeHttpRequests()
            .anyRequest().permitAll(); // 모든 요청 허용
        return http.build();
    }
}
