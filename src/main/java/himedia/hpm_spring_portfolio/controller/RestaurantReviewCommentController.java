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

import himedia.hpm_spring_portfolio.repository.vo.RestaurantReviewCommentVo;
import himedia.hpm_spring_portfolio.service.RestaurantReviewCommentService;

@RestController
@RequestMapping("/api/restaurant-reviews")
public class RestaurantReviewCommentController {

	@Autowired
	private RestaurantReviewCommentService rCommentService;

	// GET : /api/restaurant-reviews/{rReviewId}/comments -> 특정 게시물의 모든 댓글 + 대댓글 조회
	@GetMapping("/{rReviewId}/comments")
	public ResponseEntity<List<RestaurantReviewCommentVo>> retrieveAllComments(@PathVariable Long rReviewId) {
		List<RestaurantReviewCommentVo> comments = rCommentService.retrieveAllComments(rReviewId);
		return ResponseEntity.ok(comments);
	}

	// GET : /api/restaurant-reviews/comments/{id} -> 특정 댓글 조회(댓글 하나만 가져옴)
	@GetMapping("/comments/{id}")
	public ResponseEntity<RestaurantReviewCommentVo> retrieveCommentById(@PathVariable Long id) {
		RestaurantReviewCommentVo comment = rCommentService.retrieveCommentById(id);
		return ResponseEntity.ok(comment);
	}

	// GET : /api/restaurant-reviews/{rReviewId}/{id}/replies -> 특정 댓글의 대댓글 조회
	@GetMapping("/{rReviewId}/{id}/replies")
	public ResponseEntity<List<RestaurantReviewCommentVo>> retrieveReplies(@PathVariable Long id) {
		List<RestaurantReviewCommentVo> replies = rCommentService.retrieveReplies(id);
		return ResponseEntity.ok(replies);
	}

	// GET : /api/restaurant-reviews/comments/reply/{id} -> 특정 대댓글 조회
	@GetMapping("/comments/reply/{id}")
	public ResponseEntity<RestaurantReviewCommentVo> retrieveReplyById(@PathVariable Long id) {
		RestaurantReviewCommentVo reply = rCommentService.retrieveReplyById(id);
		return ResponseEntity.ok(reply);
	}

	// GET: /api/communities/comments/my/{id} -> 사용자가 작성한 댓글 + 대댓글 조회
	@GetMapping("/comments/my/{id}")
	public ResponseEntity<List<RestaurantReviewCommentVo>> retrieveCommentsByUser(@PathVariable Long id) {
		List<RestaurantReviewCommentVo> comments = rCommentService.retrieveMyComments(id);
		return ResponseEntity.ok(comments);
	}

	// POST : /api/restaurant-reviews/{rReviewId}/comments -> 댓글 생성
	@PostMapping("/{rReviewId}/comments")
	public ResponseEntity<RestaurantReviewCommentVo> createComment(@PathVariable Long rReviewId,
			@RequestBody RestaurantReviewCommentVo comment) {
		if (comment.getUsersId() == null || comment.getUsersId() <= 0) {
			return ResponseEntity.badRequest().body(null);
		}

		// 커뮤니티 ID 설정
		comment.setRestaurantReviewsId(rReviewId);

		// 대댓글이 아닐 경우 parentId를 NULL로 설정
		if (comment.getParentId() == null || comment.getParentId() <= 0) {
			comment.setParentId(null);
		}

		RestaurantReviewCommentVo savedComment = rCommentService.createComment(comment);
		return ResponseEntity.ok(savedComment);
	}

	// PATCH : /api/restaurant-reviews/comments/{id} -> 댓글 수정
	@PatchMapping("/comments/{id}")
	public ResponseEntity<RestaurantReviewCommentVo> updateComment(@RequestBody RestaurantReviewCommentVo comment,
			@PathVariable Long id) {
		comment.setId(id);
		RestaurantReviewCommentVo updateComment = rCommentService.updateComment(comment);
		return ResponseEntity.ok(updateComment);
	}

	// DELETE : /api/restaurant-reviews/comments/{id} -> 댓글 삭제
	@DeleteMapping("/comments/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
		Long usersId = requestBody.get("usersId"); // 클라이언트에서 전달받은 usersId

		// 삭제 쿼리 실행
		rCommentService.deleteComment(id, usersId);
		return ResponseEntity.ok().<Void>build();
	}

	// DELETE : /api/restaurant-reviews/{restaurantsId}/comments -> restaurantsId 기준
	// 모든 댓글 삭제
	@DeleteMapping("/{restaurantsId}/comments")
	public ResponseEntity<?> deleteCommentsByRestaurantsId(@PathVariable Long restaurantsId) {
		try {
			rCommentService.deleteCommentsByRestaurantsId(restaurantsId);
			return ResponseEntity.ok("리뷰에 달린 모든 댓글이 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 실패: " + e.getMessage());
		}
	}

	// POST : /api/restaurant-reviews/{rReviewId}/comments/{commentId}/replies ->
	// 대댓글 생성
	@PostMapping("/{rReviewId}/comments/{commentId}/replies")
	public ResponseEntity<RestaurantReviewCommentVo> createReply(@PathVariable Long rReviewId,
			@PathVariable Long commentId, @RequestBody RestaurantReviewCommentVo reply) {

		reply.setRestaurantReviewsId(rReviewId);
		reply.setParentId(commentId); // 부모 댓글 ID 설정

		RestaurantReviewCommentVo savedReply = rCommentService.createReply(reply);
		return ResponseEntity.ok(savedReply);
	}

	// PATCH : /api/restaurant-reviews/comments/replies/{replyId} -> 대댓글 수정
	@PatchMapping("/comments/replies/{replyId}")
	public ResponseEntity<RestaurantReviewCommentVo> updateReply(@PathVariable Long replyId,
			@RequestBody RestaurantReviewCommentVo reply) {

		reply.setId(replyId);
		RestaurantReviewCommentVo updatedReply = rCommentService.updateReply(reply);
		return ResponseEntity.ok(updatedReply);
	}

	// DELETE : /api/restaurant-reviews/comments/replies/{replyId} -> 대댓글 삭제
	@DeleteMapping("/comments/replies/{replyId}")
	public ResponseEntity<Void> deleteReply(@PathVariable Long replyId, @RequestBody Map<String, Long> requestBody) {

		Long usersId = requestBody.get("usersId"); // 유저 ID 검증

		rCommentService.deleteReply(replyId, usersId);
		return ResponseEntity.ok().<Void>build();
	}
}
