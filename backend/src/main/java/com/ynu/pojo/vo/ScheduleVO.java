package com.ynu.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleVO {
    private CourseVO course;
    private Integer dayNumber;
    private String timeRange;
    private String location;
    private String activityContent;
    private String learningOutcomes;
}
