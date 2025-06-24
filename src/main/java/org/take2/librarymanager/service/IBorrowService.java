package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.Borrow;
import org.take2.librarymanager.service.impl.BorrowServiceImpl;

import java.util.List;

public interface IBorrowService extends IService<Borrow> {

    IPage<BorrowServiceImpl.BorrowVO> getAllBorrows(int current);

    IPage<BorrowServiceImpl.BorrowVO> getIncompleteBorrows(int current);

    IPage<BorrowServiceImpl.BorrowVO> getBorrowsByUser(Long userId, int current);

    IPage<BorrowServiceImpl.BorrowVO> getMyBorrows(int current);

    IPage<BorrowServiceImpl.BorrowVO> getMyIncompleteBorrows(int current);

    boolean createBorrow(Borrow borrow);

    boolean updateBorrow(Borrow borrow, Boolean collectionBorrowable);

    BorrowServiceImpl.BorrowVO getBorrowByBarcode(String barcode);

    boolean deleteBorrow(Long id);

    List<BorrowServiceImpl.BorrowVO> getOverdueAndUnpaidBorrowsByUser();
}
