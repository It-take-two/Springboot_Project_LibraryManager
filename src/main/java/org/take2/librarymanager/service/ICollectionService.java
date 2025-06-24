package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.Collection;
import org.take2.librarymanager.service.impl.CollectionServiceImpl;

import java.util.List;

public interface ICollectionService extends IService<Collection> {

    IPage<CollectionServiceImpl.CollectionVO> searchCollections(
            int current, String keyword, String bigCategory, String subCategory);

    CollectionServiceImpl.CollectionVO getCollectionByBarcode(String barcode);

    CollectionServiceImpl.CollectionVO getCollectionById(Long collectionId);

    List<CollectionServiceImpl.CollectionVO> getRandomBorrowableCollections(int count);

    boolean createCollection(Long catalogId);

    boolean deleteCollection(Long id);
}
