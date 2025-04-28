package himedia.hpm_spring_portfolio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


// ✅ AWS S3와 통신하기 위한 설정 클래스
// S3Client 객체를 스프링 전체에서 재사용할 수 있도록 Bean으로 등록해주는 역할


@Configuration
public class S3Config {
	
    @Bean
    public S3Client s3Client() {
    	  String region = System.getenv("AWS_REGION");  // 환경 변수 읽어오기
          System.out.println("AWS_REGION: " + region);  // 콘솔에 로그 출력
        return S3Client.builder()
        		//	사용할 AWS 리전 
                .region(Region.of(System.getenv("AWS_REGION")))
                
                //	인증 정보(환경변수 참조) 
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        System.getenv("AWS_ACCESS_KEY"),  // 액세스 키
                                        System.getenv("AWS_SECRET_KEY")   // 시크릿 키 
                                )
                        )
                )
                .build(); // S3Client 인스턴스 생성 
    }
}