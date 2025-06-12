package com.example.demo.user.service;

import com.example.demo.common.ApiResponse;
import com.example.demo.user.controllers.dtos.DeleteUsersRequest;
import com.example.demo.user.controllers.dtos.PassUpdateRequest;
import com.example.demo.user.controllers.dtos.RoleUserUpdateRequest;
import com.example.demo.user.controllers.dtos.UserResponse;

import java.util.List;

public interface IUserService<T> {

    ApiResponse<List<UserResponse>> findAll(Integer page, Integer limit, String name, String role, String isActive, String sortBy, String sortDir);

    ApiResponse<Void> createUser(T registerRequest);

    ApiResponse<Void> deleteUsers(DeleteUsersRequest deleteUsersRequest);

    ApiResponse<Void> updateRoleUsers(RoleUserUpdateRequest roleUserUpdateRequest);

    ApiResponse<Void> updatePassUser(PassUpdateRequest passUpdateRequest);
}
