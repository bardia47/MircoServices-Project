package com.microservices.photoappapiusers.service;


import com.microservices.photoappapiusers.data.UserEntity;
import com.microservices.photoappapiusers.data.UserRepository;
import com.microservices.photoappapiusers.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    UserRepository repository;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {

        repository = userRepository;
        passwordEncoder = encoder;
    }

    @Override
    public UserDto createUser(UserDto dto) {
        dto.setUserId(UUID.randomUUID().toString());
        dto.setEncryptedPassword(passwordEncoder.encode(dto.getPassword()));
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity entity = mapper.map(dto, UserEntity.class);
        repository.save(entity);
        return dto;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserDetails userDetails = loadUserByUsername(email);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(userDetails, UserDto.class);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = repository.findByEmail(username);
        if (entity == null)
            throw new UsernameNotFoundException(username);


        return new User(entity.getEmail(), entity.getEncryptedPassword(),
                true, true, true, true, new ArrayList<>());
    }
}
