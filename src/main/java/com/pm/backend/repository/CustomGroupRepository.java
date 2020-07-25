package com.pm.backend.repository;


import com.pm.backend.controller.v1.request.UserJoinGroupRequest;

public interface CustomGroupRepository {
    String findGroupAddUser(UserJoinGroupRequest request);
}
