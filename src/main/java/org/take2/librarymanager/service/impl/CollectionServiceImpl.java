package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.common.util.StringUtils;
import org.take2.librarymanager.mapper.CatalogMapper;
import org.take2.librarymanager.mapper.CollectionMapper;
import org.take2.librarymanager.model.Catalog;
import org.take2.librarymanager.model.Collection;
import org.take2.librarymanager.service.ICollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements ICollectionService {

    @Autowired
    private CatalogMapper catalogMapper;

    /**
     * 管理员新增馆藏：
     * ① 检查 collection 对象非空且 catalogId 不为空
     * ② 校验该 catalogId 对应的图书目录是否存在
     * ③ 自动生成 barcode（例如 "BC"+当前时间+随机数）
     * ④ 插入记录
     */
    @Override
    public boolean createCollection(Collection collection) {
        if(collection == null || collection.getCatalogId() == null) {
            return false;
        }
        // 校验图书目录是否存在
        Catalog catalog = catalogMapper.selectById(collection.getCatalogId());
        if(catalog == null) {
            return false;
        }
        // 自动生成 barcode
        String barcode = generateBarcode();
        collection.setBarcode(barcode);
        return baseMapper.insert(collection) > 0;
    }

    /**
     * 生成 barcode：拼接当前时间和一个四位随机数
     */
    private String generateBarcode() {
        long timeMillis = System.currentTimeMillis();
        int randomNum = new Random().nextInt(9000) + 1000; // 生成 1000~9999 范围内的随机整数
        return "BC" + timeMillis + randomNum;
    }

    @Override
    public boolean updateCollection(Collection collection) {
        if(collection == null || collection.getId() == null) {
            return false;
        }
        return baseMapper.updateById(collection) > 0;
    }

    @Override
    public boolean deleteCollection(Long id) {
        if(id == null) {
            return false;
        }
        return baseMapper.deleteById(id) > 0;
    }

    /**
     * 分页查询馆藏：
     * ① 如果传入 keyword 或 category，则先在图书目录中进行查询（书名、作者、出版社模糊匹配，并可按类别筛选）
     * ② 将符合条件的 Catalog 的 id 收集，并作为过滤条件查询馆藏记录
     * ① 若无匹配，返回空的分页结果
     */
    @Override
    public IPage<Collection> searchCollections(Page<Collection> page, String keyword, String category) {
        // 构建图书目录查询条件
        QueryWrapper<Catalog> catalogQuery = new QueryWrapper<>();
        if(StringUtils.isNotBlank(keyword)) {
            catalogQuery.lambda()
                    .like(Catalog::getName, keyword)
                    .or().like(Catalog::getAuthor, keyword)
                    .or().like(Catalog::getPublisher, keyword);
        }
        if(StringUtils.isNotBlank(category)) {
            catalogQuery.lambda().eq(Catalog::getCategory, category);
        }
        List<Catalog> catalogs = catalogMapper.selectList(catalogQuery);
        if(catalogs == null || catalogs.isEmpty()){
            Page<Collection> emptyPage = new Page<>(page.getCurrent(), page.getSize(), 0);
            emptyPage.setRecords(null);
            return emptyPage;
        }
        List<Long> catalogIds = catalogs.stream().map(Catalog::getId).collect(Collectors.toList());
        QueryWrapper<Collection> collectionQuery = new QueryWrapper<>();
        collectionQuery.in("catalog_id", catalogIds);
        return baseMapper.selectPage(page, collectionQuery);
    }

    /**
     * 管理员通过 barcode 查询馆藏，直接在馆藏表中匹配 barcode
     */
    @Override
    public Collection getCollectionByBarcode(String barcode) {
        if(StringUtils.isBlank(barcode)){
            return null;
        }
        QueryWrapper<Collection> query = new QueryWrapper<>();
        query.eq("barcode", barcode);
        return baseMapper.selectOne(query);
    }

    /**
     * 普通用户随机查询可借图书：
     * 调用 MySQL 的 RAND() 排序并限制返回条数
     */
    @Override
    public List<Collection> getRandomAvailableCollections(int count) {
        QueryWrapper<Collection> query = new QueryWrapper<>();
        query.eq("is_borrowable", true);
        query.last("ORDER BY RAND() LIMIT " + count);
        return baseMapper.selectList(query);
    }
}
