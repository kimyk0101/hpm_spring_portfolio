package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.MountainReviewCommentVo;

public interface MountainReviewCommentMapper {

//	<select id="retrieveAllComments" parameterType="Long" resultMap="mCommentResultMap">	//	전체 댓글 조회
	List<MountainReviewCommentVo> retrieveAllComments(Long mReviewId);

//	<select id="retrieveCommentById" parameterType="Long" resultMap="mCommentResultMap">	//	특정 댓글 조회
	MountainReviewCommentVo retrieveCommentById(Long id);
	
//	<select id="findRepliesByCommentId" parameterType="Long" resultMap="mCommentResultMap">	// 특정 댓글의 대댓글 조회 
    List<MountainReviewCommentVo> findRepliesByCommentId(Long id);
    
//	<select id="findReplyById" parameterType="Long" resultMap="mCommentResultMap">	// 특정 대댓글 조회
    MountainReviewCommentVo findReplyById(Long id);

//	<select id="retriveMyComments" parameterType="Long" resultType="mCommentResultMap">	// 사용자 댓글 + 대댓글 조회  
    List<MountainReviewCommentVo> retrieveMyComments(Long id); 
    
//	<insert id="createComment" parameterType="mCommentVo" useGeneratedKeys="true" keyProperty="id">	// 댓글 생성
	int createComment(MountainReviewCommentVo comment);

//    <update id="updateComment" parameterType="mCommentVo">	// 댓글 수정
	int updateComment(MountainReviewCommentVo comment);

//    <delete id="deleteComment" parameterType="Map">	// 댓글 삭제
	int deleteComment(Long id, Long usersId);
	
//	<delete id="deleteCommentsByReviewsId" parameterType="Long">	//	reviewsId 기준 댓글 모두 삭제
	int deleteCommentsByReviewsId(Long reviewsId);
	
//    <insert id="createReply" parameterType="mCommentVo" useGeneratedKeys="true" keyProperty="id">	//	대댓글 생성
	int createReply(MountainReviewCommentVo comment);

//    <update id="updateReply" parameterType="mCommentVo">	//	대댓글 수정
	int updateReply(MountainReviewCommentVo comment);

//    <delete id="deleteReply" parameterType="Map">		//	대댓글 삭제
	int deleteReply(Long id, Long usersId);

}
