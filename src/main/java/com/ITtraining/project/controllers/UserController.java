package com.ITtraining.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ITtraining.project.controllers.util.RESTError;
import com.ITtraining.project.entities.UserEntity;
import com.ITtraining.project.entities.dto.UserDTO;
import com.ITtraining.project.entitiesEnum.EUserRole;
import com.ITtraining.project.repositories.UserRepository;
import com.ITtraining.project.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(value = "/api/v1/project/users")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	// find all public users
	@RequestMapping(value = "/public")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getUsersPublic() {
		return new ResponseEntity<Iterable<UserEntity>>(userRepo.findAll(), HttpStatus.OK);
	}

	// find all private users
	@JsonView(Views.Private.class)
	@RequestMapping(value = "/private")
	public ResponseEntity<?> getUsersPrivate() {
		return new ResponseEntity<Iterable<UserEntity>>(userRepo.findAll(), HttpStatus.OK);
	}

	// find all admin users
	@RequestMapping(value = "/admin")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getUsersAdmin() {
		return new ResponseEntity<Iterable<UserEntity>>(userRepo.findAll(), HttpStatus.OK);
	}

	// find user by Id
	@RequestMapping(value = "/{Id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer Id) {

		UserEntity userEntity = userRepo.findById(Id).get();

		if (userEntity == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "User with provided Id not found."),
					HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
		}
	}

	// add new user
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addUser(@RequestBody UserDTO newUser) {

		if (newUser == null) {
			return new ResponseEntity<RESTError>(new RESTError(2, "User object is invalid."), HttpStatus.BAD_REQUEST);
		}

		if (newUser.getFirstName() == null || newUser.getLastName() == null || newUser.getUsername() == null
				|| newUser.getPassword() == null) {

			return new ResponseEntity<RESTError>(new RESTError(2, "User object is invalid."), HttpStatus.BAD_REQUEST);
		}

		if (!newUser.getPassword().equals(newUser.getRepeatedPassword())) {
			return new ResponseEntity<RESTError>(new RESTError(2, "User passwords do not match."),
					HttpStatus.BAD_REQUEST);
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName(newUser.getFirstName());
		userEntity.setLastName(newUser.getLastName());
		userEntity.setEmail(newUser.getEmail());
		userEntity.setUsername(newUser.getUsername());
		userEntity.setPassword(newUser.getPassword());
		userEntity.setUserRole(EUserRole.ROLE_CUSTOMER);

		return new ResponseEntity<UserEntity>(userRepo.save(userEntity), HttpStatus.OK);
	}

	// modify an existing user
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserEntity user) {

		if (userRepo.existsById(id) && user != null) {

			UserEntity userEntity = userRepo.findById(id).get();

			if (user.getFirstName() != null || !user.getFirstName().equals(" ") || !user.getFirstName().equals("")) {
				userEntity.setFirstName(user.getFirstName());
			}

			if (user.getLastName() != null || !user.getLastName().equals(" ") || !user.getLastName().equals("")) {
				userEntity.setLastName(user.getLastName());
			}

			if (user.getEmail() != null || !user.getEmail().equals(" ") || !user.getEmail().equals("")) {
				userEntity.setEmail(user.getEmail());
			}

			return new ResponseEntity<UserEntity>(userRepo.save(userEntity), HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "User with provided Id not found."),
				HttpStatus.NOT_FOUND);
	}

	// modify the user_role attribute of an existing user
	@RequestMapping(value = "/change/{id}/role/{role}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUserRole(@PathVariable Integer id, @PathVariable String role) {

		if (userRepo.existsById(id)) {

			UserEntity userEntity = userRepo.findById(id).get();

			EUserRole userRole = EUserRole.valueOf(role.toUpperCase());
			userEntity.setUserRole(userRole);

			return new ResponseEntity<UserEntity>(userRepo.save(userEntity), HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "User with provided Id not found."),
				HttpStatus.NOT_FOUND);
	}

	// change user password
	@RequestMapping(value = "/changePassword/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUserPassword(@PathVariable Integer id,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {

		if (userRepo.existsById(id) && newPassword != null) {

			UserEntity userEntity = userRepo.findById(id).get();

			if (userEntity.getPassword().equals(oldPassword)) {
				userEntity.setPassword(newPassword);

				return new ResponseEntity<UserEntity>(userRepo.save(userEntity), HttpStatus.OK);
			}
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "User with provided Id not found."),
				HttpStatus.NOT_FOUND);
	}

	// delete user
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

		if (userRepo.existsById(id)) {

			UserEntity userEntity = userRepo.findById(id).get();
			userRepo.deleteById(id);

			return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "User with provided Id not found."),
				HttpStatus.NOT_FOUND);
	}

	// find by username
	@RequestMapping(value = "/by-username/{username}")
	public ResponseEntity<?> getByUserName(@PathVariable String username) {

		UserEntity userEntity = userRepo.findByUsername(username);

		if (userEntity == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "User with provided username not found."),
					HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
		}
	}

}
