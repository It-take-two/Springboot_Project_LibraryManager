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

    /**
     * 请求体：新增或更新图书目录时使用的字段
     */
    public record CatalogRequest(
            String name,
            String isbn,
            String publisher,
            String category,
            Instant publishDate,
            String author,
            BigDecimal value
    ) {}

    /**
     * 响应体：返回图书目录的详细信息
     */
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

    /**
     * 分页查询图书目录（GET /catalog/list?page=1）
     */
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


    /**
     * 管理员新增图书目录（POST /catalog/add）
     */
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

    /**
     * 管理员更新图书目录（PUT /catalog/update?id=1）
     */
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

    /**
     * 管理员删除图书目录（DELETE /catalog/{id}）
     */
    @DeleteMapping("/{id}")
    public boolean deleteCatalog(@PathVariable Long id) {
        return catalogService.deleteCatalog(id);
    }

    /**
     * 根据 ID 获取图书目录详情（GET /catalog/{id}）
     */
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
}
