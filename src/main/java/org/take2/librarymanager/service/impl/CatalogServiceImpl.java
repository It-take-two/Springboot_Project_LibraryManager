package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.take2.librarymanager.mapper.CatalogMapper;
import org.take2.librarymanager.mapper.UserMapper;
import org.take2.librarymanager.model.Catalog;
import org.take2.librarymanager.model.User;
import org.take2.librarymanager.service.ICatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class CatalogServiceImpl extends ServiceImpl<CatalogMapper, Catalog> implements ICatalogService {

    @Autowired
    private UserMapper userMapper; // 用于权限验证

    /**
     * 判断当前登录用户是否为管理员（role_name = "admin"）
     */
    private boolean isAuthorized() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userMapper.selectById(userId);
        return currentUser != null && "admin".equals(currentUser.getRoleName());
    }

    @Override
    public Page<Catalog> getCatalogPage(int current) {
        Page<Catalog> page = new Page<>(current, 12);
        return this.page(page);
    }

    @Override
    public boolean createCatalog(String name, String isbn, String publisher, String category, Instant publishDate, String author, BigDecimal value) {
        if (!isAuthorized()) {
            return false; // 非管理员无权限
        }
        // 检查是否已有相同的 ISBN（要求图书 ISBN 唯一）
        LambdaQueryWrapper<Catalog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Catalog::getIsbn, isbn);
        if (this.getOne(queryWrapper) != null) {
            return false;
        }
        Catalog catalog = new Catalog()
                .setName(name)
                .setIsbn(isbn)
                .setPublisher(publisher)
                .setCategory(category)
                .setPublishDate(publishDate)
                .setAuthor(author)
                .setValue(value);
        return this.save(catalog);
    }

    @Override
    public boolean updateCatalog(Long id, String name, String isbn, String publisher, String category, Instant publishDate, String author, BigDecimal value) {
        if (!isAuthorized()) {
            return false;
        }
        Catalog catalog = this.getById(id);
        if (catalog == null) {
            return false;
        }
        catalog.setName(name)
                .setIsbn(isbn)
                .setPublisher(publisher)
                .setCategory(category)
                .setPublishDate(publishDate)
                .setAuthor(author)
                .setValue(value);
        return this.updateById(catalog);
    }

    @Override
    public boolean deleteCatalog(Long id) {
        if (!isAuthorized()) {
            return false;
        }
        return this.removeById(id);
    }

    @Override
    public Catalog getCatalogById(Long id) {
        return this.getById(id);
    }
}
