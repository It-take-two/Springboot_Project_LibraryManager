package org.take2.librarymanager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.take2.librarymanager.model.User;
import org.take2.librarymanager.service.IUserService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    public record UserRequest(
            String username,
            String roleName,
            String name,
            String phone,
            Long classId,
            String userNumber
    ) {}

    public record UserResponse(
            Long userId,
            String username,
            String roleName,
            String name,
            String phone,
            Long classId,
            String userNumber,
            Instant createAt
    ) {}

    public record UserPageResponse(
            List<UserResponse> records,
            Long total
    ) {}

    private final IUserService userService;

    @GetMapping("/userInfo")
    public UserResponse getUserInfo() {
        return toResponse(userService.getMyUser());
    }

    @GetMapping("/userList")
    public UserPageResponse getUserList(@RequestParam int page) {
        IPage<User> iPage = userService.getUserPage(page);
        return new UserPageResponse(iPage.getRecords()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()),
                iPage.getTotal()
        );
    }

    @GetMapping("/adminList")
    public UserPageResponse getAdminList(@RequestParam int page) {
        IPage<User> iPage = userService.getUserPage(page);
        return new UserPageResponse(iPage.getRecords()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()),
                iPage.getTotal()
        );
    }

    @GetMapping("/userNumber")
    public UserResponse getUserByNumber(@RequestParam String userNumber) {
        User user = userService.getUserByUserNumber(userNumber);
        return toResponse(user);
    }

    @PostMapping("/add")
    public Boolean addUser(@RequestBody UserRequest body) {
        return userService.createUser(
                body.username,
                body.roleName,
                body.name,
                body.phone,
                body.classId,
                body.userNumber
        );
    }

    @PutMapping("/my")
    public Boolean updateMyUser(@RequestParam String phone) {
        return userService.updateMyUser(phone);
    }

    @PutMapping("/myPassword")
    public Boolean updateMyPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return userService.updatePassword(oldPassword, newPassword);
    }

    @PutMapping("/resetPassword")
    public Boolean ResetPassword(@RequestParam Long userId) {
        return userService.resetPassword(userId);
    }

    @PutMapping("/update")
    public Boolean updateUser(@RequestParam Long userId, @RequestBody UserRequest body) {
        return userService.updateUserById(
                userId,
                body.username,
                body.roleName,
                body.name,
                body.phone,
                body.classId,
                body.userNumber
        );
    }

    @DeleteMapping("/{id}")
    public Boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRoleName(),
                user.getName(),
                user.getPhone(),
                user.getClassId(),
                user.getUserNumber(),
                user.getCreatedAt()
        );
    }

}
