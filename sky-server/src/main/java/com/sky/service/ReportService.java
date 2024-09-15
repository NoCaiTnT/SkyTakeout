package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {


    /**
     * 统计指定时间区间内的营业额数据
     * @param startTime
     * @param endTime
     * @return
     */
    TurnoverReportVO turnoverStatistics(LocalDate startTime, LocalDate endTime);

    /**
     * 统计指定时间区间内的用户数据
     * @param startTime
     * @param endTime
     * @return
     */
    UserReportVO userStatistics(LocalDate startTime, LocalDate endTime);

    /**
     * 统计指定时间区间内的订单数据
     * @param startTime
     * @param endTime
     * @return
     */
    OrderReportVO orderStatistics(LocalDate startTime, LocalDate endTime);

    /**
     * 统计指定时间区间内的销售额Top10数据
     * @param startTime
     * @param endTime
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate startTime, LocalDate endTime);
}
