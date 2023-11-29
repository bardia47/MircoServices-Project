package com.microservices.photoappapiusers.service;

import com.microservices.photoappapiusers.shared.UserDto;

public interface UserService{
    UserDto createUser(UserDto dto);
}
