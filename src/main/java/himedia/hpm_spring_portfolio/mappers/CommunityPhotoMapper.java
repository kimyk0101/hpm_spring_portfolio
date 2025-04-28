package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.CommunityPhotoVo;

public interface CommunityPhotoMapper {
	
//	<insert id="insertPhoto" parameterType="CommunityPhotoVo">
    int insertPhoto(CommunityPhotoVo communityPhotoVo);
    
//  <select id="selectAllPhotoByCommunityId" parameterType="Long" resultType="CommunityPhotoVo">
    List<CommunityPhotoVo> selectAllPhotoByCommunityId(Long communitiesId);
    
//  <delete id="deletePhotoByCommunityId" parameterType="Long">
    int deletePhotoByCommunityId(Long communitiesId);

//	<select id="findPhotoById" parameterType="Long" resultType="com.example.model.CommunityPhoto">
    CommunityPhotoVo findPhotoById(Long photoId);

//	<delete id="deletePhotoById" parameterType="Long">
    int deletePhotoById(Long photoId);    
    
}