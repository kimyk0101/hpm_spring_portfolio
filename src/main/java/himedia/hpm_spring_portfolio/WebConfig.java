//	WebConfig.java 
//	리액트에서 요청한 이미지 경로를
//	서버에 실제 존재하는 파일 경로로 연결해주는 역할

package himedia.hpm_spring_portfolio;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // 사용자가 요청할 경로 (URL) 
                .addResourceLocations("file:C:/home/user/uploads/"); // 실제 파일이 있는 폴더 위치
    }
}
