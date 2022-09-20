package com.enn.reggie;

import com.enn.reggie.entity.Employee;
import com.enn.reggie.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReggieApplicationTest {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void test01(){
       List<Employee> list=employeeMapper.selectList(null);
       System.out.println("查询结果：~~~~~"+list);
    }

}
