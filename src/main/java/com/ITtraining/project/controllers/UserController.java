package com.ITtraining.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ITtraining.project.entities.UserEntity;
import com.ITtraining.project.entitiesEnum.EUserRole;
import com.ITtraining.project.repositories.UserRepository;

@RestController
@RequestMapping(value = "/api/v1/project/users")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	// find all users
	@RequestMapping
	public List<UserEntity> getUsers() {
		return (List<UserEntity>) userRepo.findAll();
	}

	// find user by Id
	@RequestMapping(value = "/{Id}")
	public UserEntity getUserById(@PathVariable Integer Id) {
		return userRepo.findById(Id).get();
	}

	// add new user
	@RequestMapping(method = RequestMethod.POST)
	public UserEntity addUser(@RequestBody UserEntity newUser) {

		if (newUser == null) {
			return null;
		}

		if (newUser.getFirstName() == null || newUser.getLastName() == null || newUser.getUsername() == null
				|| newUser.getPassword() == null) {

			return null;
		}

		newUser.setUserRole(EUserRole.ROLE_CUSTOMER);

		return userRepo.save(newUser);
	}

	// modify an existing user
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public UserEntity updateUser(@PathVariable Integer id, @RequestBody UserEntity user) {

		if (userRepo.existsById(id) && user != null) {

			UserEntity userEntity = userRepo.findById(id).get();

			if (user.getFirstName() != null) {
				userEntity.setFirstName(user.getFirstName());
			}

			if (user.getLastName() != null) {
				userEntity.setLastName(user.getLastName());
			}

			if (user.getEmail() != null) {
				userEntity.setEmail(user.getEmail());
			}

			return userRepo.save(userEntity);
		}

		return null;
	}

	// modify the user_role attribute of an existing user
	@RequestMapping(value = "/change/{id}/role/{role}", method = RequestMethod.PUT)
	public UserEntity updateUserRole(@PathVariable Integer id, @PathVariable String role) {

		if (userRepo.existsById(id)) {

			UserEntity userEntity = userRepo.findById(id).get();

			EUserRole userRole = EUserRole.valueOf(role.toUpperCase());
			userEntity.setUserRole(userRole);

			return userRepo.save(userEntity);
		}

		return null;
	}

	// change user password
	@RequestMapping(value = "/changePassword/{id}", method = RequestMethod.PUT)
	public UserEntity updateUserPassword(@PathVariable Integer id, @RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {

		if (userRepo.existsById(id) && newPassword != null) {

			UserEntity userEntity = userRepo.findById(id).get();

			if (userEntity.getPassword().equals(oldPassword)) {
				userEntity.setPassword(newPassword);

				return userRepo.save(userEntity);
			}
		}

		return null;
	}

	// delete user
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public UserEntity deleteUser(@PathVariable Integer id) {

		if (userRepo.existsById(id)) {

			UserEntity userEntity = userRepo.findById(id).get();
			userRepo.deleteById(id);

			return userEntity;
		}

		return null;
	}

	// find by username
	@RequestMapping(value = "/by-username/{username}")
	public UserEntity getByUserName(@PathVariable String username) {

		return userRepo.findByUsername(username);
	}

}
