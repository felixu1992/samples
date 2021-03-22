package top.felixu.sqlite.service.impl;

import top.felixu.sqlite.entity.User;
import top.felixu.sqlite.mapper.UserMapper;
import top.felixu.sqlite.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-05-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
