package himedia.hpm_spring_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.CommunityCommentMapper;
import himedia.hpm_spring_portfolio.repository.vo.CommunityCommentVo;

@Service
public class CommunityCommentService {

	@Autowired
	private CommunityCommentMapper cCommentMapper;

	// 특정 게시물의 모든 댓글 + 대댓글 조회
	public List<CommunityCommentVo> retrieveAllComments(Long communityId) {
	    return cCommentMapper.retrieveAllComments(communityId);
	}


	// 특정 댓글 조회
	public CommunityCommentVo retrieveCommentById(Long id) {
		return cCommentMapper.retrieveCommentById(id);
	}
	
    // 특정 댓글의 대댓글 조회 
    public List<CommunityCommentVo> retrieveReplies(Long id) {
        return cCommentMapper.findRepliesByCommentId(id);
    }

    // 특정 대댓글 조회
    public CommunityCommentVo retrieveReplyById(Long id) {
        return cCommentMapper.findReplyById(id);
    }
    
    // 사용자 댓글 + 대댓글 조회 
    public List<CommunityCommentVo> retrieveMyComments(Long id) {
    	return cCommentMapper.retrieveMyComments(id);
    }
    
	// 댓글 추가
	public CommunityCommentVo createComment(CommunityCommentVo comment) {
		cCommentMapper.createComment(comment);

		// 생성된 게시글의 ID를 이용해 게시글을 다시 조회하여 반환
		Long id = comment.getId();
		return cCommentMapper.retrieveCommentById(id);
	}

	// 댓글 수정
	public CommunityCommentVo updateComment(CommunityCommentVo comment) {

		int updatedRows = cCommentMapper.updateComment(comment);

		if (updatedRows > 0) {
			return cCommentMapper.retrieveCommentById(comment.getId());
		} else {
			throw new RuntimeException("Failed to update comment");
		}
	}

	// 댓글 삭제
	public void deleteComment(Long id, Long usersId) {
		int deletedRows = cCommentMapper.deleteComment(id, usersId);

		if (deletedRows == 0) {
			throw new RuntimeException("Failed to delete comment with ID: " + id + " for user ID: " + usersId);
		}
	}
	
	// 커뮤니티 ID 기준으로 모든 댓글 삭제
	public void deleteCommentsByCommunitiesId(Long communitiesId) {
	    cCommentMapper.deleteCommentsByCommunitiesId(communitiesId);
	}
	
	// 대댓글 추가
	public CommunityCommentVo createReply(CommunityCommentVo reply) {
	    cCommentMapper.createReply(reply);

	    // 생성된 대댓글의 ID를 이용해 다시 조회 후 반환
	    Long id = reply.getId();
	    return cCommentMapper.findReplyById(id);
	}

	// 대댓글 수정
	public CommunityCommentVo updateReply(CommunityCommentVo reply) {
	    int updatedRows = cCommentMapper.updateReply(reply);

	    if (updatedRows > 0) {
	        return cCommentMapper.findReplyById(reply.getId());
	    } else {
	        throw new RuntimeException("Failed to update reply");
	    }
	}

	// 대댓글 삭제
	public void deleteReply(Long replyId, Long usersId) {
	    int deletedRows = cCommentMapper.deleteReply(replyId, usersId);

	    if (deletedRows == 0) {
	        throw new RuntimeException("Failed to delete reply with ID: " + replyId + " for user ID: " + usersId);
	    }
	}
	
}
