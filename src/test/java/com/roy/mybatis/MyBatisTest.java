package com.roy.mybatis;

import com.roy.mybatis.bean.Employee;
import com.roy.mybatis.dao.EmployeeMapper;
import com.roy.mybatis.dao.EmployeeMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyBatisTest {

    @Test
    public void test() throws IOException {
        SqlSession sqlSession = null;
        try {

            //获取SqlSesssionFactory
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

            //获取SqlSession
            sqlSession = sqlSessionFactory.openSession();

            //执行查询语句
            Employee employee = sqlSession.selectOne("com.roy.mybatis.bean.EmployeeMapper.findById", 1);
            System.out.println(employee);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testWithInterface() throws IOException {

        SqlSession sqlSession = null;
        try {

            //获取SqlSesssionFactory
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

            //获取SqlSession
            sqlSession = sqlSessionFactory.openSession();

            //执行查询语句
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee emp = mapper.getEmpById(1);

            System.out.println(emp);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testWithInterfaceAnnotation() throws IOException {

        SqlSession sqlSession = null;
        try {

            //获取SqlSesssionFactory
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

            //获取SqlSession
            sqlSession = sqlSessionFactory.openSession();

            //执行查询语句
            EmployeeMapperAnnotation mapper = sqlSession.getMapper(EmployeeMapperAnnotation.class);
            Employee emp = mapper.getEmpById(1);

            System.out.println(emp);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testAddDeleteUpdate(){

        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

            //获取SqlSession
            sqlSession = sqlSessionFactory.openSession();

            //执行查询语句
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            Employee emp = mapper.getEmpById(1);
            System.out.println(emp);

            //添加数据
            Employee newEmp = new Employee();
            newEmp.setLastName("last name");
            newEmp.setEmail("123");
            newEmp.setGender("1");

            System.out.println(newEmp);
            mapper.addEmp(newEmp);

            System.out.println(newEmp);

            //修改数据
            Employee updateEmp = new Employee();
            updateEmp.setId(1);
            updateEmp.setGender("2");
            updateEmp.setEmail("asdf@dafs");
            updateEmp.setLastName("333");

            //删除数据
            boolean result = mapper.deleteEmp(3);
            System.out.println(result);

            mapper.updateEmp(updateEmp);

            sqlSession.commit();

            System.out.println("success");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testGetEmployeeByMultiParameter(){
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

            //获取SqlSession
            sqlSession = sqlSessionFactory.openSession();

            //执行查询语句
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            Employee emp = mapper.getEmpByIdAndGender(1,"2");
            System.out.println(emp);

            System.out.println("success");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
    @Test
    public void testGetEmployeeByMap(){
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

            //获取SqlSession
            sqlSession = sqlSessionFactory.openSession();

            //执行查询语句
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",1);
            map.put("gender","2");

            Employee emp = mapper.getEmpByMap(map);
            System.out.println(emp);

            System.out.println("success");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        //获取SqlSesssionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

}
