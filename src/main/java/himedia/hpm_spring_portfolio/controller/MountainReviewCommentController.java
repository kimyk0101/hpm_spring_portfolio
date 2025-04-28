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

import himedia.hpm_spring_portfolio.repository.vo.MountainReviewCommentVo;
import himedia.hpm_spring_portfolio.service.MountainReviewCommentService;

@RestController
@RequestMapping("/api/mountain-reviews")
public class MountainReviewCommentController {

	@Autowired
	private MountainReviewCommentService mCommentService;

	// GET : /api/mountain-reviews/{mReviewId}/comments -> 특정 게시물의 모든 댓글 + 대댓글 조회
	@GetMapping("/{mReviewId}/comments")
	public ResponseEntity<List<MountainReviewCommentVo>> retrieveAllComments(@PathVariable Long mReviewId) {
		List<MountainReviewCommentVo> comments = mCommentService.retrieveAllComments(mReviewId);
		return ResponseEntity.ok(comments);
	}

	// GET : /api/mountain-reviews/comments/{id} -> 특정 댓글 조회(댓글 하나만 가져옴)
	@GetMapping("/comments/{id}")
	public ResponseEntity<MountainReviewCommentVo> retrieveCommentById(@PathVariable Long id) {
		MountainReviewCommentVo comment = mCommentService.retrieveCommentById(id);
		return ResponseEntity.ok(comment);
	}

	// GET : /api/mountain-reviews/{mReviewId}/{id}/replies -> 특정 댓글의 대댓글 조회
	@GetMapping("/{mReviewId}/{id}/replies")
	public ResponseEntity<List<MountainReviewCommentVo>> retrieveReplies(@PathVariable Long id) {
		List<MountainReviewCommentVo> replies = mCommentService.retrieveReplies(id);
		return ResponseEntity.ok(replies);
	}

	// GET : /api/mountain-reviews/comments/reply/{id} -> 특정 대댓글 조회
	@GetMapping("/comments/reply/{id}")
	public ResponseEntity<MountainReviewCommentVo> retrieveReplyById(@PathVariable Long id) {
		MountainReviewCommentVo reply = mCommentService.retrieveReplyById(id);
		return ResponseEntity.ok(reply);
	}

	// GET: /api/communities/comments/my/{id} -> 사용자가 작성한 댓글 + 대댓글 조회
	@GetMapping("/comments/my/{id}")
	public ResponseEntity<List<MountainReviewCommentVo>> retrieveCommentsByUser(@PathVariable Long id) {
		List<MountainReviewCommentVo> comments = mCommentService.retrieveMyComments(id);
		return ResponseEntity.ok(comments);
	}

	// POST : /api/mountain-reviews/{mReviewId}/comments -> 댓글 생성
	@PostMapping("/{mReviewId}/comments")
	public ResponseEntity<MountainReviewCommentVo> createComment(@PathVariable Long mReviewId,
			@RequestBody MountainReviewCommentVo comment) {
		if (comment.getUsersId() == null || comment.getUsersId() <= 0) {
			return ResponseEntity.badRequest().body(null);
		}

		// 커뮤니티 ID 설정
		comment.setMountainReviewsId(mReviewId);

		// 대댓글이 아닐 경우 parentId를 NULL로 설정
		if (comment.getParentId() == null || comment.getParentId() <= 0) {
			comment.setParentId(null);
		}

		MountainReviewCommentVo savedComment = mCommentService.createComment(comment);
		return ResponseEntity.ok(savedComment);
	}

	// PATCH : /api/mountain-reviews/comments/{id} -> 댓글 수정
	@PatchMapping("/comments/{id}")
	public ResponseEntity<MountainReviewCommentVo> updateComment(@RequestBody MountainReviewCommentVo comment,
			@PathVariable Long id) {
		comment.setId(id);
		MountainReviewCommentVo updateComment = mCommentService.updateComment(comment);
		return ResponseEntity.ok(updateComment);
	}

	// DELETE : /api/mountain-reviews/comments/{id} -> 댓글 삭제
	@DeleteMapping("/comments/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
		Long usersId = requestBody.get("usersId"); // 클라이언트에서 전달받은 usersId

		// 삭제 쿼리 실행
		mCommentService.deleteComment(id, usersId);
		return ResponseEntity.ok().<Void>build();
	}

	// DELETE : /api/mountain-reviews/{reviewsId}/comments -> reviewsId 기준 모든 댓글 삭제
	@DeleteMapping("/{reviewsId}/comments")
	public ResponseEntity<?> deleteCommentsByReviewsId(@PathVariable Long reviewsId) {
		try {
			mCommentService.deleteCommentsByReviewsId(reviewsId);
			return ResponseEntity.ok("리뷰에 달린 모든 댓글이 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 실패: " + e.getMessage());
		}
	}

	// POST : /api/mountain-reviews/{mReviewId}/comments/{commentId}/replies -> 대댓글
	// 생성
	@PostMapping("/{mReviewId}/comments/{commentId}/replies")
	public ResponseEntity<MountainReviewCommentVo> createReply(@PathVariable Long mReviewId,
			@PathVariable Long commentId, @RequestBody MountainReviewCommentVo reply) {

		reply.setMountainReviewsId(mReviewId);
		reply.setParentId(commentId); // 부모 댓글 ID 설정

		MountainReviewCommentVo savedReply = mCommentService.createReply(reply);
		return ResponseEntity.ok(savedReply);
	}

	// PATCH : /api/mountain-reviews/comments/replies/{replyId} -> 대댓글 수정
	@PatchMapping("/comments/replies/{replyId}")
	public ResponseEntity<MountainReviewCommentVo> updateReply(@PathVariable Long replyId,
			@RequestBody MountainReviewCommentVo reply) {

		reply.setId(replyId);
		MountainReviewCommentVo updatedReply = mCommentService.updateReply(reply);
		return ResponseEntity.ok(updatedReply);
	}

	// DELETE : /api/mountain-reviews/comments/replies/{replyId} -> 대댓글 삭제
	@DeleteMapping("/comments/replies/{replyId}")
	public ResponseEntity<Void> deleteReply(@PathVariable Long replyId, @RequestBody Map<String, Long> requestBody) {

		Long usersId = requestBody.get("usersId"); // 유저 ID 검증

		mCommentService.deleteReply(replyId, usersId);
		return ResponseEntity.ok().<Void>build();
	}
}
