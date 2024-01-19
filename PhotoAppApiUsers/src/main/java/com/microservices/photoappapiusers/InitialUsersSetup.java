package com.microservices.photoappapiusers;

import com.microservices.photoappapiusers.data.*;
import com.microservices.photoappapiusers.shared.Roles;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
@Log4j2
public class InitialUsersSetup {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository usersRepository;



    @Transactional
    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("From Application Ready Event");

        AuthorityEntity readAuthority = createAuthority("READ");
        AuthorityEntity writeAuthority = createAuthority("WRITE");
        AuthorityEntity deleteAuthority = createAuthority("DELETE");

        createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        if (roleAdmin == null) return;

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("Sergey");
        adminUser.setLastName("Kargopolov");
        adminUser.setEmail("admin@test.com");
        adminUser.setUserId(UUID.randomUUID().toString());
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
        adminUser.setRoles(Arrays.asList(roleAdmin));

        UserEntity storedAdminUser = usersRepository.findByEmail("admin@test.com");

        if (storedAdminUser == null) {
            usersRepository.save(adminUser);
        }

    }

    @Transactional
    public AuthorityEntity createAuthority(String name) {

        AuthorityEntity authority = authorityRepository.findByName(name);

        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }

        return authority;
    }

    @Transactional
    public RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {

        RoleEntity role = roleRepository.findByName(name);

        if (role == null) {
            role = new RoleEntity(name, authorities);
            roleRepository.save(role);
        }

        return role;

    }

}
