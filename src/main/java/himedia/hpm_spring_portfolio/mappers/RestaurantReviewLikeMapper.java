package himedia.hpm_spring_portfolio.mappers;

import himedia.hpm_spring_portfolio.repository.vo.RestaurantReviewLikeVo;

public interface RestaurantReviewLikeMapper {

//	<select id="selectLikeCount" parameterType="Long" resultType="int">	// 특정 리뷰의 좋아요 개수 조회
    int selectLikeCount(Long restaurnatsId);

//	<select id="isLiked" parameterType="rLikeVo" resultMap="mLikeResultMap">   // 해당 사용자가 해당 리뷰를 좋아요 눌렀는지 여부
    int isLiked(RestaurantReviewLikeVo rLikeVo);

//	<select id="exists" parameterType="rLikeVo" resultType="int">    // 해당 사용자+리뷰 조합이 존재하는지 여부
    int exists(RestaurantReviewLikeVo rLikeVo);

//	<insert id="insertLike" parameterType="rLikeVo">    // 좋아요 기록 생성
    int insertLike(RestaurantReviewLikeVo mLirLikeVokeVo);

//	<update id="updateLike" parameterType="rLikeVo">    // 좋아요 활성화 (is_like = TRUE)
    int updateLike(RestaurantReviewLikeVo rLikeVo);

//	<update id="cancelLike" parameterType="rLikeVo">    // 좋아요 취소 (is_like = FALSE)
    int cancelLike(RestaurantReviewLikeVo rLikeVo);
}
