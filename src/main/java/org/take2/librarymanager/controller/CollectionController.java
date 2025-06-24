package org.take2.librarymanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

    public record CollectionAddRequest(Long catalogId) {}

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

    @PostMapping("/add")
    public boolean addCollection(@RequestBody CollectionAddRequest request) {
        return collectionService.createCollection(request.catalogId());
    }

    @DeleteMapping("/{id}")
    public boolean deleteCollection(@PathVariable Long id) {
        return collectionService.deleteCollection(id);
    }
}
