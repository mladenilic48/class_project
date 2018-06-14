package com.ITtraining.project.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ITtraining.project.entities.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {

}
