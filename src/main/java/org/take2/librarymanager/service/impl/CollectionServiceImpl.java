package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.take2.librarymanager.mapper.CollectionMapper;
import org.take2.librarymanager.mapper.CatalogMapper;
import org.take2.librarymanager.mapper.UserMapper;
import org.take2.librarymanager.model.Collection;
import org.take2.librarymanager.model.Catalog;
import org.take2.librarymanager.model.User;
import org.take2.librarymanager.service.ICollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements ICollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 检查当前登录用户是否为管理员（要求 role_name 为 "admin"）
     */
    private boolean isAuthorized() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userMapper.selectById(userId);
        return currentUser != null && "admin".equals(currentUser.getRoleName());
    }

    /**
     * 自动生成 barcode；示例规则：B + 当前时间戳 + 4位随机数字
     */
    private String generateBarcode() {
        long timestamp = System.currentTimeMillis();
        int randomNum = new Random().nextInt(9000) + 1000;
        return "B" + timestamp + randomNum;
    }

    @Override
    public IPage<CollectionVO> searchCollections(int current, String keyword, String bigCategory, String subCategory) {
        Page<CollectionVO> page = new Page<>(current, 12);
        return collectionMapper.searchCollections(page, keyword, bigCategory, subCategory);
    }

    @Override
    public CollectionVO getCollectionByBarcode(String barcode) {
        return collectionMapper.selectByBarcode(barcode);
    }

    @Override
    public CollectionVO getCollectionById(Long collectionId) { return collectionMapper.getById(collectionId); }

    @Override
    public List<CollectionVO> getRandomBorrowableCollections(int count) {
        return collectionMapper.selectRandomBorrowable(count);
    }

    @Override
    public boolean createCollection(Long catalogId) {
        if (!isAuthorized()) {
            return false;
        }
        Catalog catalog = catalogMapper.selectById(catalogId);
        if (catalog == null) {
            return false;
        }
        Collection collection = new Collection();
        collection.setCatalogId(catalogId)
                .setBarcode(generateBarcode())
                .setIsBorrowable(true);
        return save(collection);
    }

    @Override
    public boolean deleteCollection(Long id) {
        if (!isAuthorized()) {
            return false;
        }
        return removeById(id);
    }

    /**
     * 内部定义的 CollectionVO record，用于封装查询时关联的馆藏和图书目录信息。
     * 注意：在 CollectionMapper.xml 中对相关查询的 resultType 必须修改为：
     * "org.take2.librarymanager.service.impl.CollectionServiceImpl$CollectionVO"
     */
    public static record CollectionVO(
            Long id,
            String barcode,
            Boolean isBorrowable,
            Instant storageDate,
            Long catalogId,
            String name,
            String isbn,
            String publisher,
            String category,
            Instant publishDate,
            String author,
            java.math.BigDecimal value
    ) {}
}
