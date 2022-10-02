package com.example.demo.service;

import com.example.demo.entity.CityInfo;

import java.util.List;

public interface CityService {
    int insert(CityInfo cityInfo);

    int update(CityInfo cityInfo);

    CityInfo selectByName(String cityName);

    List<CityInfo> selectByProvinceId(Integer id);

}
