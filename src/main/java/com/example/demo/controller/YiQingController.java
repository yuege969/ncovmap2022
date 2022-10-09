package com.example.demo.controller;

import com.example.demo.entity.CityInfo;
import com.example.demo.entity.ProvinceInfo;
import com.example.demo.service.CityService;
import com.example.demo.service.ProvinceService;
import com.example.demo.service.YiQingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YiQingController {

    @Autowired
    private CityService cityService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private YiQingService yiQingService;

    @GetMapping("/initMap")
    public void initMap(){
        //  初始化疫情数据
        yiQingService.getCovid("");
    }
}
