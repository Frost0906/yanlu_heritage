package com.ynu.pojo.dto;

import com.ynu.pojo.entity.Order;
import com.ynu.pojo.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private UserRole role = UserRole.USER;
    private List<Order> orders;
}
