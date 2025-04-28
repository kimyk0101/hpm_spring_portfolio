package himedia.hpm_spring_portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.RestaurantReviewLikeMapper;
import himedia.hpm_spring_portfolio.repository.vo.RestaurantReviewLikeVo;

@Service
public class RestaurantReviewLikeService {

	@Autowired
	private RestaurantReviewLikeMapper rLikeMapper;

	// 좋아요 토글
	public boolean toggleLike(RestaurantReviewLikeVo vo) {
		int exists = rLikeMapper.exists(vo);

		if (exists == 0) {
			// 새로 생성
			return rLikeMapper.insertLike(vo) > 0;
		} else {
			boolean isLiked = rLikeMapper.isLiked(vo) > 0;

			if (isLiked) {
				return rLikeMapper.cancelLike(vo) > 0;
			} else {
				return rLikeMapper.updateLike(vo) > 0;
			}
		}
	}

	// 리뷰별 좋아요 총 개수
	public int getLikeCount(Long restaurantsId) {
		return rLikeMapper.selectLikeCount(restaurantsId);
	}

	// 사용자가 이 리뷰에 좋아요를 눌렀는지 확인
	public boolean isLiked(RestaurantReviewLikeVo rLikeVo) {
	    Integer result = rLikeMapper.isLiked(rLikeVo);
	    return result != null && result == 1;
	}
}
