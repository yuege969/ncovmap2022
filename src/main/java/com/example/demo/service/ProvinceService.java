package com.example.demo.service;

import com.example.demo.entity.ProvinceInfo;

import java.util.List;

public interface ProvinceService {
    int insert(ProvinceInfo provinceInfo);

    int update(ProvinceInfo provinceInfo);

    ProvinceInfo selectByProvinceName(String provinceName);

    List<ProvinceInfo> selectAllProvince();

}
