package himedia.hpm_spring_portfolio.mappers;

import java.util.List;

import himedia.hpm_spring_portfolio.repository.vo.UserVo;

public interface UserMapper {

//	<select id="retrieveAllUsers" resultType="UserVo">	// 전체 유저 조회
	List<UserVo> retrieveAllUsers();
	
//	<select id="retrieveUserById" parameterType="Long" resultType="UserVo">	// 특정 유저 조회
	UserVo retrieveUserById(Long id);
	
//	<insert id="registerUser" parameterType="UserVo">	// 회원가입
	int registerUser(UserVo user);
	
//	<select id="findByUserId" resultMap="UserVo">		// 로그인 
	UserVo findByUserId(String userId);
	
//	<update id="updateUserFields" parameterType="UserVo">		//	유저 정보 수정
	int updateUserFields(UserVo user);
	
//	<update id="updatePassword">						//	유저 정보 수정: 비밀번호 변경
	void updatePassword(Long id, String password);
	
//	<delete id="deleteUser" parameterType="Long">		//	유저 삭제
	int deleteUser(Long id);
	
//	<select id="countByUserId" parameterType="String" resultType="int">	//	아이디 중복체크
	int countByUserId(String userId);
	
//	<select id="countByNickname" parameterType="String" resultType="int">	//	닉네임 중복체크
	int countByNickname(String nickname);
	
//	<select id="countByEmail" parameterType="String" resultType="int">	//	이메일 중복체크
	int countByEmail(String email);
}
