package com.ynu.pojo.vo;

import com.ynu.pojo.entity.CourseCategory;
import com.ynu.pojo.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseVO {
    private String title;
    private String categoryName;
    private String introduction;
    private BigDecimal price;
    private Date startDate;
    private Date endDate;
    private Integer maxParticipants;
    private String coverImage;
    private List<ScheduleVO> schedules;
}
