package com.ynu.pojo.entity;

import com.ynu.pojo.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private String emergencyContact;
    private String emergencyPhone;
    private UserRole role = UserRole.USER;
    private List<Order> orders;
    private Date createTime;
}
