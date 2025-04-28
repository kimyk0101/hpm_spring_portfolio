package himedia.hpm_spring_portfolio.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import himedia.hpm_spring_portfolio.repository.vo.CommunityCommentVo;
import himedia.hpm_spring_portfolio.service.CommunityCommentService;

@RestController
@RequestMapping("/api/communities")
public class CommunityCommentController {

	@Autowired
	private CommunityCommentService cCommentService;

	// GET : /api/communities/{communityId}/comments -> 특정 게시물의 모든 댓글 + 대댓글 조회
	@GetMapping("/{communityId}/comments")
	public ResponseEntity<List<CommunityCommentVo>> retrieveAllComments(@PathVariable Long communityId) {
	    List<CommunityCommentVo> comments = cCommentService.retrieveAllComments(communityId);
	    return ResponseEntity.ok(comments);
	}
	
	// GET : /api/communities/comments/{id} -> 특정 댓글 조회(댓글 하나만 가져옴)
	@GetMapping("/comments/{id}")
	public ResponseEntity<CommunityCommentVo> retrieveCommentById(@PathVariable Long id) {
		CommunityCommentVo comment = cCommentService.retrieveCommentById(id);
		return ResponseEntity.ok(comment);
	}
	
    // GET : /api/communities/{communityId}/{id}/replies -> 특정 댓글의 대댓글 조회 
    @GetMapping("/{communityId}/{id}/replies")
    public ResponseEntity<List<CommunityCommentVo>> retrieveReplies(@PathVariable Long id) {
        List<CommunityCommentVo> replies = cCommentService.retrieveReplies(id);
        return ResponseEntity.ok(replies);
    }
    
    // GET : /api/communities/comments/reply/{id} -> 특정 대댓글 조회 
    @GetMapping("/comments/reply/{id}")
    public ResponseEntity<CommunityCommentVo> retrieveReplyById(@PathVariable Long id) {
        CommunityCommentVo reply = cCommentService.retrieveReplyById(id);
        return ResponseEntity.ok(reply);
    }
    
    // GET: /api/communities/comments/my/{id} -> 사용자가 작성한 댓글 + 대댓글 조회 
    @GetMapping("/comments/my/{id}")
    public ResponseEntity<List<CommunityCommentVo>> retrieveCommentsByUser(@PathVariable Long id) {
        List<CommunityCommentVo> comments = cCommentService.retrieveMyComments(id);
        return ResponseEntity.ok(comments);
    }
    
	// POST : /api/communities/{communityId}/comments -> 댓글 생성
	@PostMapping("/{communityId}/comments")
	public ResponseEntity<CommunityCommentVo> createComment(@PathVariable Long communityId, @RequestBody CommunityCommentVo comment) {
	    if (comment.getUsersId() == null || comment.getUsersId() <= 0) {
	        return ResponseEntity.badRequest().body(null);
	    }

	    // 커뮤니티 ID 설정
	    comment.setCommunitiesId(communityId);
	    
	    // 대댓글이 아닐 경우 parentId를 NULL로 설정
	    if (comment.getParentId() == null || comment.getParentId() <= 0) {
	        comment.setParentId(null);
	    }

	    CommunityCommentVo savedComment = cCommentService.createComment(comment);
	    return ResponseEntity.ok(savedComment);
	}

	// PATCH : /api/communities/comments/{id} -> 댓글 수정
	@PatchMapping("/comments/{id}")
	public ResponseEntity<CommunityCommentVo> updateComment(@RequestBody CommunityCommentVo comment,
			@PathVariable Long id) {
		comment.setId(id);
		CommunityCommentVo updateComment = cCommentService.updateComment(comment);
		return ResponseEntity.ok(updateComment);
	}

	// DELETE : /api/communities/comments/{id} -> 댓글 삭제
	@DeleteMapping("/comments/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
		Long usersId = requestBody.get("usersId"); // 클라이언트에서 전달받은 usersId

		// 삭제 쿼리 실행
		cCommentService.deleteComment(id, usersId);
		return ResponseEntity.ok().<Void>build();
	}
	
	// DELETE : /api/communities/{communitiesId}/comments -> communitiesId 기준 모든 댓글 삭제
	@DeleteMapping("/{communitiesId}/comments")
	public ResponseEntity<?> deleteCommentsByReviewsId(@PathVariable Long communitiesId) {
		try {
			cCommentService.deleteCommentsByCommunitiesId(communitiesId);
			return ResponseEntity.ok("리뷰에 달린 모든 댓글이 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 실패: " + e.getMessage());
		}
	}
	
	// POST : /api/communities/{communityId}/comments/{commentId}/replies -> 대댓글 생성
	@PostMapping("/{communityId}/comments/{commentId}/replies")
	public ResponseEntity<CommunityCommentVo> createReply(
	        @PathVariable Long communityId,
	        @PathVariable Long commentId,
	        @RequestBody CommunityCommentVo reply) {
	    
	    reply.setCommunitiesId(communityId);
	    reply.setParentId(commentId); // 부모 댓글 ID 설정

	    CommunityCommentVo savedReply = cCommentService.createReply(reply);
	    return ResponseEntity.ok(savedReply);
	}

	// PATCH : /api/communities/comments/replies/{replyId} -> 대댓글 수정 
	@PatchMapping("/comments/replies/{replyId}")
	public ResponseEntity<CommunityCommentVo> updateReply(
	        @PathVariable Long replyId,
	        @RequestBody CommunityCommentVo reply) {

	    reply.setId(replyId);
	    CommunityCommentVo updatedReply = cCommentService.updateReply(reply);
	    return ResponseEntity.ok(updatedReply);
	}

	// DELETE : /api/communities/comments/replies/{replyId} -> 대댓글 삭제 
	@DeleteMapping("/comments/replies/{replyId}")
	public ResponseEntity<Void> deleteReply(
	        @PathVariable Long replyId,
	        @RequestBody Map<String, Long> requestBody) {

	    Long usersId = requestBody.get("usersId"); // 유저 ID 검증

	    cCommentService.deleteReply(replyId, usersId);
	    return ResponseEntity.ok().<Void>build();
	}
}
