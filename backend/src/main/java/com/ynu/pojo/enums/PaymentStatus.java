package com.ynu.pojo.enums;

public enum PaymentStatus {
    PENDING,    // 待支付
    PAID,       // 已支付
    REFUNDING,  // 退款中
    REFUNDED,   // 已退款
    FAILED,     // 支付失败
    CANCELLED   // 已取消
}