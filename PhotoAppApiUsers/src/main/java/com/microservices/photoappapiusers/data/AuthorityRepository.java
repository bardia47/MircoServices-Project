package com.microservices.photoappapiusers.data;

import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

	AuthorityEntity findByName(String name);
	
}
