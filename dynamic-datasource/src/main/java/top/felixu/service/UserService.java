package top.felixu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.felixu.annotation.TargetDataSource;
import top.felixu.entity.User;
import top.felixu.mapper.UserMapper;

/**
 * @author felixu
 * @date 2019.07.23
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findById1(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @TargetDataSource(name = "ds1")
    public User findById2(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @TargetDataSource
    public User findById3(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @TargetDataSource(name = "ds2")
    public User findById4(int id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
