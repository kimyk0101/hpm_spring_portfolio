package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import himedia.hpm_spring_portfolio.repository.vo.CommunityVo;

public interface CommunityMapper {

//	<select id="retrieveAllCommunities" resultType="CommunityVo">	//	전체 게시글 조회
	List<CommunityVo> retrieveAllCommunities();
	
//	<select id="retrieveCommunityById" parameterType="Long" resultType="CommunityVo">	//	특정 게시글 조회
	CommunityVo retrieveCommunityById(Long id);
	
//	<select id="retrieveMyCommunities" parameterType="Long" resultType="CommunityVo">	// 	사용자의 게시글 조회
	List<CommunityVo> retrieveMyCommunities(Long id);

//  <select id="retriveCommunitiesByKeyword" resultType="CommunityVo"	//	 키워드 기반 게시글 검색
	List<CommunityVo> retrieveCommunitiesByKeyword(@Param("pattern") String pattern);

//	<insert id="createCommunity" parameterType="CommunityVo">	//	게시글 작성
	int createCommunity(CommunityVo community);
		
//	<update id="updateCommunity" parameterType="CommunityVo">	//	게시글 일부 수정
	int updateCommunity(CommunityVo community);
		
//	<delete id="deleteCommunity" parameterType="Map">	//	게시글 삭제
	int deleteCommunity(Long id, Long usersId);
	
//	<update id="incrementViews" parameterType="Long">	 //	조회수 증가 메서드
    void incrementViews(Long id);	
}
