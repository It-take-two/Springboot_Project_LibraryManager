package org.take2.librarymanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.take2.librarymanager.model.RefreshToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface RefreshTokenMapper extends BaseMapper<RefreshToken> {
    RefreshToken selectByUserIdAndHashedToken(Long userId, String hashedToken);
    void deleteByUserIdAndHashedToken(Long userId, String hashedToken);
}
