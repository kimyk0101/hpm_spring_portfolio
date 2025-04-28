package himedia.hpm_spring_portfolio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import himedia.hpm_spring_portfolio.mappers.CommunityPhotoMapper;
import himedia.hpm_spring_portfolio.repository.vo.CommunityPhotoVo;
import himedia.hpm_spring_portfolio.util.S3PathUtil;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class CommunityPhotoService {

    @Autowired
    private CommunityPhotoMapper communityPhotoMapper;
    
    @Autowired 
    private S3Client s3Client; 
    
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

    @Transactional    
    public List<String> insertPhoto(Long communitiesId, MultipartFile[] photos) { 
    	List<String> s3Urls = new ArrayList<>();

        try {
            for (MultipartFile photo : photos) {
            	 //	1. S3 키 생성
            	String s3Key = S3PathUtil.getCommunityPhotoPath(communitiesId, photo.getOriginalFilename());
                
            	 // 2. S3 업로드 요청
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(s3Key)
                        .contentType(photo.getContentType())
                        .build();
                
                s3Client.putObject(putObjectRequest,
                        RequestBody.fromInputStream(photo.getInputStream(), photo.getSize()));
            	
                // 3. S3 URL 생성
                String s3Url = "https://" + bucket + ".s3." + System.getenv("AWS_REGION") + ".amazonaws.com/" + s3Key;
                
                // 4. DB 업데이트
                CommunityPhotoVo communityPhotoVo = new CommunityPhotoVo();
                communityPhotoVo.setCommunitiesId(communitiesId);
                communityPhotoVo.setFileName(photo.getOriginalFilename());
                communityPhotoVo.setFilePath(s3Url); // S3 URL 저장 
                communityPhotoVo.setUpdateDate(new Date());

                communityPhotoMapper.insertPhoto(communityPhotoVo);
                s3Urls.add(s3Url);
            }

            return s3Urls;

        } catch (Exception e) {
            // 에러 로그
            System.err.println("커뮤니티 사진 업로드 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //	게시글 사진 전체 조회
    public List<CommunityPhotoVo> selectAllPhotoByCommunityId(Long communitiesId) {
        return communityPhotoMapper.selectAllPhotoByCommunityId(communitiesId);
    }

    //	게시글 사진 전체 삭제(S3 + DB)
    public int deletePhotoByCommunityId(Long communitiesId) {
        List<CommunityPhotoVo> photoList = communityPhotoMapper.selectAllPhotoByCommunityId(communitiesId);

        for (CommunityPhotoVo photo : photoList) {
            String s3Url = photo.getFilePath();
            if (s3Url != null && !s3Url.isBlank()) {
                String s3Key = extractS3KeyFromUrl(s3Url);

                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(s3Key)
                        .build();
                
                System.out.println("🧹 S3에서 삭제할 key: " + s3Key);
                s3Client.deleteObject(deleteRequest);
            }
        }

        return communityPhotoMapper.deletePhotoByCommunityId(communitiesId);
    }
    
    //	특정 사진 개별 조회
    public CommunityPhotoVo findPhotoById(Long photoId) {
        return communityPhotoMapper.findPhotoById(photoId);
    }
    
    //	특정 사진 개별 삭제 
    public int deletePhotoById(Long photoId) {
        CommunityPhotoVo photo = findPhotoById(photoId);
        if (photo == null) return 0;

        String s3Url = photo.getFilePath();
        if (s3Url != null && !s3Url.isBlank()) {
            String s3Key = extractS3KeyFromUrl(s3Url);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build();

            s3Client.deleteObject(deleteRequest);
        }

        return communityPhotoMapper.deletePhotoById(photoId);
    }
    
    // S3 URL에서 Key 추출하여 삭제 요청에 사용
    private String extractS3KeyFromUrl(String url) {
        String baseUrl = "https://" + bucket + ".s3." + System.getenv("AWS_REGION") + ".amazonaws.com/";
        return url.replace(baseUrl, "");
    }

}