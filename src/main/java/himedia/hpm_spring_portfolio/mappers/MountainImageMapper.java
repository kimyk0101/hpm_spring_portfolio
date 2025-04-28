package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.MountainImageVo;

public interface MountainImageMapper {

//	<select id="findRepresentativeImage" parameterType="long" resultType="MountainImageVo"> // 산 대표 이미지 조회
    MountainImageVo findRepresentativeImage(Long mountainId);	
	
//	<select id="findByMountainId" parameterType="long" resultType="MountainImageVo"> // 산 Id 기반 모든 이미지 조회 
	List<MountainImageVo> findByMountainId(Long mountainId);
	

}
