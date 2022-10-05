package com.example.demo.controller;

import com.example.demo.entity.ProvinceInfo;
import com.example.demo.service.CityService;
import com.example.demo.service.ProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class infoController {
    @Autowired
    private CityService cityService;

    @Autowired
    private ProvinceService provinceService;
    @RequestMapping("/checkCountry")
    public ModelAndView checkCountry(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        List<ProvinceInfo> allProvince = provinceService.selectAllProvince();
        request.setAttribute("allProvince",allProvince);
        mv.setViewName("checkCountry");
        return mv;
    }

    @RequestMapping("/checkLocal")
    public ModelAndView checkLocal(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
//        List<ProvinceInfo> allProvince = provinceService.selectAllProvince();
//        request.setAttribute("allProvince",allProvince);
        mv.setViewName("checkLocal");
        return mv;
        //TODO:实现获取所有省份各个城市的信息
    }
}

