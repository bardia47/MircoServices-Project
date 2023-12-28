package com.microservices.photoappapiusers.shared;

import com.microservices.photoappapiusers.model.AlbumResponseModel;
import com.microservices.photoappapiusers.model.CreateUserRequestModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserDto extends CreateUserRequestModel {
    @NotNull(message = "First name cannot be null")
    @Size(min = 2, message = "First name must not be less than two characters")
    String firstName;
    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, message = "Last name must not be less than two characters")
    String lastName;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max = 16, message = "Password must be equal or greater than 8 and less than 16")
    String password;
    @NotNull(message = "Email cannot be null")
    @Email
    String email;
    private String userId;
    private String encryptedPassword;

    private List<AlbumResponseModel> albums;
}
