package com.ITtraining.project.services;

import java.util.List;

import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;

public interface OfferDao {

	List<OfferEntity> findActiveOffersForCategory(CategoryEntity categoryEntity);
	
	public void updateOffer(OfferEntity offer, boolean billCreated);
}
