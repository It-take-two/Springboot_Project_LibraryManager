package org.take2.librarymanager.controller;


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

    public record AdminUserRequest(
            String username,
            String roleName,
            String name,
            String phone,
            Long classId,
            String userNumber
    ) {}

    public record UserResponse(
            String username,
            String roleName,
            String name,
            String phone,
            Long classId,
            String userNumber,
            Instant createAt
    ) {}

    private final IUserService userService;

    @GetMapping("/userInfo")
    public UserResponse getUserInfo() {
        return toResponse(userService.getMyUser());
    }

    @GetMapping("/userList")
    public List<UserResponse> getUserList(@RequestParam int page) {
        return userService.getUserPage(page).getRecords()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/adminList")
    public List<UserResponse> getAdminList(@RequestParam int page) {
        return userService.getAdminPage(page).getRecords()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public Boolean addUser(@RequestBody AdminUserRequest body) {
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
    public Boolean updateUser(@RequestParam Long userId, @RequestBody AdminUserRequest body) {
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
