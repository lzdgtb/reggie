package com.liliang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liliang.reggie.common.R;
import com.liliang.reggie.entity.Employee;
import com.liliang.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @描述
 * @作者 黎亮
 * @创建时间 2022/9/15
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    /**
     * @Author 黎亮
     * @Description  员工登录
     * @Date 20:57 2022/9/15
     **/
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
       //1、将页面提交的密码进行MD5进行加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
       //2、根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }
        //4、进行密码比对,密码不正确
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        //5、查看员工的状态
        if(emp.getStatus() == 0){
            return R.error("账号已经被禁用");
        }
        //6、登录成功，将员工的id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * @Author 黎亮
     * @Description  员工退出
     * @Date 22:51 2022/9/15
     **/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1、清理session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");

    }

    /**
     * @Author 黎亮
     * @Description  新增员工
     * @Date 0:13 2022/9/16
     **/
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //获取当前登录用户的id
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * @Author 黎亮
     * @Description  员工信息分页查询
     * @Date 21:22 2022/9/16
     * @Param [page, pageSize, name]
     * **/
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {} ,name = {} ",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /**
     * @Author 黎亮
     * @Description  根据id修改员工信息
     * @Date 21:58 2022/9/16
     **/
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }
}
