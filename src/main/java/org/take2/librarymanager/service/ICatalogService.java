package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.take2.librarymanager.model.Catalog;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ICatalogService extends IService<Catalog> {
    /**
     * 分页查询图书目录，
     * 支持根据图书名称、作者、出版社进行模糊查询
     */
    IPage<Catalog> searchCatalogs(Page<Catalog> page, String keyword);
}
