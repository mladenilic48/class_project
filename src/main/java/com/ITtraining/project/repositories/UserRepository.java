package com.ITtraining.project.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ITtraining.project.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	UserEntity findByUsername(String username);

}
