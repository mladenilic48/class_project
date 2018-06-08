package com.example.project.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.project.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	UserEntity findByUsername(String username);

}
