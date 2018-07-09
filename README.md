# Hello World (老式代码)
1. 配置文件 mybatis-config.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--<properties resource="mysql.properties"/>-->
    <settings>
        <!--全局性设置懒加载。如果设为‘false’，则所有相关联的都会被初始化加载,默认值为false-->
        <setting name="lazyLoadingEnabled" value="true"/>
        <!--当设置为‘true’的时候，懒加载的对象可能被任何懒属性全部加载。否则，每个属性都按需加载。默认值为true-->
        <setting name="aggressiveLazyLoading" value="false"/>
    </settings>
    <typeAliases>
        <!-- 其实就是将bean的替换成一个短的名字-->
        <typeAlias type="com.roy.mybatis.bean.Employee" alias="Employee"/>
    </typeAliases>
    <!--对事务的管理和连接池的配置-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"></transactionManager>
            <dataSource type="POOLED"><!--POOLED：使用Mybatis自带的数据库连接池来管理数据库连接-->
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql:///MyDataBase"/>
                <property name="username" value="root"/>
                <property name="password" value="+123a456"/>
            </dataSource>
        </environment>
    </environments>
    <!--mapping文件路径配置-->
    <mappers>
        <mapper resource="mapper/EmployeeMapper.xml"/>
    </mappers>

</configuration>
```
2. Sql映射文件 EmployeeMapper.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.roy.mybatis.bean.EmployeeMapper">
    <select id="findById" parameterType="int" resultType="com.roy.mybatis.bean.Employee">
        SELECT id, last_name lastName, email, gender FROM tbl_employee WHERE id=#{id}
    </select>
</mapper>
```
3. 代码
```
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
            Employee employee = sqlSession.selectOne("findById", 1);
            System.out.println(employee);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```

# 基于接口编程
1. 配置文件不变
2. 添加接口定义
```
package com.roy.mybatis.dao;

import com.roy.mybatis.bean.Employee;

public interface EmployeeMapper {

    public Employee getEmpById(Integer id);
}
```
3. Sql映射文件变化  EmployeeMapper.xml
变化：namespace是接口到全类名，select到id是接口方法名
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.roy.mybatis.dao.EmployeeMapper">
    <select id="getEmpById" parameterType="int" resultType="com.roy.mybatis.bean.Employee">
        SELECT id, last_name lastName, email, gender FROM tbl_employee WHERE id=#{id}
    </select>
</mapper>
```
4. 代码变化
```
            //获取接口并调用方法返回查询结果
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee emp = mapper.getEmpById(1);
```

# 小结
1. SqlSession代表和数据库到一次会话，用完必须关闭；
2. SqlSession和connection一样都是非线程安全的，每次使用都应该获取新对象；
3. mapper接口没有实现类，但是mybatis会为这个接口生产一个代理对象；
4. 两个重要的配置文件
- 全局配置文件
- Sql映射文件：保存了每一个sql语句映射的信息

---


# 全局配置文件标签
官方文档：http://www.mybatis.org/mybatis-3/configuration.html
1. properties  
```
    <!--使用properties引入外部properties配置文件的内容
        resource: 引入类路径资源
        rul：引入磁盘资源
    -->
    <properties resource="mysql.properties"/>
    
    <dataSource type="POOLED">
        <property name="driver" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </dataSource>
```
2. Settings 运行时行为配置   
更多参见官方文档
```
    <settings>
        <!--全局性设置懒加载。如果设为‘false’，则所有相关联的都会被初始化加载,默认值为false-->
        <setting name="lazyLoadingEnabled" value="true"/>
        <!--当设置为‘true’的时候，懒加载的对象可能被任何懒属性全部加载。否则，每个属性都按需加载。默认值为true-->
        <setting name="aggressiveLazyLoading" value="false"/>
        <!--使用驼峰法-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!--控制台输出sql语句-->
        <setting name="logImpl" value="STDOUT_LOGGING" />
    </settings>
```
3. typeAliases  
别名处理器，配置了以后在Sql映射文件中就可以使用别名，别名不区分大小写
```
    //1,单个别名
    <typeAliases>
        <!-- 其实就是将bean的替换成一个短的名字
            默认为类名小写employee，别名不区分大小写
        -->
        <typeAlias type="com.roy.mybatis.bean.Employee" alias="Employee1"/>
    </typeAliases>
    
    <mapper namespace="com.roy.mybatis.dao.EmployeeMapper">
         <select id="getEmpById" parameterType="int" resultType="Employee1">
        SELECT * FROM tbl_employee WHERE id=#{id}
        </select>
    </mapper>
    
    //2，批量别名
    <!--批量起别名 为某个包下的所有类起别名
            name：指定包（当前及子包）都起一个默认别名
    -->
    <package name="com.roy.mybatis.bean"/>
    
    //3，注解起别名
    @Alias("emp")
    public class Employee {}
```
4. typeHandlers  
类型处理器：数据库类型和java类型映射的情况
```
    <typeHandlers>
        <typeHandler handler="org.apache.ibatis.type.EnumOrdinalTypeHandler" javaType="java.math.RoundingMode"/>
    </typeHandlers>
```
5. plugins  
插件Executor、ParameterHandler、ResultSetHandler、StatementHandler
6. environments  
default可以切换成enviroment的id名
```
    <environments default="development">
        <enviroment id="development>
        </enviroment>
        <enviroment id="produce>
        </enviroment>
    </enviroments>
```
7. databaseIdProvider  
```
    <!--支持多数据库厂商
        type="DB_VENDOR" 作用是得到数据库厂商的标识
        MySQL、Oracle、SQL Server
    -->
    <databaseIdProvider type="DB_VENDOR">
        <!--为mysql数据库起名为-->
        <property name="MySQL" value="mysql2"/>
        <property name="Oracle" value="oracle2"/>
    </databaseIdProvider>
    
    //Sql映射配置，不同的数据库执行不同的Sql脚本
    <select id="getEmpById" parameterType="int" resultType="com.roy.mybatis.bean.Employee"
        databaseId="mysql2">
        SELECT * FROM tbl_employee WHERE id=#{id}
    </select>
    <select id="getEmpById" parameterType="int" resultType="com.roy.mybatis.bean.Employee"
            databaseId="oracle2">
        SELECT * FROM employees WHERE id=#{id}
    </select>
```
8. mappers
```
    <!--将Sql映射注册到全局配置中
        resource：引用类路径下到映射文件
        url：引用网络路径下（磁盘下）到映射文件
        class：引用接口
            要求：接口和映射文件同名，必须放在同一目录下
                 没有Sql映射文件，所有到Sql都是利用注解写在接口上
    -->
    <mappers>
        <mapper class="com.roy.mybatis.dao.EmployeeMapperAnnotation"/>
        <mapper resource="mapper/EmployeeMapper.xml" />
        <!--<mapper url="file:///var/mappers/Autofac.xml"/>-->
        
        <!--批量注册-->
        <package name="com.roy.mybatis.dao"/>
    </mappers>
    
    //class到使用
    public interface EmployeeMapperAnnotation {

        @Select("select * from tbl_employee where id=#{id}")
        public Employee getEmpById(Integer id);
    }

```

---
# Sql映射文件
### 1. 增删改查  
允许增删改直接定义的返回值类型：Integer、Long、Boolean
1. 接口定义
```
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void addEmp(Employee employee);

    public void updateEmp(Employee employee);

    public boolean deleteEmp(Integer id);
}
```
2. Mapper定义
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.roy.mybatis.dao.EmployeeMapper">
    <select id="getEmpById" parameterType="int" resultType="com.roy.mybatis.bean.Employee">
        SELECT * FROM tbl_employee WHERE id=#{id}
    </select>

    <!--public void addEmp(Employee employee);-->
    <insert id="addEmp">
        insert into tbl_employee (last_name,gender,email)
        VALUES (#{lastName},#{gender},#{email})
    </insert>

    <!--public void updateEmp(Employee employee);-->
    <update id="updateEmp">
        UPDATE tbl_employee
        SET last_name = #{lastName},
            gender = #{gender},
            email = #{email}
        WHERE id = #{id}
    </update>

    <!--public void deleteEmp(Integer id);-->
    <delete id="deleteEmp">
        DELETE FROM tbl_employee WHERE id = #{id}
    </delete>
</mapper>
```
3. 代码
```
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
            mapper.addEmp(newEmp);


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

            //手动提交，
            sqlSession.commit();

            System.out.println("success");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```
### 2. 获取自增主键
1. MySql
```
    <!-- MySql支持自增
        Mybatis也是利用statement的getGeneratedKeys()获取
        useGeneratedKeys="true" : 使用自增主键策略
        keyProperty="id" : 返回的自增主键存入JavaBean的哪个属性
    -->
    <insert id="addEmp" useGeneratedKeys="true" keyProperty="id">
        insert into tbl_employee (last_name,gender,email)
        VALUES (#{lastName},#{gender},#{email})
    </insert>
```
2. Oracle
```
    <!-- Oracle 不支持自增，使用序列来模拟自增，主键先从序列中拿到值，在执行插入操作-->
    <insert id="addEmp" databaseId="oracle2">
        <!--
            keyProperty把查出来到值插入到javabean到哪个属性
            order：在当前sql之前执行
        -->
        <selectKey keyProperty="id" order="BEFORE" resultType="Integer">
          <!--编写查询主键到sql语句-->
            select EMPLOYEES_SEQ.nextval from dual
        </selectKey>
        insert into tbl_employee (id,last_name,gender,email)
        VALUES (#{id},#{lastName},#{gender},#{email})
    </insert>
```
### 3. 参数处理
1. 单个参数  
#{} 里面写什么都无所谓，不做任何处理
2. 多个参数  
多个参数会被封装成一个map，key：param1..paramN  
错误的写法：
```
    public Employee getEmpByIdAndGender(Integer id, String gender);

    <select id="getEmpByIdAndGender" resultType="com.roy.mybatis.bean.Employee">
        SELECT * FROM tbl_employee WHERE id=#{id} AND gender = #{gender}
    </select>
    
    //Parameter 'id' not found. Available parameters are [arg1, arg0, param1, param2]
```
正确的写法：  
命名参数：明确的指定参数的名称, @Param("id") Integer id  
如果参数很多，正好是业务逻辑的数据模型，这时就可以直接传入POJO;  
如果参数不是POJO的模型，为了方便可以直接传入map,#{}可以直接从map中获取值
```
    public Employee getEmpByIdAndGender(@Param("id") Integer id, @Param("gender") String gender);
    public Employee getEmpByMap(Map<String,Object> map);

    <select id="getEmpByIdAndGender" resultType="com.roy.mybatis.bean.Employee">
        SELECT * FROM tbl_employee WHERE id=#{id} AND gender = #{gender}
    </select>
    
    <select id="getEmpByMap" resultType="com.roy.mybatis.bean.Employee">
        SELECT * FROM tbl_employee WHERE id=#{id} AND gender = #{gender}
    </select>
```
或者
```
    <select id="getEmpByIdAndGender" resultType="com.roy.mybatis.bean.Employee">
        SELECT * FROM tbl_employee WHERE id=#{param1} AND gender = #{param2}
    </select>
```
