package himedia.hpm_spring_portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.MountainReviewLikeMapper;
import himedia.hpm_spring_portfolio.repository.vo.MountainReviewLikeVo;

@Service
public class MountainReviewLikeService {

	@Autowired
	private MountainReviewLikeMapper mLikeMapper;

	// 좋아요 토글
	public boolean toggleLike(MountainReviewLikeVo vo) {
		int exists = mLikeMapper.exists(vo);

		if (exists == 0) {
			// 새로 생성
			return mLikeMapper.insertLike(vo) > 0;
		} else {
			boolean isLiked = mLikeMapper.isLiked(vo) > 0;

			if (isLiked) {
				return mLikeMapper.cancelLike(vo) > 0;
			} else {
				return mLikeMapper.updateLike(vo) > 0;
			}
		}
	}

	// 리뷰별 좋아요 총 개수
	public int getLikeCount(Long reviewsId) {
		return mLikeMapper.selectLikeCount(reviewsId);
	}

	// 사용자가 이 리뷰에 좋아요를 눌렀는지 확인
	public boolean isLiked(MountainReviewLikeVo mLikeVo) {
	    Integer result = mLikeMapper.isLiked(mLikeVo);
	    return result != null && result == 1;
	}
}
