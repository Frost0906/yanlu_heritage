package com.ynu.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
 public class Course {
   private Long courseId;
   private String title;
   private CourseCategory category;
   private String introduction;
   private BigDecimal price;
   private Date startDate;
   private Date endDate;
   private Integer maxParticipants;
   private String coverImage;
   private List<Schedule> schedules;
}
