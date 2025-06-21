package org.take2.librarymanager.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("refresh_token")
public class RefreshToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "refresh_token_id", type = IdType.AUTO)
    private Long refreshTokenId;

    @TableField("user_id")
    private Long userId;

    @TableField("expired_at")
    private Instant expiredAt;

    @TableField("hashed_token")
    private String hashedToken;

    @TableField("created_at")
    private Instant createdAt = Instant.now();


}
