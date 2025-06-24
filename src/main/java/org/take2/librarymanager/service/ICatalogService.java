package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.Catalog;

import java.math.BigDecimal;
import java.time.Instant;

public interface ICatalogService extends IService<Catalog> {

    Page<Catalog> getCatalogPage(int current);

    boolean createCatalog(String name, String isbn, String publisher, String category, Instant publishDate, String author, BigDecimal value);

    boolean updateCatalog(Long id, String name, String isbn, String publisher, String category, Instant publishDate, String author, BigDecimal value);

    boolean deleteCatalog(Long id);

    Catalog getCatalogById(Long id);

    Catalog getCatalogByIsbn(String isbn);

}
