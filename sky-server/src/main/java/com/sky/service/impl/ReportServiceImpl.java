package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * 统计指定时间区间内的订单数据
     * @param startTime
     * @param endTime
     * @return
     */
    public OrderReportVO orderStatistics(LocalDate startTime, LocalDate endTime) {
        // 当前集合用于存放从开始到结束范围内的每天的日期
        List<LocalDate> dateList = getDatesBetween(startTime, endTime);

        // 获取每天的订单总数
        List<Integer> orderCountList = new ArrayList<>();
        // 获取每天的有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
            // 查询每天的订单总数
            Map map = new HashMap();
            map.put("begin", begin);
            map.put("end", end);
            Integer orderCount = orderMapper.countByMap(map);
            // 查询每天的有效订单数
            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countByMap(map);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        // 获得订单总数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();

        // 获得有效订单总数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        // 计算订单完成率
        Double orderCompletionRate = totalOrderCount == 0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 统计指定时间区间内的销售额Top10数据
     * @param startTime
     * @param endTime
     * @return
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate startTime, LocalDate endTime) {
        // 转换时间
        LocalDateTime begin = LocalDateTime.of(startTime, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(endTime, LocalTime.MAX);

        // 查询销售额Top10数据
        List<GoodsSalesDTO> salesTop10List = orderMapper.getSalesTop10(begin, end);

        // 获取商品名称列表
        List<String> nameList = salesTop10List.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());

        // 获取销量列表
        List<Integer> numberList = salesTop10List.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    /**
     * 导出营业数据
     * @param response
     */
    public void exportBusinessData(HttpServletResponse response) {
        // 查询最近 30 天的运营数据
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(beginDate, LocalTime.MIN),
                                                                       LocalDateTime.of(endDate, LocalTime.MAX));

        // 通过 POI 写入 Excel 文件
        // 获取模板文件输入流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            // 基于模板文件创建新的 Excel 文件
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // 获取 sheet 页
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 填充数据：时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + beginDate + " - " + endDate);

            // 获取第 4 行
            XSSFRow row = sheet.getRow(3);

            // 填充数据：营业额、订单完成率、新增用户数
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());

            // 获取第 5 行
            row = sheet.getRow(4);

            // 填充数据：有效订单、平均客单价
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            // 循环获得 30 天的数据
            LocalDate date = beginDate;
            for (int i = 0; i < 30; i++) {
                date = beginDate.plusDays(i);
                // 查询这一天的运营数据
                BusinessDataVO tempData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN),
                                                                      LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(tempData.getTurnover());
                row.getCell(3).setCellValue(tempData.getValidOrderCount());
                row.getCell(4).setCellValue(tempData.getOrderCompletionRate());
                row.getCell(5).setCellValue(tempData.getUnitPrice());
                row.getCell(6).setCellValue(tempData.getNewUsers());
            }

            // 使用输出流将 Excel 文件下载到客户端浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);

            // 关闭资源
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
