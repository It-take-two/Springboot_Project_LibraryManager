package org.take2.librarymanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.take2.librarymanager.model.Borrow;
import org.take2.librarymanager.service.IBorrowService;
import org.take2.librarymanager.service.impl.BorrowServiceImpl;

import java.util.List;

@Mapper
public interface BorrowMapper extends BaseMapper<Borrow> {

    /**
     * 查询所有借阅记录，联表查询获取对应馆藏的 is_borrowable 状态
     */
    Page<BorrowServiceImpl.BorrowVO> selectAllBorrows(Page<?> page);

    /**
     * 查询未完成的借阅记录（对应馆藏 is_borrowable = false）
     */
    Page<BorrowServiceImpl.BorrowVO> selectIncompleteBorrows(Page<?> page);

    /**
     * 根据用户ID查询借阅记录
     */
    Page<BorrowServiceImpl.BorrowVO> selectBorrowsByUser(Page<?> page, @Param("userId") Long userId);

    /**
     * 查询当前用户未完成的借阅记录：user_id = #{userId} 且馆藏 is_borrowable = false
     */
    Page<BorrowServiceImpl.BorrowVO> selectMyIncompleteBorrows(Page<?> page, @Param("userId") Long userId);

    List<BorrowServiceImpl.BorrowVO> selectOverdueAndUnpaidBorrowsByUser(@Param("userId") Long userId);
}
