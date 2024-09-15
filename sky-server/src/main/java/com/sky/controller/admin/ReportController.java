package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 数据统计相关接口
 */
@RestController
@RequestMapping("/admin/report")
@Api(tags = "报表数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @param startTime 开始时间，使用@DateTimeFormat注解指定日期格式
     * @param endTime 结束时间，同上
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation(value = "营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(@RequestParam("begin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
                                                       @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        log.info("营业额数据统计：{}，{}", startTime, endTime);

        TurnoverReportVO turnoverReportVO = reportService.turnoverStatistics(startTime, endTime);

        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    @ApiOperation(value = "用户统计")
    public Result<UserReportVO> userStatistics(@RequestParam("begin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
                                               @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        log.info("用户数据统计：{}，{}", startTime, endTime);

        UserReportVO userReportVO = reportService.userStatistics(startTime, endTime);

        return Result.success(userReportVO);
    }
}
