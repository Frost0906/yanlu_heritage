package com.ynu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ynu.pojo.entity.Course;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {
    List<Course> selectByPage(IPage<Course> page);
}
