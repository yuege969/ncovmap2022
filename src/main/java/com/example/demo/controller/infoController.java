package com.example.demo.controller;

import com.example.demo.entity.CityInfo;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@Controller
public class infoController {
    @Autowired
    private CityService cityService;

    @Autowired
    private ProvinceService provinceService;

    @RequestMapping("/checkCountry")
    public ModelAndView checkCountry(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        List<ProvinceInfo> allProvince = provinceService.selectAllProvince();
        request.setAttribute("allProvince", allProvince);
        mv.setViewName("checkCountry");
        return mv;
    }

    @RequestMapping(value = "/checkLocal")
    public ModelAndView checkLocal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String cityName = request.getParameter("cityName");
        if (cityName == null) {
            return mv;
        }
        ProvinceInfo nowProvince = provinceService.selectByProvinceName(cityName);
        if (nowProvince == null)
        {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！输入的省份名不正确');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
        }
        else
        {
            List<CityInfo> allProvince = cityService.selectByProvinceId(nowProvince.getId());
            request.setAttribute("allCity", allProvince);
            request.setAttribute("provinceName", cityName);
            mv.setViewName("checkLocal");
        }
        return mv;
    }
}