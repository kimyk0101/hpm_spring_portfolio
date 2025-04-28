package himedia.hpm_spring_portfolio.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import himedia.hpm_spring_portfolio.mappers.UserPhotoMapper;
import himedia.hpm_spring_portfolio.repository.vo.UserPhotoVo;
import himedia.hpm_spring_portfolio.util.S3PathUtil;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class UserPhotoService {

    @Autowired
    private UserPhotoMapper userPhotoMapper;
    
    @Autowired
    private S3Client s3Client;
    
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Transactional
	public String insertPhoto(Long usersId, MultipartFile photo) {
	    try {

	        // 1. S3에서 기존 사진 삭제 (있을 경우)
	        UserPhotoVo existing = userPhotoMapper.selectPhotoByUserId(usersId);
	        if (existing != null) {
	        	 s3Client.deleteObject(builder ->
	                builder.bucket(bucket).key(existing.getFilePath())
	            );
	        }

	        // 2. 새 경로 생성
	        String key = S3PathUtil.getUserProfilePath(usersId, photo.getOriginalFilename());
	        
	        // 3. S3 업로드
	        s3Client.putObject(
	        		PutObjectRequest.builder()
	                	.bucket(bucket)
	                    .key(key)
	                    .contentType(photo.getContentType())
	                    .build(),
	                RequestBody.fromInputStream(photo.getInputStream(), photo.getSize())
	        );
	        
	        // 4. PhotoVo 객체 생성 및 DB 저장
	        UserPhotoVo userPhotoVo = new UserPhotoVo();
	        userPhotoVo.setUsersId(usersId);
	        userPhotoVo.setFilePath(key); // ✅ key만 저장
	        userPhotoVo.setFileName(photo.getOriginalFilename());
	        userPhotoVo.setUpdateDate(new Date());

	        userPhotoMapper.insertOrUpdatePhoto(userPhotoVo);

	        return key;

	    } catch (Exception e) {
	        // 에러 로그 좀 더 구체적으로
	    	  System.err.println("S3 프로필 사진 업로드 실패: " + e.getMessage());
	          e.printStackTrace();
	          return null;
	    }
	}

    //	프로필 사진 조회
    public UserPhotoVo selectPhotoByUserId(Long usersId) {
        return userPhotoMapper.selectPhotoByUserId(usersId);
    }

    //	프로필 사진 삭제
    @Transactional
    public int deletePhotoByUserId(Long usersId) {
        return userPhotoMapper.deletePhotoByUserId(usersId);
    }

}