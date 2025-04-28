package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.RestaurantReviewVo;

public interface RestaurantReviewMapper {

//	<select id="retrieveAllReviews" resultType="rReviewVo">	//	전체 맛집 리뷰 게시글 조회
	List<RestaurantReviewVo> retrieveAllReviews();
	
//	<select id="retrieveReviewById" parameterType="Long" resultType="rReviewVo">	//	특정 맛집 리뷰 게시글 조회
	RestaurantReviewVo retrieveReviewById(Long id);
	
//	<select id="retrieveMyReviews" parameterType="Long" resultType="rReviewVo">	// 	사용자의 맛집 리뷰 게시글 조회
	List<RestaurantReviewVo> retrieveMyReviews(Long id);

//	<insert id="createReview" parameterType="rReviewVo">	//	맛집 리뷰 게시글 작성
	int createReview(RestaurantReviewVo review);
		
//	<update id="updateReview" parameterType="rReviewVo">	//	맛집 리뷰 게시글 일부 수정
	int updateReview(RestaurantReviewVo review);
	
//	<delete id="deleteReview" parameterType="Map">	//	맛집 리뷰 게시글 삭제
	int deleteReview(Long id, Long usersId);
}
