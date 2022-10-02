package com.example.demo.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping(value = "/map")
public class mapController {
    @RequestMapping(value = "/checkProvince")
    public void checkProvince(HttpServletRequest request){
        String name = request.getParameter("province");
        System.out.println("点击省份："+name);
    }
}
