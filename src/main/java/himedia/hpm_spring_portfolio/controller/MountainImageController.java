/**
 * 파일명: MountainImageController.java
 * 설명: 산의 대표 이미지 및 전체 이미지 조회를 위한 컨트롤러
 *   		- 특정 산의 대표 이미지 1장 및 전체 이미지 목록 조회
 * 작성자: 김경민
 * 작성일: 2025-04-10
 */

package himedia.hpm_spring_portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import himedia.hpm_spring_portfolio.repository.vo.MountainImageVo;
import himedia.hpm_spring_portfolio.service.MountainImageService;

@RestController
@RequestMapping("/api/mountains")
public class MountainImageController {
	
	@Autowired
	private MountainImageService mountainImageservice;
	
	// GET : /api/mountains/{mountainId}/image -> 대표 이미지 조회
    @GetMapping("/{mountainId}/image")
    public ResponseEntity<MountainImageVo> getRepresentativeImage(@PathVariable Long mountainId) {
        return ResponseEntity.ok(mountainImageservice.getRepresentativeImage(mountainId));
    }
	
    // GET : /api/mountains/{mountainId}/images -> 전체 이미지 조회
    @GetMapping("/{mountainId}/images")
    public ResponseEntity<List<MountainImageVo>> getImages(@PathVariable Long mountainId) {
        return ResponseEntity.ok(mountainImageservice.getImagesByMountainId(mountainId));
    }

}
