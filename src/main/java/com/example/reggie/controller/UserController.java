package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.common.R;
import com.example.reggie.entity.Employee;
import com.example.reggie.entity.User;
import com.example.reggie.service.UserService;
import com.example.reggie.utils.EmailSending;
import com.example.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String>sendMsg (@RequestBody User user, HttpSession session) throws MessagingException {
        String phone = user.getPhone();
        if(!StringUtils.isBlank(phone)){
            String code = null;

            /*EmailSending emailSending = new EmailSending();
            emailSending.sendEmail(phone,"瑞吉外卖",code);*/
            System.out.println(code);
            session.setAttribute(phone,code);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<String>login (@RequestBody Map user, HttpSession session) throws MessagingException {

        String phone = user.get("phone").toString();
        /*String code = user.get("code").toString();*/
        Object codeInSession = session.getAttribute(phone);
        if(codeInSession==null){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.eq(User::getPhone,phone);
           User user1 = userService.getOne(queryWrapper);
           if(user1==null){
               user1 = new User();
               user1.setPhone(phone);
               user1.setStatus(1);
               userService.save(user1);
               return R.success("登录成功");
           }
        }
return R.error("登录失败");
    }
}
