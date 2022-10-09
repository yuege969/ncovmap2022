package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceInfo {

    //主键
    private Integer id;

    //省名
    private String provinceName;

    //当前确诊
    private Long currentConfirmedCount;

    //总共确诊
    private Long confirmedCount;

    //治愈
    private Long curedCount;

    //死亡人数
    private Long deadCount;

    //可疑人数
    private Long suspectedCount;

}

