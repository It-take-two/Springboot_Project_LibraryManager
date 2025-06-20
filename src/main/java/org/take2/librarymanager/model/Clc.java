package org.take2.librarymanager.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("clc")
public class Clc implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "code", type = IdType.AUTO)
    private String code;

    @TableField("name")
    private String name;

    @TableField("parent_code")
    private String parentCode;


}
