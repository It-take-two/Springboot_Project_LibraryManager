package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.take2.librarymanager.model.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ICollectionService extends IService<Collection> {
    /**
     * 管理员新增馆藏
     * 新增馆藏前须确保传入的 catalogId 在图书目录中已存在，
     * 新增时自动生成 barcode。
     */
    boolean createCollection(Collection collection);

    /**
     * 管理员更新馆藏信息
     */
    boolean updateCollection(Collection collection);

    /**
     * 管理员删除馆藏
     */
    boolean deleteCollection(Long id);

    /**
     * 分页模糊查询馆藏
     * 支持按书名、作者、出版社（模糊匹配，关联图书目录）以及类别筛选
     */
    IPage<Collection> searchCollections(Page<Collection> page, String keyword, String category);

    /**
     * 管理员通过 barcode 查询馆藏
     */
    Collection getCollectionByBarcode(String barcode);

    /**
     * 普通用户随机查询几本可借馆藏（is_borrowable = true）
     */
    List<Collection> getRandomAvailableCollections(int count);
}
