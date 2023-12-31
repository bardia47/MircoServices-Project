package com.microservices.photoappapiusers.controller;

import com.microservices.photoappapiusers.data.UserEntity;
import com.microservices.photoappapiusers.model.CreateUserRequestModel;
import com.microservices.photoappapiusers.model.CreateUserResponseModel;
import com.microservices.photoappapiusers.model.UserResponseModel;
import com.microservices.photoappapiusers.service.UserService;
import com.microservices.photoappapiusers.shared.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    Environment environment;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + environment.getProperty("local.server.port");
    }

    @Autowired
    UserService service;

    @PostMapping("/")
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel model) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto dto = mapper.map(model, UserDto.class);
        dto = service.createUser(dto);
        CreateUserResponseModel responseModel = mapper.map(dto, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);

    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = service.getUserByUserId(userId);
        UserResponseModel responseModel = new ModelMapper().map(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);


    }
}
