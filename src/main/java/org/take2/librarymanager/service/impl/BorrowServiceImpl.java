package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.take2.librarymanager.mapper.*;
import org.take2.librarymanager.model.Borrow;
import org.take2.librarymanager.model.BorrowRule;
import org.take2.librarymanager.model.Collection;
import org.take2.librarymanager.model.User;
import org.take2.librarymanager.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements IBorrowService {

    @Autowired
    private BorrowMapper borrowMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BorrowRuleMapper borrowRuleMapper;

    public Instant calculateReturnDeadline(Long userId, Instant baseTime) {
        if (userId == null || baseTime == null) {
            throw new IllegalArgumentException("用户ID和起始时间不能为空");
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("找不到用户");
        }

        BorrowRule rule = borrowRuleMapper.selectOne(
                new LambdaQueryWrapper<BorrowRule>().eq(BorrowRule::getRoleName, user.getRoleName())
        );
        if (rule == null) {
            throw new IllegalStateException("借阅规则不存在");
        }

        return baseTime.plusSeconds(rule.getLoanDays() * 86400L);
    }

    @Override
    public IPage<BorrowVO> getAllBorrows(int current) {
        Page<BorrowVO> page = new Page<>(current, 12);
        return borrowMapper.selectAllBorrows(page);
    }

    @Override
    public IPage<BorrowVO> getIncompleteBorrows(int current) {
        Page<BorrowVO> page = new Page<>(current, 12);
        return borrowMapper.selectIncompleteBorrows(page);
    }

    @Override
    public IPage<BorrowVO> getBorrowsByUser(Long userId, int current) {
        Page<BorrowVO> page = new Page<>(current, 12);
        return borrowMapper.selectBorrowsByUser(page, userId);
    }

    @Override
    public IPage<BorrowVO> getMyBorrows(int current) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getBorrowsByUser(userId, current);
    }

    @Override
    public IPage<BorrowVO> getMyIncompleteBorrows(int current) {
        Page<BorrowVO> page = new Page<>(current, 12);
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return borrowMapper.selectMyIncompleteBorrows(page, userId);
    }

    @Override
    public boolean createBorrow(Borrow borrow) {
        User user = userMapper.selectById(borrow.getUserId());
        Collection collection = collectionMapper.selectById(borrow.getCollectionId());

        if (user == null || collection == null || !collection.getIsBorrowable()) {
            return false;
        }

        borrow.setBorrowDate(Instant.now());
        borrow.setReturnDeadline(calculateReturnDeadline(user.getId(), borrow.getBorrowDate()));
        borrow.setRenewedTimes(0);
        borrow.setFinePaid(BigDecimal.ZERO);

        boolean result = save(borrow);
        System.out.println("result:" + result);

        if (result) {
            collection.setIsBorrowable(false);
            collectionMapper.updateById(collection);
        }

        return result;
    }

    @Override
    @Transactional
    public boolean updateBorrow(Borrow borrow, Boolean collectionBorrowable) {
        Borrow existing = getById(borrow.getId());
        Collection collection = collectionMapper.selectById(existing.getCollectionId());

        if (existing == null) return false;

        if (collection == null || collection.getIsBorrowable()) {
            return false;
        }

        if (borrow.getReturnDate() != null && existing.getReturnDate() == null) {
            existing.setReturnDate(Instant.now());
            existing.setFinePaid(borrow.getFinePaid() != null ? borrow.getFinePaid() : BigDecimal.ZERO);
            collection.setIsBorrowable(true);
            collectionMapper.updateById(collection);
        } else if (!borrow.getFinePaid().equals(new BigDecimal(0))) {
            existing.setFinePaid(borrow.getFinePaid());
        } else if (borrow.getRenewedTimes() != null) {
            System.out.println(borrow.getUserId() + existing.getUserId());
            if (!Objects.equals(borrow.getUserId(), existing.getUserId())) {
                throw new IllegalArgumentException("续借用户不一致");
            }
            User user = userMapper.selectById(existing.getUserId());
            BorrowRule rule = borrowRuleMapper.selectOne(
                    new LambdaQueryWrapper<BorrowRule>().eq(BorrowRule::getRoleName, user.getRoleName())
            );
            if (rule == null) {
                throw new IllegalStateException("借阅规则不存在");
            }
            if (borrow.getRenewedTimes() > rule.getRenewTimes()) {
                throw new IllegalStateException("已达到续借上限");
            }

            existing.setRenewedTimes(borrow.getRenewedTimes());
            existing.setReturnDeadline(
                    calculateReturnDeadline(user.getId(), existing.getReturnDeadline())
            );
        }

        boolean result = updateById(existing);

        if (collectionBorrowable != null) {
            Collection coll = collectionMapper.selectById(existing.getCollectionId());
            if (coll != null) {
                coll.setIsBorrowable(collectionBorrowable);
                collectionMapper.updateById(coll);
            }
        }

        return result;
    }

    @Override
    public BorrowVO getBorrowByBarcode(String barcode) {
        CollectionServiceImpl.CollectionVO collection = collectionMapper.selectByBarcode(barcode);
        if (collection == null) {
            throw new IllegalArgumentException("找不到该条码对应的馆藏");
        }

        Borrow borrow = borrowMapper.selectOne(
                new LambdaQueryWrapper<Borrow>()
                        .eq(Borrow::getCollectionId, collection.id())
                        .isNull(Borrow::getReturnDate)
                        .last("LIMIT 1")
        );
        if (borrow == null) {
            throw new IllegalArgumentException("该馆藏当前无借出记录");
        }

        return new BorrowVO(
                borrow.getId(),
                borrow.getBorrowDate(),
                borrow.getUserId(),
                borrow.getCollectionId(),
                borrow.getReturnDeadline(),
                borrow.getReturnDate(),
                borrow.getRenewedTimes(),
                borrow.getFinePaid(),
                collection.isBorrowable()
        );
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
