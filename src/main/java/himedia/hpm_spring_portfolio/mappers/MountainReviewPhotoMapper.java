package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.MountainReviewPhotoVo;

public interface MountainReviewPhotoMapper {
	
//	<insert id="insertPhoto" parameterType="mReviewPhotoVo">
    int insertPhoto(MountainReviewPhotoVo mReviewPhotoVo);
    
//  <select id="selectAllPhotoByReviewsId" parameterType="Long" resultType="mReviewPhotoVo">
    List<MountainReviewPhotoVo> selectAllPhotoByReviewsId(Long reviewsId);
    
//  <delete id="deletePhotoByReviewsId" parameterType="Long">
    int deletePhotoByReviewsId(Long reviewsId);
    
//	<select id="findPhotoById" parameterType="Long" resultType="com.example.model.MountainPhoto">
    MountainReviewPhotoVo findPhotoById(Long photoId);

//	<delete id="deletePhotoById" parameterType="Long">
    int deletePhotoById(Long photoId); 
}