package com.roy.mybatis.dao;

import com.roy.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface EmployeeMapper {

    public Employee getEmpById(Integer ida);

    public Integer addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public boolean deleteEmp(Integer id);

    public Employee getEmpByIdAndGender(@Param("id") Integer id, @Param("gender") String gender);

    public Employee getEmpByMap(Map<String,Object> map);
}
