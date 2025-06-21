package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.take2.librarymanager.mapper.BorrowMapper;
import org.take2.librarymanager.mapper.CollectionMapper;
import org.take2.librarymanager.mapper.UserMapper;
import org.take2.librarymanager.model.Borrow;
import org.take2.librarymanager.model.Collection;
import org.take2.librarymanager.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements IBorrowService {

    @Autowired
    private BorrowMapper borrowMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<BorrowVO> getAllBorrows(int current) {
        Page<BorrowVO> page = new Page<>(current, 12);
        return borrowMapper.selectAllBorrows(page);
    }

    @Override
    public Page<BorrowVO> getIncompleteBorrows(int current) {
        Page<BorrowVO> page = new Page<>(current, 12);
        return borrowMapper.selectIncompleteBorrows(page);
    }

    @Override
    public Page<BorrowVO> getBorrowsByUser(Long userId, int current) {
        Page<BorrowVO> page = new Page<>(current, 12);
        return borrowMapper.selectBorrowsByUser(page, userId);
    }

    @Override
    public Page<BorrowVO> getMyBorrows(int current) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getBorrowsByUser(userId, current);
    }

    @Override
    public Page<BorrowVO> getMyIncompleteBorrows(int current) {
        Page<BorrowVO> page = new Page<>(current, 12);
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return borrowMapper.selectMyIncompleteBorrows(page, userId);
    }

    @Override
    public boolean createBorrow(Borrow borrow) {
        // 此处可以扩展检查：如检查用户是否存在、馆藏是否存在等
        return save(borrow);
    }

    @Override
    public boolean updateBorrow(Borrow borrow, Boolean collectionBorrowable) {
        boolean result = updateById(borrow);
        if (collectionBorrowable != null) {
            // 更新对应馆藏的可借状态
            Collection coll = collectionMapper.selectById(borrow.getCollectionId());
            if (coll != null) {
                coll.setIsBorrowable(collectionBorrowable);
                collectionMapper.updateById(coll);
            }
        }
        return result;
    }

    @Override
    public boolean deleteBorrow(Long id) {
        return removeById(id);
    }

    public boolean isOverdue(BorrowVO vo) {
        return vo.returnDate() == null
                && vo.returnDeadline() != null
                && Instant.now().isAfter(vo.returnDeadline());
    }

    @Override
    public List<BorrowVO> getOverdueAndUnpaidBorrowsByUser() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userId == null) {
            throw new IllegalArgumentException("找不到用户");
        }
        return borrowMapper.selectOverdueAndUnpaidBorrowsByUser(userId);
    }

    public record BorrowVO(
            Long id,
            Instant borrowDate,
            Long userId,
            Long collectionId,
            Instant returnDeadline,
            Instant returnDate,
            Integer renewedTimes,
            BigDecimal finePaid,
            Boolean collectionIsBorrowable
    ) {}

}
