package com.microservices.photoappapiaccountmanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/account")
public class AccountController {
    public class UsersController {
        @GetMapping("/status/check")
        public String status(){
            return "Working";
        }
    }

}
