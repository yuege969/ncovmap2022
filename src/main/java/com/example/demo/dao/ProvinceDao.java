package com.example.demo.dao;

import com.example.demo.entity.ProvinceInfo;

import java.util.List;

public interface ProvinceDao {

    int insert(ProvinceInfo provinceInfo);

    int update(ProvinceInfo provinceInfo);

    ProvinceInfo selectByProvinceName(String provinceName);

    List<ProvinceInfo> selectAllProvince();

}
