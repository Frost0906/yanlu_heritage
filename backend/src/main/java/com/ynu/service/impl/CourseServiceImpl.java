package com.ynu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ynu.mapper.CourseMapper;
import com.ynu.pojo.entity.Course;
import com.ynu.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> list() {
        IPage<Course> page = new Page<>(1, 3);
        return courseMapper.selectByPage(page);
    }
}
