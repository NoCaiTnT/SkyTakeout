package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee
     */
    @Insert("insert into employee(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void addEmployee(Employee employee);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> searchEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据员工账号信息 动态修改
     * @param employee
     */
    void updateEmployee(Employee employee);

    /**
     * 根据 id 查询员工信息
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")  // 表列名和类变量名不完全一致，MyBatis有自动映射功能
    Employee searchEmployeeByID(Long id);
}
