package himedia.hpm_spring_portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import himedia.hpm_spring_portfolio.repository.vo.RestaurantReviewLikeVo;
import himedia.hpm_spring_portfolio.service.RestaurantReviewLikeService;

@RestController
@RequestMapping("/api/restaurant-reviews/likes/")
public class RestaurantReviewLikeController {

	@Autowired
	private RestaurantReviewLikeService rReviewLikeService;

	// 좋아요 토글 (POST 요청)
	@PostMapping("toggle")
	public ResponseEntity<String> toggleLike(@RequestBody RestaurantReviewLikeVo vo) {
		System.out.println("💬 toggleLike 컨트롤러 호출됨: " + vo); 
		boolean result = rReviewLikeService.toggleLike(vo);

		return result ? ResponseEntity.ok("좋아요 토글 완료")
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body("실패: 사용자 또는 리뷰 없음");
	}

	// 리뷰에 대한 좋아요 개수 조회
	@GetMapping("/count")
	public ResponseEntity<Integer> getLikeCount(@RequestParam("restaurantsId") Long restaurantsId) {
		int count = rReviewLikeService.getLikeCount(restaurantsId);
		return ResponseEntity.ok(count);
	}

	// 사용자가 해당 리뷰에 좋아요 눌렀는지 확인
	@GetMapping("/is-liked")
	public ResponseEntity<Boolean> isLiked(@RequestParam("usersId") Long usersId,
			@RequestParam("restaurantsId") Long restaurantsId) {
		RestaurantReviewLikeVo rLikeVo = new RestaurantReviewLikeVo();
		rLikeVo.setUsersId(usersId);
		rLikeVo.setRestaurantReviewsId(restaurantsId);

		Boolean isLiked = rReviewLikeService.isLiked(rLikeVo); // Integer로 받아서 null 가능성 있음

		return ResponseEntity.ok(isLiked != null && isLiked); // null이면 false로 처리
	}
}
