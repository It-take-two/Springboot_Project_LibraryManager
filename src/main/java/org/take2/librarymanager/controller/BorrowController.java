package org.take2.librarymanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.take2.librarymanager.model.Borrow;
import org.take2.librarymanager.service.IBorrowService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.take2.librarymanager.service.ICollectionService;
import org.take2.librarymanager.service.impl.BorrowServiceImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    private final IBorrowService borrowService;

    public record BorrowResponse(
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

    public record BorrowPageResponse(
            List<BorrowResponse> records,
            Long total
    ) {}

    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public record BorrowAddRequest(
            Long userId,
            Long collectionId
    ) {}

    public record BorrowUpdateRequest(
            Long userId,
            Integer renewedTimes,
            BigDecimal finePaid,
            Instant returnDate,
            Boolean collectionIsBorrowable
    ) {}

    @GetMapping("/barcode")
    public BorrowResponse getBorrowByBarcode(@RequestParam String barcode) {
        BorrowServiceImpl.BorrowVO vo =  borrowService.getBorrowByBarcode(barcode);
        return new BorrowResponse(
                vo.id(),
                vo.borrowDate(),
                vo.userId(),
                vo.collectionId(),
                vo.returnDeadline(),
                vo.returnDate(),
                vo.renewedTimes(),
                vo.finePaid(),
                vo.collectionIsBorrowable()
        );
    }

    @GetMapping("/list")
    public BorrowPageResponse getAllBorrows(@RequestParam int page) {
        IPage<BorrowServiceImpl.BorrowVO> result = borrowService.getAllBorrows(page);
        return new BorrowPageResponse(result.getRecords().stream()
                .map(vo -> new BorrowResponse(
                        vo.id(),
                        vo.borrowDate(),
                        vo.userId(),
                        vo.collectionId(),
                        vo.returnDeadline(),
                        vo.returnDate(),
                        vo.renewedTimes(),
                        vo.finePaid(),
                        vo.collectionIsBorrowable()))
                .collect(Collectors.toList()),
                result.getTotal()
        );
    }

    @GetMapping("/incomplete")
    public BorrowPageResponse getIncompleteBorrows(@RequestParam int page) {
        IPage<BorrowServiceImpl.BorrowVO> result = borrowService.getIncompleteBorrows(page);
        return new BorrowPageResponse(result.getRecords().stream()
                .map(vo -> new BorrowResponse(
                        vo.id(),
                        vo.borrowDate(),
                        vo.userId(),
                        vo.collectionId(),
                        vo.returnDeadline(),
                        vo.returnDate(),
                        vo.renewedTimes(),
                        vo.finePaid(),
                        vo.collectionIsBorrowable()))
                .collect(Collectors.toList()),
                result.getTotal()
        );
    }

    @GetMapping("/user")
    public BorrowPageResponse getBorrowsByUser(@RequestParam Long userId, @RequestParam int page) {
        IPage<BorrowServiceImpl.BorrowVO> result = borrowService.getBorrowsByUser(userId, page);
        return new BorrowPageResponse(result.getRecords().stream()
                .map(vo -> new BorrowResponse(
                        vo.id(),
                        vo.borrowDate(),
                        vo.userId(),
                        vo.collectionId(),
                        vo.returnDeadline(),
                        vo.returnDate(),
                        vo.renewedTimes(),
                        vo.finePaid(),
                        vo.collectionIsBorrowable()))
                .collect(Collectors.toList()),
                result.getTotal()
        );
    }

    @GetMapping("/my")
    public BorrowPageResponse getMyBorrows(@RequestParam int page) {
        IPage<BorrowServiceImpl.BorrowVO> result = borrowService.getMyBorrows(page);
        return new BorrowPageResponse(result.getRecords().stream()
                .map(vo -> new BorrowResponse(
                        vo.id(),
                        vo.borrowDate(),
                        vo.userId(),
                        vo.collectionId(),
                        vo.returnDeadline(),
                        vo.returnDate(),
                        vo.renewedTimes(),
                        vo.finePaid(),
                        vo.collectionIsBorrowable()))
                .collect(Collectors.toList()),
                result.getTotal()
        );
    }

    @GetMapping("/my/incomplete")
    public BorrowPageResponse getMyIncompleteBorrows(@RequestParam int page) {
        IPage<BorrowServiceImpl.BorrowVO> result = borrowService.getMyIncompleteBorrows(page);
        return new BorrowPageResponse(result.getRecords().stream()
                .map(vo -> new BorrowResponse(
                        vo.id(),
                        vo.borrowDate(),
                        vo.userId(),
                        vo.collectionId(),
                        vo.returnDeadline(),
                        vo.returnDate(),
                        vo.renewedTimes(),
                        vo.finePaid(),
                        vo.collectionIsBorrowable()))
                .collect(Collectors.toList()),
                result.getTotal()
        );
    }

    @GetMapping("/overdue")
    public List<BorrowResponse> getMyOverdueBorrows() {
        List<BorrowServiceImpl.BorrowVO> result = borrowService.getOverdueAndUnpaidBorrowsByUser();
        return result.stream()
                .map(vo -> new BorrowResponse(
                        vo.id(),
                        vo.borrowDate(),
                        vo.userId(),
                        vo.collectionId(),
                        vo.returnDeadline(),
                        vo.returnDate(),
                        vo.renewedTimes(),
                        vo.finePaid(),
                        vo.collectionIsBorrowable()))
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public boolean addBorrow(@RequestBody BorrowAddRequest request) {
        Borrow borrow = new Borrow();
        borrow.setUserId(request.userId());
        borrow.setCollectionId(request.collectionId());
        return borrowService.createBorrow(borrow);
    }

    @PutMapping("/update")
    public boolean updateBorrow(@RequestParam Long borrowId, @RequestBody BorrowUpdateRequest request) {
        Borrow borrow = new Borrow();
        borrow.setUserId(request.userId());
        borrow.setId(borrowId);
        borrow.setRenewedTimes(request.renewedTimes());
        borrow.setFinePaid(request.finePaid());
        borrow.setReturnDate(request.returnDate());
        return borrowService.updateBorrow(borrow, request.collectionIsBorrowable());
    }

    @DeleteMapping("/{id}")
    public boolean deleteBorrow(@PathVariable Long id) {
        return borrowService.deleteBorrow(id);
    }
}
