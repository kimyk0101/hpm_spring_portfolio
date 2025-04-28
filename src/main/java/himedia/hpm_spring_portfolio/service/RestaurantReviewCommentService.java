package himedia.hpm_spring_portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.RestaurantReviewCommentMapper;
import himedia.hpm_spring_portfolio.repository.vo.RestaurantReviewCommentVo;

@Service
public class RestaurantReviewCommentService {

	@Autowired
	private RestaurantReviewCommentMapper rCommentMapper;

	// 특정 게시물의 모든 댓글 + 대댓글 조회
	public List<RestaurantReviewCommentVo> retrieveAllComments(Long rReviewId) {
	    return rCommentMapper.retrieveAllComments(rReviewId);
	}

	// 특정 댓글 조회
	public RestaurantReviewCommentVo retrieveCommentById(Long id) {
		return rCommentMapper.retrieveCommentById(id);
	}
	
    // 특정 댓글의 대댓글 조회 
    public List<RestaurantReviewCommentVo> retrieveReplies(Long id) {
        return rCommentMapper.findRepliesByCommentId(id);
    }

    // 특정 대댓글 조회
    public RestaurantReviewCommentVo retrieveReplyById(Long id) {
        return rCommentMapper.findReplyById(id);
    }
    
    // 사용자 댓글 + 대댓글 조회 
    public List<RestaurantReviewCommentVo> retrieveMyComments(Long id) {
    	return rCommentMapper.retrieveMyComments(id);
    }
    
	// 댓글 추가
	public RestaurantReviewCommentVo createComment(RestaurantReviewCommentVo comment) {
		rCommentMapper.createComment(comment);

		// 생성된 게시글의 ID를 이용해 게시글을 다시 조회하여 반환
		Long id = comment.getId();
		return rCommentMapper.retrieveCommentById(id);
	}

	// 댓글 수정
	public RestaurantReviewCommentVo updateComment(RestaurantReviewCommentVo comment) {

		int updatedRows = rCommentMapper.updateComment(comment);

		if (updatedRows > 0) {
			return rCommentMapper.retrieveCommentById(comment.getId());
		} else {
			throw new RuntimeException("Failed to update comment");
		}
	}

	// 댓글 삭제
	public void deleteComment(Long id, Long usersId) {
		int deletedRows = rCommentMapper.deleteComment(id, usersId);

		if (deletedRows == 0) {
			throw new RuntimeException("Failed to delete comment with ID: " + id + " for user ID: " + usersId);
		}
	}
	
	// 리뷰 ID 기준으로 모든 댓글 삭제
	public void deleteCommentsByRestaurantsId(Long restaurantsId) {
	    rCommentMapper.deleteCommentsByRestaurantsId(restaurantsId);
	}
	
	// 대댓글 추가
	public RestaurantReviewCommentVo createReply(RestaurantReviewCommentVo reply) {
		rCommentMapper.createReply(reply);

	    // 생성된 대댓글의 ID를 이용해 다시 조회 후 반환
	    Long id = reply.getId();
	    return rCommentMapper.findReplyById(id);
	}

	// 대댓글 수정
	public RestaurantReviewCommentVo updateReply(RestaurantReviewCommentVo reply) {
	    int updatedRows = rCommentMapper.updateReply(reply);

	    if (updatedRows > 0) {
	        return rCommentMapper.findReplyById(reply.getId());
	    } else {
	        throw new RuntimeException("Failed to update reply");
	    }
	}

	// 대댓글 삭제
	public void deleteReply(Long replyId, Long usersId) {
	    int deletedRows = rCommentMapper.deleteReply(replyId, usersId);

	    if (deletedRows == 0) {
	        throw new RuntimeException("Failed to delete reply with ID: " + replyId + " for user ID: " + usersId);
	    }
	}
}
