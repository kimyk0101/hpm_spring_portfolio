package himedia.hpm_spring_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.MountainMapper;
import himedia.hpm_spring_portfolio.repository.vo.MountainVo;

@Service
public class MountainService {

    @Autowired
    private MountainMapper mountainMapper;

    // 모든 산 정보 조회
    public List<MountainVo> retrieveAllMountains() {
        return mountainMapper.selectAllMountains();
    }

    // 특정 산 정보 조회 (id)
    public MountainVo retrieveMountainById(Long id) {
        return mountainMapper.selectMountainById(id);
    }
    
    // 특정 산 정보 조회 (name)
    public String retrieveMountainByName(String name) {
    	MountainVo mountain = mountainMapper.selectMountainByName(name);
        if (mountain != null) {
            return mountain.getId().toString(); // 산 ID를 문자열로 반환
        } else {
            return null; // 산 코드가 없을 경우 null 반환
        }
    }
    
    // 검색 기능
    public List<MountainVo> searchMountains(String keyword) {
        return mountainMapper.searchMountains(keyword);
    }

}
