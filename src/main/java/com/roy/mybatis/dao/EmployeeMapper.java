package com.roy.mybatis.dao;

import com.roy.mybatis.bean.Employee;

public interface EmployeeMapper {

    public Employee getEmpById(Integer id);
}
