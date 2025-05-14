package com.ynu.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private Long scheduleId;
    private Course course;
    private Integer dayNumber;
    private String timeRange;
    private String location;
    private String activityContent;
    private String learningOutcomes;
}
