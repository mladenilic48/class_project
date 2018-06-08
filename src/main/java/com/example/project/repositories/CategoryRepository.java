package com.example.project.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.project.entities.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {

}
