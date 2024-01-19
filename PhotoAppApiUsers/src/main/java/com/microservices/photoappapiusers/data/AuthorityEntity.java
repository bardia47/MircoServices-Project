package com.microservices.photoappapiusers.data;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "authorities")
@Data
public class AuthorityEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1618156374241833700L;

    @Id
    @GeneratedValue
    @JsonIgnore
    private long id;

    @Column(nullable = false, length = 20)
    @JsonIgnore
    private String name;

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    private Collection<RoleEntity> roles;

    public AuthorityEntity() {

    }

    public AuthorityEntity(String name) {
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return getName();
    }
}
