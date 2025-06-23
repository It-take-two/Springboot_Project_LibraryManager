package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.take2.librarymanager.mapper.UserMapper;
import org.take2.librarymanager.model.BorrowRule;
import org.take2.librarymanager.mapper.BorrowRuleMapper;
import org.take2.librarymanager.model.User;
import org.take2.librarymanager.service.IBorrowRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class BorrowRuleServiceImpl extends ServiceImpl<BorrowRuleMapper, BorrowRule> implements IBorrowRuleService {

}
