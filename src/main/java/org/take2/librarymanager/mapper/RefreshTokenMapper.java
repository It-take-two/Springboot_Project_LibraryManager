package org.take2.librarymanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.take2.librarymanager.model.RefreshToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-06-20
 */
@Mapper
public interface RefreshTokenMapper extends BaseMapper<RefreshToken> {
    RefreshToken selectByUserIdAndHashedToken(Long userId, String hashedToken);
    void deleteByUserIdAndHashedToken(Long userId, String hashedToken);

}
