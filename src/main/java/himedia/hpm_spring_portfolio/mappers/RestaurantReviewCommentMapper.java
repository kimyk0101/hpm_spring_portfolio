package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.RestaurantReviewCommentVo;

public interface RestaurantReviewCommentMapper {

//	<select id="retrieveAllComments" parameterType="Long" resultMap="rCommentResultMap">	//	전체 댓글 조회
	List<RestaurantReviewCommentVo> retrieveAllComments(Long rReviewId);

//	<select id="retrieveCommentById" parameterType="Long" resultMap="rCommentResultMap">	//	특정 댓글 조회
	RestaurantReviewCommentVo retrieveCommentById(Long id);
	
//	<select id="findRepliesByCommentId" parameterType="Long" resultMap="rCommentResultMap">	// 특정 댓글의 대댓글 조회 
    List<RestaurantReviewCommentVo> findRepliesByCommentId(Long id);
    
//	<select id="findReplyById" parameterType="Long" resultMap="rCommentResultMap">	// 특정 대댓글 조회
    RestaurantReviewCommentVo findReplyById(Long id);
    
//	<select id="retriveMyComments" parameterType="Long" resultType="rCommentResultMap">	// 사용자 댓글 + 대댓글 조회  
    List<RestaurantReviewCommentVo> retrieveMyComments(Long id);     

//	<insert id="createComment" parameterType="rCommentVo" useGeneratedKeys="true" keyProperty="id">	// 댓글 생성
	int createComment(RestaurantReviewCommentVo comment);

//    <update id="updateComment" parameterType="rCommentVo">	// 댓글 수정
	int updateComment(RestaurantReviewCommentVo comment);

//    <delete id="deleteComment" parameterType="Map">	// 댓글 삭제
	int deleteComment(Long id, Long usersId);
	
//	<delete id="deleteCommentsByRestaurantsId" parameterType="Long">	//	restaurantsId 기준 댓글 모두 삭제
	int deleteCommentsByRestaurantsId(Long restaurantsId);
	
//    <insert id="createReply" parameterType="rCommentVo" useGeneratedKeys="true" keyProperty="id">	//	대댓글 생성
	int createReply(RestaurantReviewCommentVo comment);

//    <update id="updateReply" parameterType="rCommentVo">	//	대댓글 수정
	int updateReply(RestaurantReviewCommentVo comment);

//    <delete id="deleteReply" parameterType="Map">		//	대댓글 삭제
	int deleteReply(Long id, Long usersId);
}
