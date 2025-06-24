package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.User;
import java.util.List;

public interface IUserService extends IService<User> {
    User getMyUser();

    User getUserById(Long id);

    Page<User> getUserPage(int current);

    Page<User> getAdminPage(int current);

    User getUserByUserNumber(String userNumber);

    Boolean createUser(String username, String roleName, String name, String phone, Long classId, String userNumber);

    Boolean updateMyUser(String phone);

    Boolean updatePassword(String oldPassword, String newPassword);

    Boolean resetPassword(Long userId);

    Boolean updateUserById(Long id, String username, String roleName, String name, String phone, Long classId, String userNumber);

    Boolean deleteUser(Long userId);
}
