package himedia.hpm_spring_portfolio;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

	  @Override
	   public void addCorsMappings(CorsRegistry registry) {
	       registry.addMapping("/api/**")
	               .allowedOrigins("http://localhost:5173")
	               .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
	               .allowedHeaders("*")
	               .maxAge(3600)
	               .allowCredentials(true);
	    }
}
