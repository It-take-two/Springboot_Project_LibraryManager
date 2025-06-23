package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.Borrow;
import org.take2.librarymanager.service.impl.BorrowServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 借阅记录业务接口
 */
public interface IBorrowService extends IService<Borrow> {

    /**
     * 获取所有借阅记录（管理员接口）
     */
    IPage<BorrowServiceImpl.BorrowVO> getAllBorrows(int current);

    /**
     * 获取未完成的借阅记录（管理员接口），条件：对应馆藏的 is_borrowable = false
     */
    IPage<BorrowServiceImpl.BorrowVO> getIncompleteBorrows(int current);

    /**
     * 根据用户 ID 获取借阅记录（管理员接口）
     */
    IPage<BorrowServiceImpl.BorrowVO> getBorrowsByUser(Long userId, int current);

    /**
     * 当前用户获取自己的所有借阅记录
     */
    IPage<BorrowServiceImpl.BorrowVO> getMyBorrows(int current);

    /**
     * 当前用户获取自己未完成的借阅记录
     */
    IPage<BorrowServiceImpl.BorrowVO> getMyIncompleteBorrows(int current);

    /**
     * 新增借阅记录
     */
    boolean createBorrow(Borrow borrow);

    /**
     * 更新借阅记录，允许修改续借次数、罚款、归还日期；同时更新
     * 对应馆藏的可借状态（collectionBorrowable 非 null 时更新）
     */
    boolean updateBorrow(Borrow borrow, Boolean collectionBorrowable);

    BorrowServiceImpl.BorrowVO getBorrowByBarcode(String barcode);

    /**
     * 删除借阅记录
     */
    boolean deleteBorrow(Long id);

    List<BorrowServiceImpl.BorrowVO> getOverdueAndUnpaidBorrowsByUser();
}
