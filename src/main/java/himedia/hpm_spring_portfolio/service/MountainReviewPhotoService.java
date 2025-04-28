package himedia.hpm_spring_portfolio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import himedia.hpm_spring_portfolio.mappers.MountainReviewPhotoMapper;
import himedia.hpm_spring_portfolio.repository.vo.MountainReviewPhotoVo;
import himedia.hpm_spring_portfolio.util.S3PathUtil;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class MountainReviewPhotoService {

	@Autowired
	private MountainReviewPhotoMapper mReviewPhotoMapper;

	@Autowired
	private S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Transactional
	public List<String> insertPhoto(Long reviewsId, MultipartFile[] photos) {
		List<String> s3Urls = new ArrayList<>();

		try {
			for (MultipartFile photo : photos) {
				String s3Key = S3PathUtil.getMountainReviewPhotoPath(reviewsId, photo.getOriginalFilename());

				PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(s3Key)
						.contentType(photo.getContentType()).build();

				s3Client.putObject(putObjectRequest,
						RequestBody.fromInputStream(photo.getInputStream(), photo.getSize()));

				String s3Url = "https://" + bucket + ".s3." + System.getenv("AWS_REGION") + ".amazonaws.com/" + s3Key;

				// 3. DB 업데이트
				MountainReviewPhotoVo mReviewPhotoVo = new MountainReviewPhotoVo();
				mReviewPhotoVo.setMountainReviewsId(reviewsId);
				mReviewPhotoVo.setFileName(photo.getOriginalFilename());
				mReviewPhotoVo.setFilePath(s3Url); // 프론트에서 접근할 URL 기준 경로
				mReviewPhotoVo.setUpdateDate(new Date());

				mReviewPhotoMapper.insertPhoto(mReviewPhotoVo);
				s3Urls.add(s3Url);
			}

			return s3Urls;

		} catch (Exception e) {
			// 에러 로그
			System.err.println("등산후기 사진 업로드 실패: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	// 등산후기 사진 조회
	public List<MountainReviewPhotoVo> selectAllPhotoByReviewsId(Long reviewsId) {
		return mReviewPhotoMapper.selectAllPhotoByReviewsId(reviewsId);
	}

	// 등산후기 사진 삭제
	@Transactional
	public int deletePhotoByReviewsId(Long reviewsId) {
		List<MountainReviewPhotoVo> photoList = mReviewPhotoMapper.selectAllPhotoByReviewsId(reviewsId);

		for (MountainReviewPhotoVo photo : photoList) {
			String s3Url = photo.getFilePath();
			if (s3Url != null && !s3Url.isBlank()) {
				String s3Key = extractS3KeyFromUrl(s3Url);

				DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(bucket).key(s3Key).build();

				s3Client.deleteObject(deleteRequest);
			}
		}

		return mReviewPhotoMapper.deletePhotoByReviewsId(reviewsId);
	}

	// 특정 사진 개별 조회
	public MountainReviewPhotoVo findPhotoById(Long photoId) {
		return mReviewPhotoMapper.findPhotoById(photoId);
	}

	// 특정 사진 개별 삭제
	public int deletePhotoById(Long photoId) {
		MountainReviewPhotoVo photo = findPhotoById(photoId);
		if (photo == null)
			return 0;

		String s3Url = photo.getFilePath();
		if (s3Url != null && !s3Url.isBlank()) {
			String s3Key = extractS3KeyFromUrl(s3Url);

			DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(bucket).key(s3Key).build();

			s3Client.deleteObject(deleteRequest);
		}

		return mReviewPhotoMapper.deletePhotoById(photoId);
	}

	private String extractS3KeyFromUrl(String url) {
		String baseUrl = "https://" + bucket + ".s3." + System.getenv("AWS_REGION") + ".amazonaws.com/";
		return url.replace(baseUrl, "");
	}

}