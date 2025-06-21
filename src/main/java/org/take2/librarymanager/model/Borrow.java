package org.take2.librarymanager.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.Instant;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("borrow")
public class Borrow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("borrow_date")
    private Instant borrowDate = Instant.now();

    @TableField("user_id")
    private Long userId;

    @TableField("collection_id")
    private Long collectionId;

    @TableField("return_deadline")
    private Instant returnDeadline;

    @TableField("return_date")
    private Instant returnDate;

    @TableField("renewed_times")
    private Integer renewedTimes;

    @TableField("fine_paid")
    private BigDecimal finePaid;


}
