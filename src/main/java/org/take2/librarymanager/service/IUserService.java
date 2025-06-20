package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.User;
import java.util.List;

public interface IUserService extends IService<User> {
    /**
     * 管理员查找普通用户（即 role_name 不为 "admin" 的用户）
     */
    List<User> adminListNormalUsers(User operator);

    /**
     * 管理员新增用户：
     * - 若新增用户为普通用户，任一管理员均可操作；
     * - 若新增用户为管理员（role_name 为 "admin"），则必须由用户名为 "root" 的管理员操作。
     */
    boolean adminCreateUser(User operator, User user);

    /**
     * 管理员修改用户信息：
     * - 普通管理员只能修改普通用户的信息；
     * - 修改管理员信息（role_name 为 "admin"）时，除非是修改自己的密码，否则必须是由 root 操作。
     */
    boolean adminUpdateUser(User operator, User user);

    /**
     * 管理员删除用户：
     * - 普通管理员只能删除普通用户；
     * - 删除管理员账号时，只有 root 才有权限进行操作。
     */
    boolean adminDeleteUser(User operator, Long userId);

    /**
     * 普通用户获取个人详细信息
     */
    User getMyInfo(Long userId);

    /**
     * 普通用户或管理员修改自己的密码和电话
     */
    boolean updateMyProfile(Long userId, String newPassword, String newPhone);

    IPage<User> adminListNormalUsers(Page<User> page, User operator);
}
