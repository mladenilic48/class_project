package com.example.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.entities.UserEntity;
import com.example.project.entitiesEnum.EUserRole;
import com.example.project.repositories.UserRepository;

@RestController
@RequestMapping(value = "/api/v1/project/users")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	// vraca sve korisnike
	@RequestMapping
	public List<UserEntity> getUsers() {
		return (List<UserEntity>) userRepo.findAll();
	}

	// vraca korisnika po Id-u
	@RequestMapping(value = "/{Id}")
	public UserEntity getUserById(@PathVariable Integer Id) {
		return userRepo.findById(Id).get();
	}

	// dodavanje novog korisnika
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

	// izmena postojeceg korisnika
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

	// izmena atributa user_role postojeceg korisnika
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

	// promena lozinke
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

	// brisanje korisnika
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public UserEntity deleteUser(@PathVariable Integer id) {

		if (userRepo.existsById(id)) {

			UserEntity userEntity = userRepo.findById(id).get();
			userRepo.deleteById(id);

			return userEntity;
		}

		return null;
	}

	// pretraga po korisnickom imenu
	@RequestMapping(value = "/by-username/{username}")
	public UserEntity getByUserName(@PathVariable String username) {

		return userRepo.findByUsername(username);
	}

}
