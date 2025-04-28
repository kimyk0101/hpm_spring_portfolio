package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.MountainCourseVo;

public interface MountainCourseMapper {

	List<MountainCourseVo> findByMountainId(Long mountainId);
}
