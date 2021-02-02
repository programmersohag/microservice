package com.sohag.teachersservice.controller;

import com.sohag.teachersservice.proxy.StudentServiceProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final StudentServiceProxy studentServiceProxy;

    public TeacherController(StudentServiceProxy studentServiceProxy) {
        this.studentServiceProxy = studentServiceProxy;
    }

    @GetMapping("/list")
    List<String> findAll() {
        return studentServiceProxy.getList();
    }

}
