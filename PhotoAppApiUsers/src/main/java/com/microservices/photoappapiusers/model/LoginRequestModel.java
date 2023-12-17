package com.microservices.photoappapiusers.model;

import lombok.Data;

@Data
public class LoginRequestModel {
    private String password;
    private String email;
}
