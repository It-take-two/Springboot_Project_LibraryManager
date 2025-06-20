package org.take2.librarymanager.service.impl;

import org.take2.librarymanager.model.Borrow;
import org.take2.librarymanager.mapper.BorrowMapper;
import org.take2.librarymanager.service.IBorrowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements IBorrowService {

}
