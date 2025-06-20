package org.take2.librarymanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.take2.librarymanager.model.Catalog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface CatalogMapper extends BaseMapper<Catalog> {

}
