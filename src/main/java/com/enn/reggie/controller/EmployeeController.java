package com.enn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enn.reggie.common.R;
import com.enn.reggie.entity.Employee;
import com.enn.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/test1")
    public String todotest(){
        return "成功";
    }

    @RequestMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        String password=employee.getPassword();
        //对页面输入的密码进行加密
        String getpass=DigestUtils.md5DigestAsHex(password.getBytes());

        //与数据库中的用户进行匹配
        //new QueryWrapper<>();
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<Employee>();
        queryWrapper.eq("username",employee.getUsername());
        Employee em=employeeService.getOne(queryWrapper);

        if(em==null){
            return R.error("登录失败，该用户不存在！！");
        }

        //否则的话进行用户密码比对
        if(!em.getPassword().equals(getpass)){
            return R.error("登录失败，用户密码错误！！");
        }
        //查看员工是否被禁用
        if(em.getStatus()==0){
            return R.error("账号已禁用~");
        }
        //登录成功，存入session
        request.getSession().setAttribute("employee",em.getId());
        return R.success(em);
    }

    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的员工信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！！");
    }


}
