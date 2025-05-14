package com.ynu.pojo.entity;

import com.ynu.pojo.enums.PaymentMethod;
import com.ynu.pojo.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderId;
    private User user;
    private Course course;
    private Date orderTime;
    private PaymentStatus status;
    private BigDecimal totalAmount;
    private Payment payment;
}
