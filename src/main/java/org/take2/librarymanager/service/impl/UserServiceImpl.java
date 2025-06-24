package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.take2.librarymanager.mapper.ClassMapper;
import org.take2.librarymanager.mapper.UserMapper;
import org.take2.librarymanager.model.User;
import org.take2.librarymanager.security.AuthService;
import org.take2.librarymanager.security.HashEncoder;
import org.take2.librarymanager.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private AuthService authService;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HashEncoder hashEncoder;

    private Boolean isRoot() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getById(userId).getUsername().equals("root");
    }

    private Boolean isAuthorized() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getById(userId).getRoleName().equals("admin");
    }

    @Override
    public User getMyUser() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getById(userId);
    }

    @Override
    public Page<User> getUserPage(int current) {
        if (!isAuthorized()) throw new IllegalArgumentException("权限不足");
        Page<User> page = new Page<>(current, 12);
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<User>()
                .ne(User::getRoleName, "admin");
        return this.page(page, query);
    }

    @Override
    public Page<User> getAdminPage(int current) {
        if (!isRoot()) throw new IllegalArgumentException("权限不足");
        Page<User> page = new Page<>(current, 12);
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<User>()
                .eq(User::getRoleName, "admin");
        return this.page(page, query);
    }

    @Override
    public User getUserByUserNumber(String userNumber) {
        return userMapper.selectByUserNumber(userNumber);
    }

    @Override
    public Boolean createUser(String username, String roleName, String name, String phone, Long classId, String userNumber) {
        if (!isAuthorized()) {
            return false;
        }

        if (!isRoot() && roleName.equals("admin")) {
            return false;
        }

        if (classId != null && classMapper.selectById(classId) == null) {
            return false;
        }

        if (userMapper.selectByUserNumber(userNumber) != null) {
            return false;
        }

        User user = authService.register(username, "123456");
        if (user == null) {
            return false;
        }

        user.setRoleName(roleName)
                .setName(name)
                .setPhone(phone)
                .setClassId(classId)
                .setUserNumber(userNumber);

        return updateById(user);
    }

    @Override
    public Boolean updateMyUser(String phone) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getById(userId);
        if (user == null) return false;
        user.setPhone(phone);
        return updateById(user);
    }

    @Override
    public Boolean updatePassword(String oldPassword, String newPassword) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getById(userId);
        if (user == null || !hashEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(hashEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public Boolean resetPassword(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        if (!isAuthorized() || user.getRoleName().equals("admin")) {
            return false;
        }
        user.setPassword(hashEncoder.encode("123456"));
        return updateById(user);
    }

    @Override
    public Boolean updateUserById(Long userId, String username, String roleName, String name, String phone, Long classId, String userNumber) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        if (!isAuthorized() || (user.getRoleName().equals("admin") && !isRoot())) {
            return false;
        }
        user.setRoleName(roleName)
                .setName(name)
                .setPhone(phone)
                .setClassId(classId)
                .setUserNumber(userNumber);
        return updateById(user);
    }

    @Override
    public Boolean deleteUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        if (!isAuthorized()) {
            return false;
        }
        if (!isRoot() && user.getRoleName().equals("admin")) {
            return false;
        }
        return removeById(userId);
    }

}
