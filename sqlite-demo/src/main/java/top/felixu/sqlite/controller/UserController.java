package top.felixu.sqlite.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.felixu.sqlite.entity.User;
import top.felixu.sqlite.service.IUserService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-05-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sqlite/user")
public class UserController {

    private final IUserService userService;

    @PostMapping
    public Object add() {
        User user = new User();
        user.setAge(20);
        user.setName("test");
        return userService.save(user);
    }

    @GetMapping
    public Object get() {
        return userService.list();
    }
}

