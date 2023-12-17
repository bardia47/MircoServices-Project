package com.microservices.photoappapiusers.service;

import com.microservices.photoappapiusers.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto dto);

    UserDto getUserDetailsByEmail(String email);
}
