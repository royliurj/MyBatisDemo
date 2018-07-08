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

public class MyBatisTest {

    @Test
    public void test() throws IOException {
        SqlSession sqlSession = null;
        try {

            //获取SqlSesssionFactory
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

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
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

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
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

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


}
