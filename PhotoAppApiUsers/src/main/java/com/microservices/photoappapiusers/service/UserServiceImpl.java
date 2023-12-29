package com.microservices.photoappapiusers.service;


import com.microservices.photoappapiusers.data.UserEntity;
import com.microservices.photoappapiusers.data.UserRepository;
import com.microservices.photoappapiusers.shared.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final AlbumsServiceClient client;


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
        return mapper.map(userDetails, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = repository.findByEmail(username);
        if (entity == null)
            throw new UsernameNotFoundException(username);


        return new User(entity.getEmail(), entity.getEncryptedPassword(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity entity = repository.findByUserId(userId);
        if (entity == null)
            throw new UsernameNotFoundException("user not found");
        UserDto userDto = new ModelMapper().map(entity, UserDto.class);


        userDto.setAlbums(client.getAlbums(userId));
        return userDto;
    }
}
