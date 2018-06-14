package com.ITtraining.project.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entitiesEnum.EOfferStatus;

public interface OfferRepository extends CrudRepository<OfferEntity, Integer> {

	List<OfferEntity> findByActionPriceBetween(Double lowerPrice, Double upperPrice);

	List<OfferEntity> findByOfferStatusAndOfferCategory(EOfferStatus status, CategoryEntity categoryEntity);
}
