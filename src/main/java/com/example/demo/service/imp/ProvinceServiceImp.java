package com.example.demo.service.imp;

import com.example.demo.dao.ProvinceDao;
import com.example.demo.entity.ProvinceInfo;
import com.example.demo.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@SuppressWarnings("all")
public class ProvinceServiceImp implements ProvinceService {

    @Autowired
    private ProvinceDao provinceDao;

    @Override
    public int insert(ProvinceInfo provinceInfo) {

        return provinceDao.insert(provinceInfo);
    }

    @Override
    public int update(ProvinceInfo provinceInfo) {
        return provinceDao.update(provinceInfo);
    }

    @Override
    public ProvinceInfo selectByProvinceName(String provinceName) {
        return provinceDao.selectByProvinceName(provinceName);
    }

    @Override
    public List<ProvinceInfo> selectAllProvince() {
        return provinceDao.selectAllProvince();
    }
}
