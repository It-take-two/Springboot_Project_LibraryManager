package org.take2.librarymanager.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.take2.librarymanager.model.Collection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.take2.librarymanager.service.impl.CollectionServiceImpl;

import java.util.List;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {

    /**
     * 分页查询馆藏（连表查询 Catalog），支持模糊搜索与类别筛选
     * 备注：查询结果封装在 CollectionVO record 中
     */
    IPage<CollectionServiceImpl.CollectionVO> searchCollections(Page<?> page,
                                                                @Param("keyword") String keyword,
                                                                @Param("bigCategory") String bigCategory,
                                                                @Param("subCategory") String subCategory);

    /**
     * 根据 barcode 精确查询馆藏记录
     */
    org.take2.librarymanager.service.impl.CollectionServiceImpl.CollectionVO selectByBarcode(@Param("barcode") String barcode);

    /**
     * 根据 ID 查询馆藏记录
     */
    org.take2.librarymanager.service.impl.CollectionServiceImpl.CollectionVO getById(@Param("id") Long id);

    /**
     * 随机查询可借图书，利用 MySQL 的 ORDER BY RAND()
     */
    List<org.take2.librarymanager.service.impl.CollectionServiceImpl.CollectionVO> selectRandomBorrowable(@Param("count") int count);
}
