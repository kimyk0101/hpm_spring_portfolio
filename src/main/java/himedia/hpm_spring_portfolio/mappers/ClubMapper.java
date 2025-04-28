package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import himedia.hpm_spring_portfolio.repository.vo.ClubVo;

@Mapper
public interface ClubMapper {

    @Select("SELECT * FROM clubs")
    List<ClubVo> findAllClubs();

    @Select("SELECT * FROM clubs WHERE id = #{id}")
    ClubVo findClubById(Long id);

    @Select("SELECT * FROM clubs WHERE users_id = #{usersId}")
    List<ClubVo> findClubsByUsersId(Long usersId);

    @Insert("INSERT INTO clubs (name, url, content, update_date, users_id, views, title) " +
            "VALUES (#{name}, #{url}, #{content}, #{updateDate}, #{usersId}, #{views}, #{title})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertClub(ClubVo club);

    @Update("UPDATE clubs SET name = #{name}, url = #{url}, content = #{content}, update_date = #{updateDate}, title = #{title} " +
            "WHERE id = #{id}")
    void updateClub(ClubVo club);

    @Delete("DELETE FROM clubs WHERE id = #{id} AND users_id = #{usersId}")
    void deleteClub(@Param("id") Long id, @Param("usersId") Long usersId);

    @Update("UPDATE clubs SET views = views + 1 WHERE id = #{id}")
    void incrementViews(Long id);
}