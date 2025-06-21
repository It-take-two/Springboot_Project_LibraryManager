package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.Collection;
import org.take2.librarymanager.service.impl.CollectionServiceImpl;

import java.util.List;

public interface ICollectionService extends IService<Collection> {

    /**
     * 分页查询馆藏，支持对书名、作者、出版社模糊查询以及类别筛选
     *
     * @param current 当前页码
     * @param keyword 模糊搜索关键字
     * @param bigCategory 分类大类筛选（如 "A"）
     * @param subCategory 分类小类筛选（如 "A1"）
     * @return 分页结果，内部数据封装在 CollectionVO record 中
     */
    Page<CollectionServiceImpl.CollectionVO> searchCollections(
            int current, String keyword, String bigCategory, String subCategory);

    /**
     * 根据 barcode 精确查询馆藏
     *
     * @param barcode 图书馆藏条形码
     * @return 返回查询结果，封装在 CollectionVO record 中
     */
    CollectionServiceImpl.CollectionVO getCollectionByBarcode(String barcode);

    /**
     * 随机查询几本可借图书（isBorrowable 为 true）
     *
     * @param count 返回记录数
     * @return 可借图书集合，封装在 CollectionVO record 中
     */
    List<CollectionServiceImpl.CollectionVO> getRandomBorrowableCollections(int count);

    /**
     * 管理员新增馆藏（必须关联已存在的图书目录），自动生成 barcode
     *
     * @param catalogId 图书目录 ID
     * @return 操作成功返回 true，否则 false
     */
    boolean createCollection(Long catalogId);

    /**
     * 管理员删除馆藏
     *
     * @param id 馆藏记录 ID
     * @return 操作成功返回 true，否则 false
     */
    boolean deleteCollection(Long id);
}
