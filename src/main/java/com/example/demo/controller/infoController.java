package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.CityInfo;
import com.example.demo.entity.ProvinceInfo;
import com.example.demo.service.CityService;
import com.example.demo.service.ProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        mv.setViewName("checkLocal");
        return mv;
        //TODO:实现获取所有省份各个城市的信息
    }
    @ResponseBody
    @RequestMapping("/getCities")
    public JSONArray getCities(HttpServletRequest request){
        List<ProvinceInfo> allProvince = provinceService.selectAllProvince();
        JSONArray array = new JSONArray();
        for(ProvinceInfo provinceInfo: allProvince){
            List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
            String name = provinceInfo.getProvinceName();
            ProvinceInfo provinceInfo1 = provinceService.selectByProvinceName(name);
            List<CityInfo> cityInfos = cityService.selectByProvinceId(provinceInfo1.getId());
            JSONObject obj;
            JSONObject allObj;
            allObj = new JSONObject();
            for(CityInfo cityInfo:cityInfos){
                obj = new JSONObject();
                obj.put("cityName",cityInfo.getCityName());
                obj.put("currentConfirmedCount",cityInfo.getCurrentConfirmedCount());
                obj.put("confirmedCount",cityInfo.getConfirmedCount());
                obj.put("curedCount",cityInfo.getCuredCount());
                obj.put("deadCount",cityInfo.getDeadCount());
                obj.put("suspectedCount",cityInfo.getSuspectedCount());
                jsonObjects.add(obj);
            }
            allObj.put(name,jsonObjects);
            array.add(allObj);
        }
        return array;
    }
}

