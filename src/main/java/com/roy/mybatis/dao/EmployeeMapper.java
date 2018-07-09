package com.roy.mybatis.dao;

import com.roy.mybatis.bean.Employee;

public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public boolean deleteEmp(Integer id);
}
