package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.CommunityCommentVo;

public interface CommunityCommentMapper {
	
//	<select id="retrieveAllComments" parameterType="Long" resultMap="cCommentResultMap">	//	전체 댓글 조회
	List<CommunityCommentVo> retrieveAllComments(Long communityId);

//	<select id="retrieveCommentById" parameterType="Long" resultType="cCommentVo">	//	특정 댓글 조회
	CommunityCommentVo retrieveCommentById(Long id);
	
//	<select id="findRepliesByCommentId" parameterType="Long" resultMap="cCommentResultMap">	// 특정 댓글의 대댓글 조회 
    List<CommunityCommentVo> findRepliesByCommentId(Long id);
    
//	<select id="findReplyById" parameterType="Long" resultMap="cCommentResultMap">	// 특정 대댓글 조회
    CommunityCommentVo findReplyById(Long id);

//	<select id="retriveMyComments" parameterType="Long" resultType="cCommentResultMap">	// 사용자 댓글 + 대댓글 조회  
    List<CommunityCommentVo> retrieveMyComments(Long id); 
    
//	<insert id="createComment" parameterType="cCommentVo">	// 댓글 생성
	int createComment(CommunityCommentVo comment);

//    <update id="updateComment" parameterType="cCommentVo">	// 댓글 수정
	int updateComment(CommunityCommentVo comment);

//    <delete id="deleteComment" parameterType="Map">	// 댓글 삭제
	int deleteComment(Long id, Long usersId);
	
//	<delete id="deleteCommentsByCommunitiesId" parameterType="Long">	//	communitiesId 기준 댓글 모두 삭제
	int deleteCommentsByCommunitiesId(Long communitiesId);
	
//    <insert id="createReply" parameterType="cCommentVo" useGeneratedKeys="true" keyProperty="id">	//	대댓글 생성
	int createReply(CommunityCommentVo comment);

//    <update id="updateReply" parameterType="cCommentVo">	//	대댓글 수정
	int updateReply(CommunityCommentVo comment);

//    <delete id="deleteReply" parameterType="Map">		//	대댓글 삭제
	int deleteReply(Long id, Long usersId);

}
