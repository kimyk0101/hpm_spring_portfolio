package himedia.hpm_spring_portfolio.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import himedia.hpm_spring_portfolio.mappers.ClubCommentMapper;
import himedia.hpm_spring_portfolio.repository.vo.ClubCommentVo;

@Service
public class ClubCommentService {

    @Autowired
    private ClubCommentMapper clubCommentMapper;

    public List<ClubCommentVo> findCommentsByClubsId(Long clubsId) {
        return clubCommentMapper.findCommentsByClubsId(clubsId);
    }

    public ClubCommentVo createComment(ClubCommentVo comment) {
        comment.setUpdateDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        clubCommentMapper.insertComment(comment);
        return comment;
    }

    public ClubCommentVo updateComment(ClubCommentVo comment) {
        comment.setUpdateDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        clubCommentMapper.updateComment(comment);
        return comment;
    }

    public void deleteComment(Long id) {
    	clubCommentMapper.deleteComment(id);
    }
}