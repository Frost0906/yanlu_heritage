package com.ynu.controller;

import com.ynu.pojo.entity.Course;
import com.ynu.service.ICourseService;
import com.ynu.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @GetMapping("/list")
    public Result list() {
        List<Course> list = courseService.list();
        return Result.ok(list);
    }


}
