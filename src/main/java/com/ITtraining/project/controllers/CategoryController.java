package com.ITtraining.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ITtraining.project.controllers.util.RESTError;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.repositories.CategoryRepository;
import com.ITtraining.project.security.Views;
import com.ITtraining.project.services.BillDao;
import com.ITtraining.project.services.OfferDao;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(value = "/api/v1/project/categories")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private BillDao billDao;

	@Autowired
	private OfferDao offerDao;

	// find all categories
	@RequestMapping
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getAllCategories() {
		return new ResponseEntity<Iterable<CategoryEntity>>(categoryRepo.findAll(), HttpStatus.OK);
	}

	// add new category
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewCategory(@RequestBody CategoryEntity newCategory) {

		if (newCategory == null || newCategory.getCategoryName() == null || newCategory.getCategoryName().equals(" ")
				|| newCategory.getCategoryName().equals("")) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Invalid category name."), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<CategoryEntity>(categoryRepo.save(newCategory), HttpStatus.OK);
	}

	// modify an existing category
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody CategoryEntity category) {

		if (categoryRepo.existsById(id) && category != null) {

			CategoryEntity categoryEntity = categoryRepo.findById(id).get();

			if (category.getCategoryName() != null) {
				categoryEntity.setCategoryName(category.getCategoryName());
			}

			if (category.getCategoryDescription() != null) {
				categoryEntity.setCategoryDescription(category.getCategoryDescription());
			}

			return new ResponseEntity<CategoryEntity>(categoryRepo.save(category), HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Category with provided ID not found."),
				HttpStatus.NOT_FOUND);
	}

	// delete an existing category
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {

		if (categoryRepo.existsById(id)) {

			CategoryEntity categoryEntity = categoryRepo.findById(id).get();

			if (billDao.findActiveBillsForCategory(categoryEntity).size() == 0
					&& offerDao.findActiveOffersForCategory(categoryEntity).size() == 0) {

				categoryRepo.deleteById(id);
				return new ResponseEntity<CategoryEntity>(categoryEntity, HttpStatus.OK);
			}
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Category with provided ID not found."),
				HttpStatus.NOT_FOUND);
	}

	// find category by Id
	@RequestMapping(value = "/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {

		if (categoryRepo.findById(id).isPresent()) {
			return new ResponseEntity<CategoryEntity>(categoryRepo.findById(id).get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "Category with provided ID not found."),
					HttpStatus.NOT_FOUND);
		}
	}

}
