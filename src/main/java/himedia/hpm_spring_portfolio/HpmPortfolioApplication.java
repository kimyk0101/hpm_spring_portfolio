package himedia.hpm_spring_portfolio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = "himedia.hpm_spring_portfolio.mappers")
@ComponentScan(basePackages = "himedia.hpm_spring_portfolio")

public class HpmPortfolioApplication{

	public static void main(String[] args) {
		SpringApplication.run(HpmPortfolioApplication.class, args);
	}

}