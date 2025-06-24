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

    IPage<CollectionServiceImpl.CollectionVO> searchCollections(Page<?> page,
                                                                @Param("keyword") String keyword,
                                                                @Param("bigCategory") String bigCategory,
                                                                @Param("subCategory") String subCategory);

    org.take2.librarymanager.service.impl.CollectionServiceImpl.CollectionVO selectByBarcode(@Param("barcode") String barcode);

    org.take2.librarymanager.service.impl.CollectionServiceImpl.CollectionVO getById(@Param("id") Long id);

    List<org.take2.librarymanager.service.impl.CollectionServiceImpl.CollectionVO> selectRandomBorrowable(@Param("count") int count);
}
