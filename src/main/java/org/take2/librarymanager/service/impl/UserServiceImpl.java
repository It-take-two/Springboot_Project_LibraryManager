package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.take2.librarymanager.mapper.UserMapper;
import org.take2.librarymanager.model.User;
import org.take2.librarymanager.service.IUserService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    // 判断操作者是否为管理员（role_name 为 "admin"）
    private boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRoleName());
    }

    // 判断操作者是否为超级管理员（用户名为 "root"）
    private boolean isRoot(User user) {
        return user != null && "root".equals(user.getUsername());
    }

    /**
     * 管理员查找普通用户，返回 role_name 不为 "admin" 的用户列表
     */
    @Override
    public List<User> adminListNormalUsers(User operator) {
        if (!isAdmin(operator)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("role_name", "admin");
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 管理员新增用户：
     * 如果新增用户的 role_name 为 "admin" 则只有 root 才能操作，否则任一管理员均可操作
     */
    @Override
    public boolean adminCreateUser(User operator, User user) {
        if (!isAdmin(operator)) {
            return false;
        }
        if ("admin".equals(user.getRoleName()) && !isRoot(operator)) {
            return false;
        }
        return baseMapper.insert(user) > 0;
    }

    /**
     * 管理员修改用户信息：
     * - 修改普通用户信息时任一管理员均可操作；
     * - 修改管理员信息（role_name 为 "admin"）时，除非操作者正修改自己的信息，否则必须由 root 操作。
     */
    @Override
    public boolean adminUpdateUser(User operator, User user) {
        if (!isAdmin(operator) || user == null || user.getId() == null) {
            return false;
        }
        User target = baseMapper.selectById(user.getId());
        if (target == null) {
            return false;
        }
        if ("admin".equals(target.getRoleName())
                && !isRoot(operator)
                && !operator.getId().equals(target.getId())) {
            return false;
        }
        return baseMapper.updateById(user) > 0;
    }

    /**
     * 管理员删除用户：
     * 如果目标用户为管理员，则只有 root 才能删除。
     */
    @Override
    public boolean adminDeleteUser(User operator, Long userId) {
        if (!isAdmin(operator)) {
            return false;
        }
        User target = baseMapper.selectById(userId);
        if (target == null) {
            return false;
        }
        if ("admin".equals(target.getRoleName()) && !isRoot(operator)) {
            return false;
        }
        return baseMapper.deleteById(userId) > 0;
    }

    /**
     * 普通用户获取个人详细信息
     */
    @Override
    public User getMyInfo(Long userId) {
        return baseMapper.selectById(userId);
    }

    /**
     * 普通用户或管理员修改个人资料，仅允许修改密码和电话
     */
    @Override
    public boolean updateMyProfile(Long userId, String newPassword, String newPhone) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(newPassword);
        }
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            user.setPhone(newPhone);
        }
        return baseMapper.updateById(user) > 0;
    }

    @Override
    public IPage<User> adminListNormalUsers(Page<User> page, User operator) {
        if (!isAdmin(operator)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 只显示普通用户（role_name 不为 admin）
        queryWrapper.ne("role_name", "admin");
        return baseMapper.selectPage(page, queryWrapper);
    }

}
