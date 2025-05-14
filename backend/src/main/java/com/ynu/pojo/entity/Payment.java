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
public class Payment {
    private String paymentId;
    private Order order;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private Date paymentTime;
    private PaymentStatus status;
}
