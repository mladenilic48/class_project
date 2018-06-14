package com.ITtraining.project.services;

import java.util.List;

import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;

public interface BillDao {
	
	List<BillEntity> findActiveBillsForCategory(CategoryEntity categoryEntity);
	
	void cancelBillsForOffer(OfferEntity offerEntity);
}
