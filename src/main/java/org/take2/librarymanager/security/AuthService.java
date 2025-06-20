package org.take2.librarymanager.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.take2.librarymanager.mapper.RefreshTokenMapper;
import org.take2.librarymanager.mapper.UserMapper;
import org.take2.librarymanager.model.RefreshToken;
import org.take2.librarymanager.model.User;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final HashEncoder hashEncoder;

    public AuthService(JwtService jwtService,
                       UserMapper userMapper,
                       RefreshTokenMapper refreshTokenMapper,
                       HashEncoder hashEncoder) {
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.refreshTokenMapper = refreshTokenMapper;
        this.hashEncoder = hashEncoder;
    }

    public record TokenPair(String accessToken, String refreshToken) {}

    public User register(String username, String password) {
        String trimmed = username.trim();
        User existing = userMapper.selectByUserName(trimmed);
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        User user = new User();
        user.setUsername(trimmed);
        user.setPassword(hashEncoder.encode(password));
        userMapper.insert(user);
        return user;
    }

    public TokenPair login(String username, String password) {
        User user = userMapper.selectByUserName(username.trim());
        if (user == null || !hashEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String accessToken = jwtService.generateAccessToken(user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        storeRefreshToken(user.getId(), refreshToken);
        return new TokenPair(accessToken, refreshToken);
    }

    @Transactional
    public TokenPair refresh(String token) {
        if (!jwtService.validateRefreshToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
        Long userId = jwtService.getUserIdFromToken(token);
        String hashed = hashToken(token);
        RefreshToken record = refreshTokenMapper.selectByUserIdAndHashedToken(userId, hashed);
        if (record == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token not found");
        }
        refreshTokenMapper.deleteByUserIdAndHashedToken(userId, hashed);

        String newAccessToken = jwtService.generateAccessToken(userId);
        String newRefreshToken = jwtService.generateRefreshToken(userId);
        storeRefreshToken(userId, newRefreshToken);
        return new TokenPair(newAccessToken, newRefreshToken);
    }

    @Scheduled(fixedRate = 60000)
    public void cleanExpiredRefreshTokens() {
        LambdaQueryWrapper<RefreshToken> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(RefreshToken::getExpiredAt, Instant.now());
        refreshTokenMapper.delete(wrapper);
    }

    private void storeRefreshToken(Long userId, String token) {
        RefreshToken entity = new RefreshToken();
        entity.setUserId(userId);
        entity.setHashedToken(hashToken(token));
        entity.setExpiredAt(Instant.now().plusMillis(jwtService.refreshTokenValidityMs));
        refreshTokenMapper.insert(entity);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(digest.digest(token.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Hash error", e);
        }
    }
}