package org.take2.librarymanager.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("v_class_semester_report")
public class VClassSemesterReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;

    @TableField("semester")
    private String semester;

    @TableField("class_id")
    private Long classId;

    @TableField("class_name")
    private String className;

    @TableField("borrow_times")
    private Long borrowTimes;

    @TableField("reader_count")
    private Long readerCount;

    @TableField("last_borrow_time")
    private Instant lastBorrowTime;


}
