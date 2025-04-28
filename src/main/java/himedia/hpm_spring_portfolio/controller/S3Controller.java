/**
 * 파일명: S3Controller.java, S3Config.java, S3PathUtil.java
 * 작성자: 김경민
 * 설명: 
 * - S3Service를 통해 프론트엔드에서 사용할 Presigned URL을 발급해주는 컨트롤러
 * - Presigned URL: 프론트엔드에서 직접 AWS S3에 파일을 업로드할 수 있게 만들어주는 임시 암호화 URL
 * - 사용 목적: 백엔드 직접 업로드 방식과 다르게, 파일이 서버를 거치지 않기 때문에 서버 부하 줄어듦 
 * 
 * S3Config.java
 * - AWS S3와 연동하기 위한 설정 클래스
 * - AWS 인증 정보를 이용해 S3Client 객체 생성(업로드, 아운로드, 삭제 등에 사용)  
 * - Bean으로 등록해서 다른 서비스나 컨트롤러에서 자유롭게 사용할 수 있도록 함   
 * 
 * S3PathUtil.java
 * - AWS S3에 사진을 업로드할 때 사용할 "저장 경로(path)"를 생성하는 유틸리티 클래스
 * - 파일명을 고유하게 만들기 위해 UUID를 사용
 * - 사용 목적(프로필, 커뮤니티, 등산후기 등)에 따라 폴더를 나눠서 저장 
 *    
 * 작성일: 2025-04-09
 */
package himedia.hpm_spring_portfolio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import himedia.hpm_spring_portfolio.service.S3Service;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    
/**
 * Presigned URL 발급 API
 * 프론트에서 이미지 파일 업로드 전에 이 API를 호출해서 S3에 업로드 가능한 URL을 발급받는다.
 * 해당 URL은 일정 시간 동안만 유효하고, 해당 경로로 직접 PUT 요청을 보내면 파일이 업로드됨.
 *
 * 예: GET /api/s3/presigned-url?fileName=test.jpg
 *
 * @param fileName 업로드할 파일 이름
 * @return S3에 직접 업로드할 수 있는 presigned URL (문자열 형태)
 */
     
    @GetMapping("/presigned-url")
    public String getPresignedUrl(@RequestParam String fileName) {
        return s3Service.generatePresignedUrl(fileName).toString();
    }
}
