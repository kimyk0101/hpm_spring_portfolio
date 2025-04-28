/**
 * 파일명: MountainReviewPhotoController.java
 * 작성자: 문호정
 * 설명: 등산 후기 사진 관련 API 요청을 처리하는 컨트롤러 클래스
 *        	- 사진 업로드, 조회, 삭제 관련 기능 제공
 *        	- 다중 파일 업로드 처리
 *         	- 게시글 ID 기준 사진 조회/삭제
 * 작성일: 2025-04-02
 * 수정자: 김경민
 * 수정내용: 개별 사진 삭제 기능 추가, AWS S3와 연동
 * 수정일: 2025.04.07
 * 수정자: 김연경
 * 수정일: 2025-04-12
 * 수정내용: API 수정 및 기존에 업로드된 사진을 삭제하고 새로 추가하는 기능 추가
 */

package himedia.hpm_spring_portfolio.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.model.Model;
import himedia.hpm_spring_portfolio.repository.vo.MountainReviewPhotoVo;
import himedia.hpm_spring_portfolio.service.MountainReviewPhotoService;
import himedia.hpm_spring_portfolio.service.S3Service;

@RestController
@RequestMapping("/api/mountain-reviews/photos")
public class MountainReviewPhotoController {

	@Autowired
	private MountainReviewPhotoService mReviewPhotoService;
	
	@Autowired
	private S3Service s3Service;

	@PostMapping("/upload")
	public ResponseEntity<List<MountainReviewPhotoVo>> uploadPhoto(@RequestParam("reviewsId") Long reviewsId,
			@RequestParam(value = "photos", required = false) MultipartFile[] photos,
			@RequestParam(value = "existingPhotoNames", required = false) List<String> existingPhotoNames)
			throws IOException {

		// ✅ 1. 기존 DB에 등록된 사진 목록 조회
		List<MountainReviewPhotoVo> currentPhotos = mReviewPhotoService.selectAllPhotoByReviewsId(reviewsId);

		// ✅ 2. 기존 사진 중 삭제 대상만 추려서 삭제 (existingPhotoNames에 없는 것들)
		for (MountainReviewPhotoVo photo : currentPhotos) {
			String fileName = photo.getFileName(); 
			if (existingPhotoNames == null || !existingPhotoNames.contains(fileName)) {
				mReviewPhotoService.deletePhotoById(photo.getId()); // DB에서 삭제
				s3Service.deleteFile(fileName); // S3에서 삭제
			}
		}

		// ✅ 3. 새로 추가된 사진 업로드
		if (photos != null && photos.length > 0) {
			List<String> filePaths = mReviewPhotoService.insertPhoto(reviewsId, photos);

			if (filePaths == null) {
				return ResponseEntity.internalServerError().body(null); // 실패 시 처리
			}
		}

		// ✅ 4. 결과 반환 (수정된 전체 사진 목록 조회)
		List<MountainReviewPhotoVo> updatedPhotoList = mReviewPhotoService.selectAllPhotoByReviewsId(reviewsId);
		return ResponseEntity.ok(updatedPhotoList);
	}

	// 등산후기 사진 조회
	@GetMapping("/by-review/{reviewsId}")
	public ResponseEntity<?> viewPhoto(@PathVariable("reviewsId") Long reviewsId, Model model) {
		try {
			List<MountainReviewPhotoVo> photos = mReviewPhotoService.selectAllPhotoByReviewsId(reviewsId);
			// 사진이 없으면 404 말고 빈 배열로
			return ResponseEntity.ok(photos); // 빈 배열 []도 JSON이니까 OK 처리 가능
		} catch (Exception e) {
			// 예외 발생
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("사진 조회 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	// reviewsId로 사진 삭제
	@DeleteMapping("/by-review/{reviewsId}")
	public ResponseEntity<?> deletePhoto(@PathVariable("reviewsId") Long reviewsId) {
		try {
			int result = mReviewPhotoService.deletePhotoByReviewsId(reviewsId);
			if (result == 0) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사진을 찾을 수 없습니다.");
			}
			return ResponseEntity.ok("사진이 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("사진 삭제 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	// photoId로 사진 삭제(사진 개별 삭제)
	@DeleteMapping("/by-photo/{photoId}")
	public ResponseEntity<?> deletePhotoById(@PathVariable("photoId") Long photoId) {
		System.out.println("✅ [삭제 요청 들어옴] photoId = " + photoId);
		try {
			MountainReviewPhotoVo photo = mReviewPhotoService.findPhotoById(photoId);
			System.out.println("📸 photo 객체: " + photo);
			if (photo == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사진이 존재하지 않습니다.");
			}

			// DB에서 삭제
			int result = mReviewPhotoService.deletePhotoById(photoId);
			if (result == 0) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB 삭제 실패");
			}

			return ResponseEntity.ok("사진이 성공적으로 삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace(); // ✅ 콘솔에 에러 출력
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류 발생: " + e.getMessage());
		}
	}

}