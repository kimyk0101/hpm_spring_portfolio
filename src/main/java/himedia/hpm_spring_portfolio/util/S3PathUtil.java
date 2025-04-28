package himedia.hpm_spring_portfolio.util;

import java.util.UUID;

public class S3PathUtil {

    // 확장자 추출
    private static String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex != -1) ? filename.substring(dotIndex + 1) : "";
    }

    // ✅ 유저 프로필 사진 경로
    public static String getUserProfilePath(Long userId, String originalFilename) {
        String extension = getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return String.format("user-profile/%d/%s.%s", userId, uuid, extension);
    }

    // ✅ 커뮤니티 사진 경로
    public static String getCommunityPhotoPath(Long communitiesId, String originalFilename) {
        String extension = getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return String.format("community/%d/%s.%s", communitiesId, uuid, extension);
    }

    // ✅ 맛집 후기 사진 경로
    public static String getFoodReviewPhotoPath(Long restaurantsId, String originalFilename) {
        String extension = getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return String.format("food-review/%d/%s.%s", restaurantsId, uuid, extension);
    }

    // ✅ 등산 후기 사진 경로
    public static String getMountainReviewPhotoPath(Long reviewsId, String originalFilename) {
        String extension = getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return String.format("mountain-review/%d/%s.%s", reviewsId, uuid, extension);
    }
}
