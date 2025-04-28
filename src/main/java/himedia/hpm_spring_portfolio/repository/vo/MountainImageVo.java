package himedia.hpm_spring_portfolio.repository.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MountainImageVo {
	
	private Long id;					// 이미지 고유 번호 
    private String imageUrl;			// 이미지 url(저장 경로)  
    private Long mountainsId;			// 산 Id 
    private Boolean isRepresentative;	// 대표 이미지(true/false) 

}
