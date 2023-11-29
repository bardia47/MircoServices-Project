package com.microservices.photoappapiusers.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserResponseModel {

    String firstName;
    String lastName;
    String email;
    String userId;
}
