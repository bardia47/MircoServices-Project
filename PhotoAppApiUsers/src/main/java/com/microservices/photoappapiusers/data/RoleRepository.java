package com.microservices.photoappapiusers.data;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

	RoleEntity findByName(String name);
	
}
