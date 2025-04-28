package himedia.hpm_spring_portfolio.repository.vo;

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
public class MountainVo {

	private Long id;					// 	산 고유번호
	private String name;				//	산 이름	
	private String location;			//	산 위치
	private String longitude;			//	경도
	private String latitude;			// 	위도
	private String height;				//	고도
	private String selection_reason;	//	100대명산 선정이유
	private String transportation_info;	//	대중교통 정보
	
}
