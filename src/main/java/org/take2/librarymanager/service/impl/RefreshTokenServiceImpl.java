package org.take2.librarymanager.service.impl;

import org.take2.librarymanager.model.RefreshToken;
import org.take2.librarymanager.mapper.RefreshTokenMapper;
import org.take2.librarymanager.service.IRefreshTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl extends ServiceImpl<RefreshTokenMapper, RefreshToken> implements IRefreshTokenService {

}
