package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单定时任务类
 */
@Slf4j
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")    // 每分钟触发一次
    public void processTimeourOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        // 查询未支付订单超过 15 分钟的订单
        // select * from orders where status = 下单未支付 and order_time < (当前时间 - 15 min)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        // 判断是否含有数据
        if (ordersList != null && ordersList.size() > 0) {
            // 遍历订单列表
            for (Orders orders : ordersList) {
                // 修改订单状态为已取消
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直配送中订单
     */
    @Scheduled(cron = "0 0 1 * * ?")    // 每天凌晨 1 点触发
    public void processDeliveryOrder() {
        log.info("定时处理一直配送中订单：{}", LocalDateTime.now());

        // 查询上一个工作日一直处于派送中的订单
        // select * from orders where status = 派送中 and order_time < (当前时间 - 1 h)，凌晨1点减去1h为昨天的订单
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));

        // 判断是否含有数据
        if (ordersList != null && ordersList.size() > 0) {
            // 遍历订单列表
            for (Orders orders : ordersList) {
                // 修改订单状态为已取消
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
