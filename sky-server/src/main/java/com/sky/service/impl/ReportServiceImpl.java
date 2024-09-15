package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取指定时间范围内的所有日期
     * @param startDate
     * @param endDate
     * @return
     */
    List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        // 当前集合用于存放从开始到结束范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();

        // 计算日期
        while (!startDate.isAfter(endDate)) {
            dateList.add(startDate);
            startDate = startDate.plusDays(1);
        }

        return dateList;
    }

    /**
     * 统计指定时间区间内的营业额数据
     * @param startTime
     * @param endTime
     * @return
     */
    public TurnoverReportVO turnoverStatistics(LocalDate startTime, LocalDate endTime) {
        // 当前集合用于存放从开始到结束范围内的每天的日期
        List<LocalDate> dateList = getDatesBetween(startTime, endTime);

        // 查询营业额数据
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 查询这一天的营业额数据（已完成订单）
            // select sum(amount) from order where status = 5 and order_time > ? and order_time < ?
            // 获取当天的 0 点时间以及 23:59:59
            LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", begin);
            map.put("end", end);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
        return  turnoverReportVO;
    }

    /**
     * 统计指定时间区间内的用户数据
     * @param startTime
     * @param endTime
     * @return
     */
    public UserReportVO userStatistics(LocalDate startTime, LocalDate endTime) {
        // 当前集合用于存放从开始到结束范围内的每天的日期
        List<LocalDate> dateList = getDatesBetween(startTime, endTime);

        // 获取每天的新增用户数
        List<Integer> newUserList = new ArrayList<>();
        // 获取每天的总用户数
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
            // 查询每天的用户总量
            Map map = new HashMap();
            map.put("end", end);
            Integer totalUser = userMapper.countByMap(map);

            // 查询每天的新增用户量
            map.put("begin", begin);
            Integer newUser = userMapper.countByMap(map);

            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }
}
