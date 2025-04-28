package himedia.hpm_spring_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.MountainImageMapper;
import himedia.hpm_spring_portfolio.repository.vo.MountainImageVo;

@Service
public class MountainImageService {
	
	@Autowired 
	private MountainImageMapper mountainImagemapper;
	
	// 산 대표 이미지 조회 
	public MountainImageVo getRepresentativeImage(Long mountainId) {
	    return mountainImagemapper.findRepresentativeImage(mountainId);
	    }
	
	// 산 Id 기반 모든 이미지 조회 
	public List<MountainImageVo> getImagesByMountainId(Long mountainId) {
        return mountainImagemapper.findByMountainId(mountainId);
    }


}
