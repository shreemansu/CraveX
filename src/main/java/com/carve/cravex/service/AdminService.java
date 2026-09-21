package com.carve.cravex.service;

import com.carve.cravex.entity.User;

import java.util.List;

public interface AdminService {

    String approveUserService(Long userId);

    String rejectUserService(Long userId);

    List<User> showAllPendingUsers();
}
