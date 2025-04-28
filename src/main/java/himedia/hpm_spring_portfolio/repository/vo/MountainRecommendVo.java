package himedia.hpm_spring_portfolio.repository.vo;

import lombok.Data;

@Data
public class MountainRecommendVo {
    private int id;
    private String location;
    private String difficultyLevel;
    private String courseTime;
    private String name; // mountains 테이블의 name 컬럼
    private String courseName;
    private String selectionReason;
    private String transportationInfo;
    
}