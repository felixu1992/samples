package top.felixu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import top.felixu.dynamic.DynamicDataSourceRegister;
import top.felixu.entity.DataSourceEntity;
import top.felixu.entity.User;
import top.felixu.service.UserService;
import top.felixu.utils.ApplicationUtils;
import top.felixu.utils.HttpUtils;

/**
 * @author felixu
 * @date 2019.07.23
 */
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/test1")
    public User test1() {
        return userService.findById1(1);
    }

    @GetMapping("/test2")
    public User test2() {
        return userService.findById2(1);
    }

    @PostMapping("/add")
    public Object addDataSource(@RequestBody DataSourceEntity entity) {
        DynamicDataSourceRegister register = ApplicationUtils.getBean(DynamicDataSourceRegister.class);
        register.hook(entity);
        return entity;
    }

    @GetMapping("/test3")
    public User test3(@RequestParam String dsId) {
        HttpUtils.getRequestAttributes().setAttribute("ds", dsId, RequestAttributes.SCOPE_REQUEST);
        return userService.findById3(1);
    }

    @GetMapping("/test4")
    public User test4() {
        return userService.findById4(1);
    }
}
