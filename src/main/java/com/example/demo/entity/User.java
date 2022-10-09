package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
@Data注解：提供了类的get、set、equals、hashCode、canEqual、toString方法
@AllArgsConstructor：注在类上，提供类的全参构造
@NoArgsConstructor：注在类上，提供类的无参构造
*/
public class User {
    private Integer uid;
    private String uname;
    private String password;
    private String mail;
    private String phone;
    private Timestamp timeOfRegister;
    private Timestamp timeOfLastLogin;
    private int actived;
    private String securityCode;
}
