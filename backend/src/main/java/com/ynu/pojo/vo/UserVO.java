package com.ynu.pojo.vo;

import com.ynu.pojo.entity.Order;
import com.ynu.pojo.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private String username;
    private String email;
    private String phone;
    private String realName;
    private String emergencyContact;
    private String emergencyPhone;
    private List<Order> orders;
    private Date createTime;
}
