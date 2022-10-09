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
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping(value="/getData")
    public JSONArray getData(HttpServletRequest request){
        List<ProvinceInfo> allProvince = provinceService.selectAllProvince();
        //JSONObject json=JSONObject.fromObject(allProvince);

        String jsonStr = null;
        JSONArray array = new JSONArray();
        JSONObject obj;
        for(ProvinceInfo provinceInfo:allProvince){
            obj = new JSONObject();
            obj.put("provinceName",provinceInfo.getProvinceName());
            obj.put("currentConfirmedCount",provinceInfo.getCurrentConfirmedCount());
            obj.put("confirmedCount",provinceInfo.getConfirmedCount());
            obj.put("curedCount",provinceInfo.getCuredCount());
            obj.put("deadCount",provinceInfo.getDeadCount());
            obj.put("suspectedCount",provinceInfo.getSuspectedCount());
            array.add(obj);
        }
        System.out.println(array.toString());
        return array;
    }

    /**
     * 获取省份的疫情数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/clickProvince")
    public JSONArray clickProvince(HttpServletRequest request){
        String name = request.getParameter("province");
        System.out.println("鼠标点击获取的省份是 "+name);
        ProvinceInfo provinceInfo = provinceService.selectByProvinceName(name);
        String s = JSON.toJSONString(provinceInfo);
//        System.out.println(s);
        List<CityInfo> cityInfos = cityService.selectByProvinceId(provinceInfo.getId());
        String jsonStr = null;
        JSONArray array = new JSONArray();
        JSONObject obj;
        for(CityInfo cityInfo:cityInfos){
            obj = new JSONObject();
            obj.put("cityName",cityInfo.getCityName());
            obj.put("currentConfirmedCount",cityInfo.getCurrentConfirmedCount());
            obj.put("confirmedCount",cityInfo.getConfirmedCount());
            obj.put("curedCount",cityInfo.getCuredCount());
            obj.put("deadCount",cityInfo.getDeadCount());
            obj.put("suspectedCount",cityInfo.getSuspectedCount());
            array.add(obj);
        }
        System.out.println(array.toString());
        return array;
    }
}
