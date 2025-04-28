package himedia.hpm_spring_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.MountainRecommendMapper;
import himedia.hpm_spring_portfolio.repository.vo.MountainRecommendVo;

@Service
public class MountainRecommendService {

    @Autowired
    private MountainRecommendMapper mountainRecommendMapper;

    public List<MountainRecommendVo> findAllMountainRecommends() {
        return mountainRecommendMapper.findAllMountainRecommends();
    }
}