package himedia.hpm_spring_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.MountainReviewCommentMapper;
import himedia.hpm_spring_portfolio.repository.vo.MountainReviewCommentVo;

@Service
public class MountainReviewCommentService {

	@Autowired
	private MountainReviewCommentMapper mCommentMapper;

	// 특정 게시물의 모든 댓글 + 대댓글 조회
	public List<MountainReviewCommentVo> retrieveAllComments(Long mReviewId) {
	    return mCommentMapper.retrieveAllComments(mReviewId);
	}

	// 특정 댓글 조회
	public MountainReviewCommentVo retrieveCommentById(Long id) {
		return mCommentMapper.retrieveCommentById(id);
	}
	
    // 특정 댓글의 대댓글 조회 
    public List<MountainReviewCommentVo> retrieveReplies(Long id) {
        return mCommentMapper.findRepliesByCommentId(id);
    }

    // 특정 대댓글 조회
    public MountainReviewCommentVo retrieveReplyById(Long id) {
        return mCommentMapper.findReplyById(id);
    }
    
 // 사용자 댓글 + 대댓글 조회 
    public List<MountainReviewCommentVo> retrieveMyComments(Long id) {
    	return mCommentMapper.retrieveMyComments(id);
    }
    
	// 댓글 추가
	public MountainReviewCommentVo createComment(MountainReviewCommentVo comment) {
		mCommentMapper.createComment(comment);

		// 생성된 게시글의 ID를 이용해 게시글을 다시 조회하여 반환
		Long id = comment.getId();
		return mCommentMapper.retrieveCommentById(id);
	}

	// 댓글 수정
	public MountainReviewCommentVo updateComment(MountainReviewCommentVo comment) {

		int updatedRows = mCommentMapper.updateComment(comment);

		if (updatedRows > 0) {
			return mCommentMapper.retrieveCommentById(comment.getId());
		} else {
			throw new RuntimeException("Failed to update comment");
		}
	}

	// 댓글 삭제
	public void deleteComment(Long id, Long usersId) {
		int deletedRows = mCommentMapper.deleteComment(id, usersId);

		if (deletedRows == 0) {
			throw new RuntimeException("Failed to delete comment with ID: " + id + " for user ID: " + usersId);
		}
	}
	
	// 리뷰 ID 기준으로 모든 댓글 삭제
	public void deleteCommentsByReviewsId(Long reviewsId) {
	    mCommentMapper.deleteCommentsByReviewsId(reviewsId);
	}
	
	// 대댓글 추가
	public MountainReviewCommentVo createReply(MountainReviewCommentVo reply) {
		mCommentMapper.createReply(reply);

	    // 생성된 대댓글의 ID를 이용해 다시 조회 후 반환
	    Long id = reply.getId();
	    return mCommentMapper.findReplyById(id);
	}

	// 대댓글 수정
	public MountainReviewCommentVo updateReply(MountainReviewCommentVo reply) {
	    int updatedRows = mCommentMapper.updateReply(reply);

	    if (updatedRows > 0) {
	        return mCommentMapper.findReplyById(reply.getId());
	    } else {
	        throw new RuntimeException("Failed to update reply");
	    }
	}

	// 대댓글 삭제
	public void deleteReply(Long replyId, Long usersId) {
	    int deletedRows = mCommentMapper.deleteReply(replyId, usersId);

	    if (deletedRows == 0) {
	        throw new RuntimeException("Failed to delete reply with ID: " + replyId + " for user ID: " + usersId);
	    }
	}
}
