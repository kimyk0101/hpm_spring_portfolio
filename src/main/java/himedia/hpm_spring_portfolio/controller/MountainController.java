/**
 * 파일명: MountainController.java, MountainCourseController.java
 * 작성자: 김승룡
 * 작성일: 2025-04-07 ~ 04-09
 * 
 * 설명:
 * - 산 정보 조회 및 검색 관련 API 요청을 처리하는 컨트롤러 클래스들
 * 
 * MountainController.java
 * - 전체 산 목록 조회, 특정 산 ID로 산 조회, 산 이름으로 ID 조회 기능 제공
 * - 키워드 기반 산 검색 기능 제공
 * 
 * MountainCourseController.java
 * - 특정 산에 속한 등산 코스 목록을 산 ID를 통해 조회하는 기능 제공
 */

package himedia.hpm_spring_portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import himedia.hpm_spring_portfolio.repository.vo.MountainVo;
import himedia.hpm_spring_portfolio.service.MountainService;

@RestController
@RequestMapping("/api/mountains")
public class MountainController {

    @Autowired
    private MountainService mountainService;

    @GetMapping
    public List<MountainVo> getAllMountains() {
        return mountainService.retrieveAllMountains();
    }

    @GetMapping("/{id}")
    public MountainVo getMountainById(@PathVariable Long id) {
        return mountainService.retrieveMountainById(id);
    }

    @GetMapping("/name/{name}")
    public String getMountainByName(@PathVariable String name) {
    	String mountainId = mountainService.retrieveMountainByName(name);
        if (mountainId != null) {
            return mountainId;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 이름의 산을 찾을 수 없습니다: " + name);
        }
    }    
    
    @GetMapping("/search")
    public List<MountainVo> searchMountains(@RequestParam String keyword) {
        return mountainService.searchMountains(keyword);
    }
}
