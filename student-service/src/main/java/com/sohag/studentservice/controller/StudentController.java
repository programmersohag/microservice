package com.sohag.studentservice.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/list")
    public List<String> findStudentList() {
        List<String> myList = new ArrayList<>();
        myList.add("Shohag");
        myList.add("Rakib");
        myList.add("Masum");
        myList.add("Setu");
        myList.add("Shovon");
        System.out.println(myList.toString());
        return myList;
    }


}
