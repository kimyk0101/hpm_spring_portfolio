/**
 * 파일명: CommunityPhotoController.java
 * 작성자: 문호정
 * 설명: 커뮤니티 사진 관련 API 요청을 처리하는 컨트롤러 클래스
 * 			- 사진의 업로드, 조회, 삭제 관련 기능 제공
 *         	- 다중 파일 업로드 처리
 *         	- 게시글 ID 기준 사진 조회/삭제
 * 작성일: 2025-04-02
 * 수정자: 김경민
 * 수정내용: 개별 사진 삭제 기능 추가, AWS S3와 연동
 * 수정일: 2025.04.07
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
import himedia.hpm_spring_portfolio.repository.vo.CommunityPhotoVo;
import himedia.hpm_spring_portfolio.service.CommunityPhotoService;

@RestController
@RequestMapping("/api/communities/photos")
public class CommunityPhotoController {

    @Autowired
    private CommunityPhotoService communityPhotoService;

    //	커뮤니티 사진 업로드
    @PostMapping("/upload")
    public ResponseEntity<List<CommunityPhotoVo>> uploadPhoto(@RequestParam("communitiesId") Long communitiesId, 
                                              @RequestParam("photos") MultipartFile[] photos) throws IOException {
    	
    	
        if (photos == null || photos.length == 0) {
            return ResponseEntity.badRequest().body(null); // 오류 발생시 null
        }

        List<String> filePaths = communityPhotoService.insertPhoto(communitiesId, photos);

        if (filePaths == null) {
            return ResponseEntity.internalServerError().body(null); // 서비스에서 null 반환시 서버 오류 처리
        }

        // 파일 저장 후 DB에서 다시 조회
        List<CommunityPhotoVo> photoVo = communityPhotoService.selectAllPhotoByCommunityId(communitiesId);
        System.out.println("✔️ 업로드된 커뮤니티 ID: " + communitiesId);
        System.out.println("✔️ 파일 수: " + photos.length);
        System.out.println("✔️ 저장된 경로 리스트: " + filePaths);
        return ResponseEntity.ok(photoVo); // JSON 객체로 반환
    }
    
    
    
    
    //	커뮤니티 사진 조회
    @GetMapping("/by-community/{communitiesId}")
    public ResponseEntity<?> viewPhoto(@PathVariable("communitiesId") Long communitiessId, Model model) {
        try {
            List<CommunityPhotoVo> photos = communityPhotoService.selectAllPhotoByCommunityId(communitiessId);
            if (photos == null || photos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자의 사진이 존재하지 않습니다.");
            }
            return ResponseEntity.ok(photos);
        } catch (Exception e) {
            // 예외 발생
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사진 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    
    //	communitysId로 사진 삭제(모든 사진 삭제)  
    @DeleteMapping("/by-community/{communitiesId}")
    public ResponseEntity<?> deletePhoto(@PathVariable("communitiesId") Long communitiesId) {
        try {
            int result = communityPhotoService.deletePhotoByCommunityId(communitiesId);
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사진을 찾을 수 없습니다.");
            }
            return ResponseEntity.ok("사진이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사진 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    //	photoId로 사진 삭제(사진 개별 삭제)
    @DeleteMapping("/by-photo/{photoId}")
    public ResponseEntity<?> deletePhotoById(@PathVariable("photoId") Long photoId) {
    	System.out.println("✅ [삭제 요청 들어옴] photoId = " + photoId);
    	try {
            CommunityPhotoVo photo = communityPhotoService.findPhotoById(photoId);
            System.out.println("📸 photo 객체: " + photo); 
            if (photo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사진이 존재하지 않습니다.");
            }
            
            // 2️. DB에서 삭제   
            int result = communityPhotoService.deletePhotoById(photoId);
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB 삭제 실패");
            }

            return ResponseEntity.ok("사진이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
        	e.printStackTrace(); // ✅ 콘솔에 에러 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 중 오류 발생: " + e.getMessage());
        }
    }
    
}