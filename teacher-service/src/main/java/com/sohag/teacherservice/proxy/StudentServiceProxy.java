package com.sohag.teacherservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("student-service")
public interface StudentServiceProxy {

    @GetMapping("student/list")
    List<String> getList();

}
