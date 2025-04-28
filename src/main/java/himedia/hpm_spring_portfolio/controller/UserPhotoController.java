/**
 * 파일명: UserPhotoController.java
 * 작성자: 문호정
 * 설명: 사용자 프로필 사진 관련 API 요청을 처리하는 컨트롤러 클래스
 *   		- 사용자 ID(usersId)를 기준으로 프로필 사진 업로드 (또는 수정)
 *   		- 특정 사용자의 프로필 사진 조회
 *   		- 특정 사용자의 프로필 사진 삭제
 * 작성일: 2025-04-02
 * 수정자: 김경민
 * 수정내용: 기존 파일 있으면 삭제하고 새로운 사진 저장 기능 추가, AWS S3와 연동
 * 수정일: 2025.04.07
 */

package himedia.hpm_spring_portfolio.controller;

import java.io.IOException;

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

import himedia.hpm_spring_portfolio.repository.vo.UserPhotoVo;
import himedia.hpm_spring_portfolio.service.UserPhotoService;

@RestController
@RequestMapping("/api/users/photos")
public class UserPhotoController {

    @Autowired
    private UserPhotoService userPhotoService;

    //	프로필 사진 업로드(수정) 
    @PostMapping("/upload")
    public ResponseEntity<UserPhotoVo> uploadPhoto(@RequestParam("usersId") Long userId, 
                                                   @RequestParam("photo") MultipartFile photo) throws IOException {
        
        String filePath = userPhotoService.insertPhoto(userId, photo);

        // 파일 저장 후 DB에서 다시 조회
        UserPhotoVo photoVo = userPhotoService.selectPhotoByUserId(userId);

        return ResponseEntity.ok(photoVo); // ✅ JSON 객체로 반환
    }

    //	프로필 사진 조회
    @GetMapping("/by-user/{usersId}")
    public ResponseEntity<UserPhotoVo> viewPhoto(@PathVariable("usersId") Long usersId) {
        UserPhotoVo photo = userPhotoService.selectPhotoByUserId(usersId);

        if (photo != null) {
            return ResponseEntity.ok(photo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    
    //	usersId, photoId로 사진 삭제
    @DeleteMapping("/by-id/{usersId}")
    public ResponseEntity<?> deletePhoto(@PathVariable("usersId") Long usersId) {
        try {
            int result = userPhotoService.deletePhotoByUserId(usersId);
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사진을 찾을 수 없습니다.");
            }
            return ResponseEntity.ok("사진이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사진 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
}