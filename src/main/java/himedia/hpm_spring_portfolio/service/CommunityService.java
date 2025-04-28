package himedia.hpm_spring_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.CommunityCommentMapper;
import himedia.hpm_spring_portfolio.mappers.CommunityMapper;
import himedia.hpm_spring_portfolio.repository.vo.CommunityVo;

@Service
public class CommunityService {

	@Autowired
	private CommunityMapper communityMapper;
	
	@Autowired
	private CommunityPhotoService communityPhotoService;
	
	@Autowired
	private CommunityCommentMapper cCommentMapper;
	
	// 모든 커뮤니티 게시글 조회
	public List<CommunityVo> retrieveAllCommunities() {
		return communityMapper.retrieveAllCommunities();
	}

	// 특정 게시글 조회
	public CommunityVo retrieveCommunityById(Long id) {
		return communityMapper.retrieveCommunityById(id);
	}

	// 사용자의 게시글 조회
	public List<CommunityVo> retrieveMyCommunities(Long id) {
		return communityMapper.retrieveMyCommunities(id);
	}

	// [경민] 키워드 기반 게시글 조회
	public List<CommunityVo> retrieveCommunitiesByKeyword(String keyword) {
		String pattern = "(^|[^가-힣a-zA-Z0-9])" + keyword + "([^가-힣a-zA-Z0-9]|$)";
	    return communityMapper.retrieveCommunitiesByKeyword(pattern);
	}

	// 게시글 생성
	public CommunityVo createCommunity(CommunityVo community) {
		// 게시글 생성
		communityMapper.createCommunity(community);

		// 생성된 게시글의 ID를 이용해 게시글을 다시 조회하여 반환
		Long id = community.getId();
		return communityMapper.retrieveCommunityById(id);
	}

	// 게시글 일부 수정 (PATCH)
	public CommunityVo updateCommunity(CommunityVo community) {
		// 커뮤니티 업데이트 (일부 필드 수정)
		int updatedRows = communityMapper.updateCommunity(community);

		if (updatedRows > 0) {
			return communityMapper.retrieveCommunityById(community.getId());
		} else {
			throw new RuntimeException("Failed to update community");
		}
	}

	// 게시글 삭제
	public void deleteCommunity(Long id, Long usersId) {
		
		// 1. 댓글 먼저 삭제
		cCommentMapper.deleteCommentsByCommunitiesId(id);
		
		// 2. 이미지 삭제
		communityPhotoService.deletePhotoByCommunityId(id);
		
		// 3. 리뷰 삭제
		int deletedRows = communityMapper.deleteCommunity(id, usersId);

		if (deletedRows == 0) {
			throw new RuntimeException("Failed to delete community with ID: " + id + " for user ID: " + usersId);
		}
	}

	// 조회수 증가 메서드
	public void incrementViews(Long id) {
		communityMapper.incrementViews(id); // Mapper 메서드 호출
	}
}
