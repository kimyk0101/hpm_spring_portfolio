package himedia.hpm_spring_portfolio.mappers;

import himedia.hpm_spring_portfolio.repository.vo.MountainReviewLikeVo;

public interface MountainReviewLikeMapper {

//	<select id="selectLikeCount" parameterType="Long" resultType="int">	// 특정 리뷰의 좋아요 개수 조회
    int selectLikeCount(Long reviewsId);

//	<select id="isLiked" parameterType="mLikeVo" resultMap="mLikeResultMap">   // 해당 사용자가 해당 리뷰를 좋아요 눌렀는지 여부
    int isLiked(MountainReviewLikeVo mLikeVo);

//	<select id="exists" parameterType="mLikeVo" resultType="int">    // 해당 사용자+리뷰 조합이 존재하는지 여부
    int exists(MountainReviewLikeVo mLikeVo);

//	<insert id="insertLike" parameterType="mLikeVo">    // 좋아요 기록 생성
    int insertLike(MountainReviewLikeVo mLikeVo);

//	<update id="updateLike" parameterType="mLikeVo">    // 좋아요 활성화 (is_like = TRUE)
    int updateLike(MountainReviewLikeVo mLikeVo);

//	<update id="cancelLike" parameterType="mLikeVo">    // 좋아요 취소 (is_like = FALSE)
    int cancelLike(MountainReviewLikeVo mLikeVo);
}
