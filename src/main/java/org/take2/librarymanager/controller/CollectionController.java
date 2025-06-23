package org.take2.librarymanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.take2.librarymanager.service.ICollectionService;
import org.take2.librarymanager.service.impl.CollectionServiceImpl.CollectionVO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/collection")
public class CollectionController {

    private final ICollectionService collectionService;

    /**
     * 封装查询结果的响应体
     */
    public record CollectionResponse(
            Long id,
            String barcode,
            Boolean isBorrowable,
            Instant storageDate,
            Long catalogId,
            String name,
            String isbn,
            String publisher,
            String category,
            Instant publishDate,
            String author,
            BigDecimal value
    ) {}

    public record CollectionPageResponse(
            List<CollectionResponse> records,
            Long total
    ) {}

    /**
     * 新增馆藏时请求体，仅传入图书目录 ID
     */
    public record CollectionAddRequest(Long catalogId) {}

    /**
     * 分页查询馆藏
     * GET /collection/search?page=1&keyword=xxx&bigCategory=A&subCategory=A1
     */
    @GetMapping("/search")
    public CollectionPageResponse searchCollections(
            @RequestParam int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String bigCategory,
            @RequestParam(required = false) String subCategory) {
        IPage<CollectionVO> resultPage = collectionService.searchCollections(page, keyword, bigCategory, subCategory);
        return new CollectionPageResponse(resultPage.getRecords().stream()
                .map(vo -> new CollectionResponse(
                        vo.id(),
                        vo.barcode(),
                        vo.isBorrowable(),
                        vo.storageDate(),
                        vo.catalogId(),
                        vo.name(),
                        vo.isbn(),
                        vo.publisher(),
                        vo.category(),
                        vo.publishDate(),
                        vo.author(),
                        vo.value()))
                .collect(Collectors.toList()),
                resultPage.getTotal()
        );
    }

    /**
     * 管理员通过 barcode 查询馆藏
     * GET /collection/barcode?barcode=xxx
     */
    @GetMapping("/barcode")
    public CollectionResponse getCollectionByBarcode(@RequestParam String barcode) {
        CollectionVO vo = collectionService.getCollectionByBarcode(barcode);
        if (vo == null) return null;
        return new CollectionResponse(
                vo.id(),
                vo.barcode(),
                vo.isBorrowable(),
                vo.storageDate(),
                vo.catalogId(),
                vo.name(),
                vo.isbn(),
                vo.publisher(),
                vo.category(),
                vo.publishDate(),
                vo.author(),
                vo.value());
    }

    /**
     * 管理员通过 ID 查询馆藏
     * GET /collection/id?id=123
     */
    @GetMapping("/id")
    public CollectionResponse getCollectionById(@RequestParam Long id) {
        CollectionVO vo = collectionService.getCollectionById(id);
        if (vo == null) return null;
        return new CollectionResponse(
                vo.id(),
                vo.barcode(),
                vo.isBorrowable(),
                vo.storageDate(),
                vo.catalogId(),
                vo.name(),
                vo.isbn(),
                vo.publisher(),
                vo.category(),
                vo.publishDate(),
                vo.author(),
                vo.value()
        );
    }

    /**
     * 普通用户随机查询几本可借图书
     * GET /collection/random?count=3
     */
    @GetMapping("/random")
    public List<CollectionResponse> getRandomBorrowableCollections() {
        List<CollectionVO> list = collectionService.getRandomBorrowableCollections(9);
        return list.stream()
                .map(vo -> new CollectionResponse(
                        vo.id(),
                        vo.barcode(),
                        vo.isBorrowable(),
                        vo.storageDate(),
                        vo.catalogId(),
                        vo.name(),
                        vo.isbn(),
                        vo.publisher(),
                        vo.category(),
                        vo.publishDate(),
                        vo.author(),
                        vo.value()))
                .collect(Collectors.toList());
    }

    /**
     * 管理员新增馆藏
     * POST /collection/add
     * 请求体示例：{"catalogId": 1}
     */
    @PostMapping("/add")
    public boolean addCollection(@RequestBody CollectionAddRequest request) {
        return collectionService.createCollection(request.catalogId());
    }

    /**
     * 管理员删除馆藏
     * DELETE /collection/{id}
     */
    @DeleteMapping("/{id}")
    public boolean deleteCollection(@PathVariable Long id) {
        return collectionService.deleteCollection(id);
    }
}
