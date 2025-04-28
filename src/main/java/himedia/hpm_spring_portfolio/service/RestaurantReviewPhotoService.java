package himedia.hpm_spring_portfolio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import himedia.hpm_spring_portfolio.mappers.RestaurantReviewPhotoMapper;
import himedia.hpm_spring_portfolio.repository.vo.RestaurantReviewPhotoVo;
import himedia.hpm_spring_portfolio.util.S3PathUtil;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class RestaurantReviewPhotoService {

    @Autowired
    private RestaurantReviewPhotoMapper rReviewPhotoMapper;
    
    @Autowired
    private S3Client s3Client;
    
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

    @Transactional    
    public List<String> insertPhoto(Long restaurantsId, MultipartFile[] photos) {
        List<String> s3Urls = new ArrayList<>();

        try {
            for (MultipartFile photo : photos) {
            	String s3Key = S3PathUtil.getFoodReviewPhotoPath(restaurantsId, photo.getOriginalFilename());
                
            	PutObjectRequest putRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(s3Key)
                        .contentType(photo.getContentType())
                        .build();

                s3Client.putObject(putRequest, RequestBody.fromInputStream(photo.getInputStream(), photo.getSize()));

                String s3Url = "https://" + bucket + ".s3." + System.getenv("AWS_REGION") + ".amazonaws.com/" + s3Key;
            	
                // 3. DB 업데이트
                RestaurantReviewPhotoVo rReviewPhotoVo = new RestaurantReviewPhotoVo();
                rReviewPhotoVo.setRestaurantReviewsId(restaurantsId);
                rReviewPhotoVo.setFileName(photo.getOriginalFilename());
                rReviewPhotoVo.setFilePath(s3Url); // 프론트에서 접근할 URL 기준 경로
                rReviewPhotoVo.setUpdateDate(new Date());

                rReviewPhotoMapper.insertPhoto(rReviewPhotoVo);
                s3Urls.add(s3Url.toString());
            }

            return s3Urls;

        } catch (Exception e) {
            // 에러 로그
            System.err.println("맛집후기 사진 업로드 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //	맛집후기 사진 전체 조회
    public List<RestaurantReviewPhotoVo> selectAllPhotoByRestaurantsId(Long restaurantsId) {
        return rReviewPhotoMapper.selectAllPhotoByRestaurantsId(restaurantsId);
    }
    
    //	맛집후기 사진 전체 삭제
    @Transactional
    public int deletePhotoByRestaurantsId(Long restaurantsId) {
        List<RestaurantReviewPhotoVo> photoList = rReviewPhotoMapper.selectAllPhotoByRestaurantsId(restaurantsId);

        for (RestaurantReviewPhotoVo photo : photoList) {
            String s3Url = photo.getFilePath();
            if (s3Url != null && !s3Url.isBlank()) {
                String s3Key = extractS3KeyFromUrl(s3Url);
                
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(bucket).key(s3Key).build();

                s3Client.deleteObject(deleteRequest);
            }
        }

        return rReviewPhotoMapper.deletePhotoByRestaurantsId(restaurantsId);
    }
    
    //	특정 사진 조회
    public RestaurantReviewPhotoVo findPhotoById(Long photoId) {
        return rReviewPhotoMapper.findPhotoById(photoId);
    }

    
    //  개별 사진 삭제
    public int deletePhotoById(Long photoId) {
        RestaurantReviewPhotoVo photo = rReviewPhotoMapper.findPhotoById(photoId);
        if (photo == null) 
        	return 0;

        String s3Url = photo.getFilePath();
        if (s3Url != null && !s3Url.isBlank()) {
            String s3Key = extractS3KeyFromUrl(s3Url);
            
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(bucket).key(s3Key).build();
            
            s3Client.deleteObject(deleteRequest);
        }

        return rReviewPhotoMapper.deletePhotoById(photoId);
    }
    
    private String extractS3KeyFromUrl(String url) {
        String baseUrl = "https://" + bucket + ".s3." + System.getenv("AWS_REGION") + ".amazonaws.com/";
        return url.replace(baseUrl, "");
    }
}