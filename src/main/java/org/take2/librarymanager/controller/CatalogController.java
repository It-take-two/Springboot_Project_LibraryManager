package org.take2.librarymanager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.take2.librarymanager.model.Catalog;
import org.take2.librarymanager.service.ICatalogService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final ICatalogService catalogService;

    public record CatalogRequest(
            String name,
            String isbn,
            String publisher,
            String category,
            Instant publishDate,
            String author,
            BigDecimal value
    ) {}

    public record CatalogResponse(
            Long id,
            String name,
            String isbn,
            String publisher,
            String category,
            Instant publishDate,
            String author,
            BigDecimal value
    ) {}

    public record CatalogPageResponse(
            List<CatalogResponse> records,
            Long total
    ) {}

    @GetMapping("/list")
    public CatalogPageResponse getCatalogList(@RequestParam int page) {
        Page<Catalog> catalogPage = catalogService.getCatalogPage(page);
        return new CatalogPageResponse(
                catalogPage.getRecords()
                .stream()
                .map(catalog -> new CatalogResponse(
                        catalog.getId(),
                        catalog.getName(),
                        catalog.getIsbn(),
                        catalog.getPublisher(),
                        catalog.getCategory(),
                        catalog.getPublishDate(),
                        catalog.getAuthor(),
                        catalog.getValue()))
                .collect(Collectors.toList()),
                catalogPage.getTotal()
        );
    }

    @PostMapping("/add")
    public boolean addCatalog(@RequestBody CatalogRequest req) {
        return catalogService.createCatalog(
                req.name(),
                req.isbn(),
                req.publisher(),
                req.category(),
                req.publishDate(),
                req.author(),
                req.value()
        );
    }

    @PutMapping("/update")
    public boolean updateCatalog(@RequestParam Long id, @RequestBody CatalogRequest req) {
        return catalogService.updateCatalog(
                id,
                req.name(),
                req.isbn(),
                req.publisher(),
                req.category(),
                req.publishDate(),
                req.author(),
                req.value()
        );
    }

    @DeleteMapping("/{id}")
    public boolean deleteCatalog(@PathVariable Long id) {
        return catalogService.deleteCatalog(id);
    }

    @GetMapping("/{id}")
    public CatalogResponse getCatalog(@PathVariable Long id) {
        Catalog catalog = catalogService.getCatalogById(id);
        if (catalog == null) {
            return null; // 可根据需求改为返回错误信息或抛异常
        }
        return new CatalogResponse(
                catalog.getId(),
                catalog.getName(),
                catalog.getIsbn(),
                catalog.getPublisher(),
                catalog.getCategory(),
                catalog.getPublishDate(),
                catalog.getAuthor(),
                catalog.getValue());
    }

    @GetMapping("/isbn")
    public CatalogResponse getCatalogByIsbn(@RequestParam String isbn) {
        Catalog catalog = catalogService.getCatalogByIsbn(isbn);
        if (catalog == null) {
            return null; // 可根据需求改为返回错误信息或抛异常
        }
        return new CatalogResponse(
                catalog.getId(),
                catalog.getName(),
                catalog.getIsbn(),
                catalog.getPublisher(),
                catalog.getCategory(),
                catalog.getPublishDate(),
                catalog.getAuthor(),
                catalog.getValue());
    }
}
