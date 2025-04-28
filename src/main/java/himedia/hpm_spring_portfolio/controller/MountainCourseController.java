package himedia.hpm_spring_portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import himedia.hpm_spring_portfolio.repository.vo.MountainCourseVo;
import himedia.hpm_spring_portfolio.service.MountainCourseService;

@RestController
@RequestMapping("/api/mountains")
public class MountainCourseController {

	@Autowired
	private MountainCourseService mountainCourseService;

	 @GetMapping("/{id}/courses")
	    public List<MountainCourseVo> getCoursesByMountainId(@PathVariable Long id) {
	        try {            
	            return mountainCourseService.retrieveMountainById(id);
	        } catch (Exception e) {	        	
	            throw new ResponseStatusException(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                "코스 정보 조회 실패: " + e.getMessage()
	            );
	        }
	    }
}
