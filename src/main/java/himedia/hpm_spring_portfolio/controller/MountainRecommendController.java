/**
 * 파일명: MountainRecommendController.java
 * 설명: 산 추천 데이터를 조회하는 컨트롤러 클래스
 *       추천 산 목록 전체를 클라이언트에게 JSON 형태로 반환
 * 작성자: 서민정
 * 작성일: 2025-04-13
 */

package himedia.hpm_spring_portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import himedia.hpm_spring_portfolio.repository.vo.MountainRecommendVo;
import himedia.hpm_spring_portfolio.service.MountainRecommendService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/mountain-recommends")
public class MountainRecommendController {

    @Autowired
    private MountainRecommendService mountainRecommendService;

    @GetMapping
    public ResponseEntity<List<MountainRecommendVo>> retrieveAllMountainRecommends() {
        List<MountainRecommendVo> mountainRecommends = mountainRecommendService.findAllMountainRecommends();
        return ResponseEntity.ok(mountainRecommends);
    }
}