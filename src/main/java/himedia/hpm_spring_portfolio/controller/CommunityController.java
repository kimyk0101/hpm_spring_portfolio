/**
 * 파일명: CommunityController.java, CommunityCommentController.java
 * 작성자: 김연경
 * 작성일: 2025-03-25 ~ 04-03
 * 
 * 설명:
 * - 커뮤니티 게시글 관련 API 요청을 처리하는 컨트롤러 클래스
 * - 커뮤니티 게시글에 달린 댓글 및 대댓글 관련 API 요청을 처리하는 컨트롤러 클래스
 * 
 * CommunityController.java
 * - 커뮤니티 게시글의 CRUD(생성, 읽기, 수정, 삭제) 기능 제공
 * - 특정 사용자의 게시글 목록 조회, 게시글 조회수 증가 기능 포함
 * 
 * CommunityCommentController.java
 * - 커뮤니티 게시글의 댓글 및 대댓글 CRUD 기능 제공
 * - 댓글과 대댓글의 계층 구조 관리
 * 
 * 수정자: 김경민
 * 수정내용: 커뮤니티 게시글 검색 기능 추가, 사용자가 작성한 댓글 조회 API 추가
 * 수정일: 2025-03-28 ~ 04-04
 */

package himedia.hpm_spring_portfolio.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import himedia.hpm_spring_portfolio.repository.vo.CommunityVo;
import himedia.hpm_spring_portfolio.service.CommunityService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/communities")
public class CommunityController {

	@Autowired
	private CommunityService communityService;

	// GET : /api/communities -> 모든 커뮤니티 게시글 조회
	@GetMapping
	public ResponseEntity<List<CommunityVo>> retrieveAllCommunities() {
		List<CommunityVo> communities = communityService.retrieveAllCommunities();
		return ResponseEntity.ok(communities);
	}

	// GET : /api/communities/{id} -> 특정 게시글 조회
	@GetMapping("/{id}")
	public ResponseEntity<CommunityVo> retrieveCommunityById(@PathVariable Long id) {
		CommunityVo community = communityService.retrieveCommunityById(id);
		return ResponseEntity.ok(community);
	}

	// GET : /api/communities/my/{id} -> 사용자의 게시글 조회
	@GetMapping("/my/{id}")
	public ResponseEntity<List<CommunityVo>> retrieveMyCommunities(@PathVariable Long id) {
		List<CommunityVo> communities = communityService.retrieveMyCommunities(id);
		return ResponseEntity.ok(communities);
	}
	
	// [경민] GET : /api/communities/search?q=#{keyword} -> 키워드 기반 게시글 조회
		@GetMapping("/search")
		public ResponseEntity<List<CommunityVo>> retrieveCommunitiesByKeyword(@RequestParam("q") String keyword) {
			// 정확한 단어 검색을 위한 커스텀 정규식 패턴 생성
		    String pattern = "(^|[^가-힣a-zA-Z0-9])" + keyword + "([^가-힣a-zA-Z0-9]|$)";
			List<CommunityVo> results = communityService.retrieveCommunitiesByKeyword(pattern);
			return ResponseEntity.ok(results);
		}
	
	// POST : /api/communities -> 게시글 생성
	@PostMapping
	public ResponseEntity<CommunityVo> createCommunity(@RequestBody CommunityVo community) {
		 System.out.println("받은 데이터: " + community);
		if (community.getUsersId() == null || community.getUsersId() <= 0) {
			return ResponseEntity.badRequest().body(null); // 유효하지 않은 usersId에 대한 오류 응답
		}

		CommunityVo savedCommunity = communityService.createCommunity(community);
		return ResponseEntity.ok(savedCommunity);
	}

	// PATCH : /api/communities/{id} -> 게시글 일부 수정
	@PatchMapping("/{id}")
	public ResponseEntity<CommunityVo> updateCommunity(@RequestBody CommunityVo community, @PathVariable Long id) {
		community.setId(id);
		CommunityVo updatedCommunity = communityService.updateCommunity(community);
		return ResponseEntity.ok(updatedCommunity);
	}

	// DELETE : /api/communities/{id} -> 게시글 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCommunity(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
		Long usersId = requestBody.get("usersId"); // 클라이언트에서 전달받은 usersId

	    // 삭제 쿼리 실행    
		communityService.deleteCommunity(id, usersId);
		return ResponseEntity.ok().<Void>build();
	}
	
	//	PUT : /api/communities/{id}/increment-views -> 조회수
    @PutMapping("/{id}/increment-views")
    public ResponseEntity<Void> incrementViews(@PathVariable("id") Long id) {
        communityService.incrementViews(id);
        return ResponseEntity.ok().build();
    }
}
