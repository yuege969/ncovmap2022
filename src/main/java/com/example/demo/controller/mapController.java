package com.example.demo.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.CityInfo;
import com.example.demo.entity.ProvinceInfo;
import com.example.demo.service.CityService;
import com.example.demo.service.ProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/map")
public class mapController {

    @Autowired
    private CityService cityService;

    @Autowired
    private ProvinceService provinceService;


    /**
     * 获取省份的疫情数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectProvince")
    public String selectProvince(HttpServletRequest request){
        String name = request.getParameter("province");
        System.out.println("获取的省份是 "+name);

        ProvinceInfo provinceInfo = provinceService.selectByProvinceName(name);

        String s = JSON.toJSONString(provinceInfo);
        System.out.println(s);
        List<CityInfo> cityInfos = cityService.selectByProvinceId(provinceInfo.getId());
        String s1 = JSON.toJSONString(cityInfos);
        System.out.println(s1);

        return s+s1;
    }

    /**
     * 获取城市的疫情数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectCity")
    public String selectCity(HttpServletRequest request){
        String name=request.getParameter("city");
        System.out.println("获取的城市是 "+name);
        CityInfo cityInfo = cityService.selectByName(name);
        System.out.println(cityInfo);
        String s = JSON.toJSONString(cityInfo);

        return s;

    }

}
