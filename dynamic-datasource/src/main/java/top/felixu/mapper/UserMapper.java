package top.felixu.mapper;

import top.felixu.entity.User;

/**
 * @author felixu
 * @date 2019.07.23
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}