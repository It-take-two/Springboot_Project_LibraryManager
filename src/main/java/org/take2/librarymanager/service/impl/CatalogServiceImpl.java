package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.take2.librarymanager.mapper.CatalogMapper;
import org.take2.librarymanager.model.Catalog;
import org.take2.librarymanager.service.ICatalogService;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl extends ServiceImpl<CatalogMapper, Catalog> implements ICatalogService {

    @Override
    public IPage<Catalog> searchCatalogs(Page<Catalog> page, String keyword) {
        QueryWrapper<Catalog> query = new QueryWrapper<>();
        if(StringUtils.isNotBlank(keyword)){
            query.lambda()
                    .like(Catalog::getName, keyword)
                    .or().like(Catalog::getAuthor, keyword)
                    .or().like(Catalog::getPublisher, keyword);
        }
        return baseMapper.selectPage(page, query);
    }
}
