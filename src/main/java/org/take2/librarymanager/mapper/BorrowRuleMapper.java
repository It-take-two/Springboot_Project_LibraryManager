package org.take2.librarymanager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.take2.librarymanager.model.BorrowRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface BorrowRuleMapper extends BaseMapper<BorrowRule> {

}
