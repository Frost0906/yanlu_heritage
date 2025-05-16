package com.ynu.pojo.entity;

import com.ynu.pojo.enums.CourseCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategory {
    private Long categoryId;
    private CourseCategoryEnum name;
    private String icon;
}
