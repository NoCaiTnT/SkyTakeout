package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    PageResult searchEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用 & 禁用员工账号
     * @param status
     * @param id
     */
    void changeEmployeeStatus(Integer status, Long id);

    /**
     * 根据 id 查询员工信息
     * @param id
     * @return
     */
    Employee searchEmployeeByID(Long id);

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    void changeEmployeeInfo(EmployeeDTO employeeDTO);
}
