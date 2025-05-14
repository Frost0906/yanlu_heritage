package com.ynu.pojo.dto;

import com.ynu.pojo.entity.CourseCategory;
import com.ynu.pojo.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long courseId;
    private String title;
    private CourseCategory category;
    private BigDecimal price;
    private Date startDate;
    private Date endDate;
    private Integer maxParticipants;
    private String coverImage;
    private List<ScheduleDTO> schedules;

}
