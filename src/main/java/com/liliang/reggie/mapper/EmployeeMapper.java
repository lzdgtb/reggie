package com.liliang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liliang.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @描述
 * @作者 黎亮
 * @创建时间 2022/9/15
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
