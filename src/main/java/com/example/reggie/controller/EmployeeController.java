package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.entity.Employee;
import com.example.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * Description 登录功能
     *
     * @param request (HttpServletRequest) 用于储存session
     * @param employee 员工对象
     * @return {@link R }<{@link Employee }>
     * @author 李朋逊
     * @date 2023/06/17
     */
    @RequestMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /*
        1.将页面提交的密码进行md5加密处理
        2.根据页面提交的用户名查询数据库
        3.如果没有查询到则返回登陆失败结果
        4.密码比对，如果不一致则返回登陆失败结果
        5.查看员工状态，如果为已禁用状态则返回员工已禁用结果
        6.登录成功，将员工id存入session并返回登陆成功结果
         */
        //将密码加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //mybatis-plus中的Lambda匿名函数表达式，wrapper.eq(实体类::查询字段,条件值)表达式相当于where类sql语句
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //相当于select from employee where username = employee.getUsername();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        //因为username在数据库中做了唯一判断，所以只可能查出一条数据
        Employee emp = employeeService.getOne(queryWrapper);
        //没有查询到用户名
        if(emp == null){
            return R.error("登陆失败");
        }
        //密码比对，如果不一致则返回登陆失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //查看员工状态，如果为已禁用状态则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return  R.error("账号已禁用");
        }
        //登录成功，存入session,返回响应
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    /**
     * Description 管理端退出功能
     *
     * @return {@link R }<{@link String }>
     * @author 李朋逊
     * @date 2023/06/17
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1.清理session中保存的用户id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * Description 新增员工方法
     *
     * @param request  请求
     * @param employee 员工
     * @return {@link R }<{@link String }>
     * @author 李朋逊
     * @date 2023/06/18
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息{}",employee.toString());
        //设置初始密码并进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //mybatis-plus封装方法
        employeeService.save(employee);
        return R.success("新增员工成功");
    }


    /**
     * Description 员工信息分页查询
     *
     * @param page     页面
     * @param pageSize 页面大小
     * @param name     名字
     * @return {@link R }<{@link Page }>
     * @author 李朋逊
     * @date 2023/06/18
     */
    @GetMapping("/page")
    public R<Page> page (int page,int pageSize,String name){
        log.info("page = {},pageSize = {} , name = {}",page,pageSize,name);

        //创建分页构造器
        Page pageInfo = new Page(page,pageSize);

        //创建条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件:当name不为空时，查询employee表中name关键字下含有相应字符name的数据
        //相当于select from employee where name like queryName;
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件-根据修改时间进行降序排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询——根据queryWrapper进行条件查询，将查询到的数据封装到pageInfo对象中，pageInfo对象本身就已经定义了分页条件
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * Description 根据id修改员工信息
     *
     * @param employee 员工
     * @return {@link R }<{@link String }>
     * @author 李朋逊
     * @date 2023/06/18
     */

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long empId  = (Long)  request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");

    }


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息，{}",id);
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("为查询到员工");
    }
}
