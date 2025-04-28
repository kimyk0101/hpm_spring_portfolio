package himedia.hpm_spring_portfolio.repository.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class RestaurantReviewVo {

	private Long id;				// 	맛집 후기 게시글 고유번호
	private String name;			//	맛집 이름
	private String nickname;		//	users 테이블의 nickname과 매핑
	private String location;		//	맛집 위치

	@JsonProperty("mountain_name")	//	mountains 테이블의 name과 매핑
	private String mountainName;	//	
	
	private String rate;			//	별점
	private String content;			// 	게시글 내용
	
	@JsonProperty("update_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateDate;	//	게시글 게시시간
	
	@JsonProperty("users_id")
	private Long usersId;			//	게시글 작성자 아이디
	
	@JsonProperty("mountains_id")
	private Long mountainsId;			//	산 고유 아이디
	
	@JsonProperty("comment_count")
	private Long commentCount;		//	댓글 개수
	
	private String imageUrl;		//	대표 이미지
	
}
