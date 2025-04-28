package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.MountainReviewVo;

public interface MountainReviewMapper {

//	<select id="retrieveAllReviews" resultType="mReviewVo">	//	전체 리뷰 게시글 조회
	List<MountainReviewVo> retrieveAllReviews();
	
//	<select id="retrieveReviewById" parameterType="Long" resultType="mReviewVo">	//	특정 리뷰 게시글 조회
	MountainReviewVo retrieveReviewById(Long id);
	
//	<select id="retrieveMyReviews" parameterType="Long" resultType="mReviewVo">	// 	사용자의 리뷰 게시글 조회
	List<MountainReviewVo> retrieveMyReviews(Long id);
	
//	<insert id="createReview" parameterType="mReviewVo">	//	리뷰 게시글 작성
	int createReview(MountainReviewVo review);
		
//	<update id="updateReview" parameterType="mReviewVo">	//	리뷰 게시글 일부 수정
	int updateReview(MountainReviewVo review);
	
//	<delete id="deleteReview" parameterType="Map">	//	리뷰 게시글 삭제
	int deleteReview(Long id, Long usersId);
}
