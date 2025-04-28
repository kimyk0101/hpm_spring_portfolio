package himedia.hpm_spring_portfolio.repository.vo;

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
public class RestaurantReviewLikeVo {
	private Long id;

	@JsonProperty("users_id")
	private Long usersId;
	
	@JsonProperty("restaurant_reviews_id")
	private Long restaurantReviewsId;
	
	@JsonProperty("is_like")
	private boolean isLike;
}
