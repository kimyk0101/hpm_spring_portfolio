package himedia.hpm_spring_portfolio.repository.vo;

import java.util.Date;

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
public class UserPhotoVo {

    private Long id;				//	userPhoto 고유번호
    
    @JsonProperty("users_id")
    private Long usersId;		//	유저 ID
    
    @JsonProperty("file_name")
    private String fileName;	//	파일 이름
    
    @JsonProperty("file_path")
    private String filePath;	//	파일 경로
    
    @JsonProperty("update_date")
    private Date updateDate;	//	갱신 시간
}
