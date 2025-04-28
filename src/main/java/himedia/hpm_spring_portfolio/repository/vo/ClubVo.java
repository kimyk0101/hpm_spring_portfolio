package himedia.hpm_spring_portfolio.repository.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClubVo {
    private Long id;
    private String name;
    private String url;
    private String title;
    private String content;
    private LocalDateTime updateDate;
    private Integer views;
    private Long usersId;
}