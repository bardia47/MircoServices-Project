package com.microservices.photoappapiusers.model;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseModel {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private List<AlbumResponseModel> albums;
}
