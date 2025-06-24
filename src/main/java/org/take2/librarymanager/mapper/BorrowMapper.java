package org.take2.librarymanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.take2.librarymanager.model.Borrow;
import org.take2.librarymanager.service.impl.BorrowServiceImpl;

import java.util.List;

@Mapper
public interface BorrowMapper extends BaseMapper<Borrow> {

    IPage<BorrowServiceImpl.BorrowVO> selectAllBorrows(Page<?> page);

    IPage<BorrowServiceImpl.BorrowVO> selectIncompleteBorrows(Page<?> page);

    IPage<BorrowServiceImpl.BorrowVO> selectBorrowsByUser(Page<?> page, @Param("userId") Long userId);

    IPage<BorrowServiceImpl.BorrowVO> selectMyIncompleteBorrows(Page<?> page, @Param("userId") Long userId);

    List<BorrowServiceImpl.BorrowVO> selectOverdueAndUnpaidBorrowsByUser(@Param("userId") Long userId);
}
