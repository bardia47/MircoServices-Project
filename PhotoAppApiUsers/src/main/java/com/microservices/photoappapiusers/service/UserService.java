package com.microservices.photoappapiusers.service;

import com.microservices.photoappapiusers.data.UserEntity;
import com.microservices.photoappapiusers.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto dto);

    UserEntity loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDto getUserDetailsByEmail(String email);

    UserDto getUserByUserId(String userId);
}
