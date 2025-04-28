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
public class MountainCourseVo {

	private Long id;
    private String courseName;
    private String difficultyLevel;
    private String courseTime;
    private String courseLength;
    private Long mountainsId;
	
}
