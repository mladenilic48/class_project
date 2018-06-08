package com.example.project.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.project.entities.OfferEntity;

public interface OfferRepository extends CrudRepository<OfferEntity, Integer> {
	
	List<OfferEntity> findByActionPriceBetween(Double lowerPrice, Double upperPrice);

}
