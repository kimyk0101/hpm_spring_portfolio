package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import himedia.hpm_spring_portfolio.repository.vo.ClubCommentVo;

@Mapper
public interface ClubCommentMapper {

    @Select("SELECT cc.id, cc.content, cc.update_date AS updateDate, cc.clubs_id AS clubsId, cc.users_id AS usersId, u.nickname " +
            "FROM club_comments cc " +
            "JOIN users u ON cc.users_id = u.id " +
            "WHERE cc.clubs_id = #{clubsId}")
    List<ClubCommentVo> findCommentsByClubsId(Long clubsId);

    @Insert("INSERT INTO club_comments (content, update_date, clubs_id, users_id) " +
            "VALUES (#{content}, #{updateDate}, #{clubsId}, #{usersId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertComment(ClubCommentVo comment);

    @Update("UPDATE club_comments SET content = #{content}, update_date = #{updateDate} WHERE id = #{id}")
    void updateComment(ClubCommentVo comment);

    @Delete("DELETE FROM club_comments WHERE id = #{id}")
    void deleteComment(Long id);
}