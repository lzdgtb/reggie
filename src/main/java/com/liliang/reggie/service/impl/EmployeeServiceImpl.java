package com.liliang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liliang.reggie.entity.Employee;
import com.liliang.reggie.mapper.EmployeeMapper;
import com.liliang.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @描述
 * @作者 黎亮
 * @创建时间 2022/9/15
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
