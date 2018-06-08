package com.example.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.entities.CategoryEntity;
import com.example.project.repositories.CategoryRepository;

@RestController
@RequestMapping(value = "/api/v1/project/categories")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepo;

	// vraca sve kategorije
	@RequestMapping
	public List<CategoryEntity> getAllCategories() {
		return (List<CategoryEntity>) categoryRepo.findAll();
	}

	// postavljanje nove kategorije
	@RequestMapping(method = RequestMethod.POST)
	public CategoryEntity addNewCategory(@RequestBody CategoryEntity newCategory) {

		if (newCategory == null || newCategory.getCategoryName() == null) {
			return null;
		}

		return categoryRepo.save(newCategory);
	}

	// izmena postojece kategorije
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public CategoryEntity updateCategory(@PathVariable Integer id, @RequestBody CategoryEntity category) {

		if (categoryRepo.existsById(id) && category != null) {

			CategoryEntity categoryEntity = categoryRepo.findById(id).get();

			if (category.getCategoryName() != null) {
				categoryEntity.setCategoryName(category.getCategoryName());
			}

			if (category.getCategoryDescription() != null) {
				categoryEntity.setCategoryDescription(category.getCategoryDescription());
			}

			return categoryRepo.save(categoryEntity);
		}

		return null;
	}

	// brisanje postojece kategorije
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public CategoryEntity deleteCategory(@PathVariable Integer id) {

		if (categoryRepo.existsById(id)) {

			CategoryEntity categoryEntity = categoryRepo.findById(id).get();
			categoryRepo.deleteById(id);

			return categoryEntity;
		}

		return null;
	}

	// vraca kategoriju po Id-u
	@RequestMapping(value = "/{id}")
	public CategoryEntity getCategoryById(@PathVariable Integer id) {
		return categoryRepo.findById(id).get();
	}

}
