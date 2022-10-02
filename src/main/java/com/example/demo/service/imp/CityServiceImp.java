package com.example.demo.service.imp;

import com.example.demo.dao.CityDao;
import com.example.demo.entity.CityInfo;
import com.example.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@SuppressWarnings("all")

public class CityServiceImp implements CityService {

    @Autowired
    private CityDao cityDao;

    @Override
    public int insert(CityInfo cityInfo) {
        return cityDao.insert(cityInfo);
    }

    @Override
    public int update(CityInfo cityInfo) {
        return cityDao.update(cityInfo);
    }

    @Override
    public CityInfo selectByName(String cityName) {
        return cityDao.selectByName(cityName);
    }

    @Override
    public List<CityInfo> selectByProvinceId(Integer id) {
        return cityDao.selectByProvinceId(id);
    }
}
