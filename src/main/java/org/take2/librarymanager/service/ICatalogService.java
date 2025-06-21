package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.Catalog;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface ICatalogService extends IService<Catalog> {

    Page<Catalog> getCatalogPage(int current);

    /**
     * 管理员新增图书目录
     * @return 操作是否成功
     */
    boolean createCatalog(String name, String isbn, String publisher, String category, Instant publishDate, String author, BigDecimal value);

    /**
     * 管理员更新图书目录
     */
    boolean updateCatalog(Long id, String name, String isbn, String publisher, String category, Instant publishDate, String author, BigDecimal value);

    /**
     * 管理员删除图书目录
     */
    boolean deleteCatalog(Long id);

    /**
     * 根据 ID 获取图书目录详情
     */
    Catalog getCatalogById(Long id);
}
