package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
import com.example.demo.service.SessionUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionUserServiceImpl implements SessionUser {

    @Override
    public String getCurrentUserUsername() {
        return ((UserDto) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
