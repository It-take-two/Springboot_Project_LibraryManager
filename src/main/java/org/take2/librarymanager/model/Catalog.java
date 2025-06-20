package org.take2.librarymanager.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("catalog")
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("isbn")
    private String isbn;

    @TableField("publisher")
    private String publisher;

    @TableField("category")
    private String category;

    @TableField("publish_date")
    private LocalDateTime publishDate;

    @TableField("author")
    private String author;

    @TableField("value")
    private BigDecimal value;


}
