package org.take2.librarymanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.take2.librarymanager.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User selectByUserName(String userName);
    User selectByUserNumber(String userNumber);
}
