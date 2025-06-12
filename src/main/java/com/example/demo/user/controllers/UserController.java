package com.example.demo.user.controllers;

import com.cloudinary.Api;
import com.example.demo.authentication.controllers.dtos.RegisterRequest;
import com.example.demo.common.ApiResponse;

import com.example.demo.common.annotation.positiveOrDefault.PositiveOrDefault;
import com.example.demo.user.controllers.dtos.DeleteUsersRequest;
import com.example.demo.user.controllers.dtos.PassUpdateRequest;
import com.example.demo.user.controllers.dtos.RoleUserUpdateRequest;
import com.example.demo.user.controllers.dtos.UserResponse;
import com.example.demo.user.service.IUserService;
import com.example.demo.user.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    IUserService<RegisterRequest> userService;


    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll(
            @PositiveOrDefault Integer page,
            @PositiveOrDefault(defaultValue = 10) Integer limit,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String isActive,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        return ResponseEntity.ok(userService.findAll(page, limit, name, role, isActive, sortBy, sortDir));
    }

    @DeleteMapping("/users")
    public ResponseEntity<ApiResponse<Void>> delete(@RequestBody @Valid DeleteUsersRequest request) {

        return ResponseEntity.ok(userService.deleteUsers(request));
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse<Void>> updateRole(@RequestBody @Valid RoleUserUpdateRequest request) {
         System.out.println(request);
        return ResponseEntity.ok(userService.updateRoleUsers(request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody @Valid PassUpdateRequest request) {

        return ResponseEntity.ok(userService.updatePassUser(request));
    }


}
