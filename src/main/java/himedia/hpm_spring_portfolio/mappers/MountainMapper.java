package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.MountainVo;


public interface MountainMapper {

    List<MountainVo> selectAllMountains();

    MountainVo selectMountainById(Long id);
        
    MountainVo selectMountainByName(String name);
    
    List<MountainVo> searchMountains(String keyword);
    
}
