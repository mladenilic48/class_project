package com.ITtraining.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.UserEntity;

public interface BillRepository extends CrudRepository<BillEntity, Integer> {

	List<BillEntity> findByBillUser(UserEntity userEntity);

	List<BillEntity> findBybillOfferOfferCategory(CategoryEntity categoryEntity);
	
	List<BillEntity> findByBillCreatedBetween(Date startDate, Date endDate);
	
	List<BillEntity> findByPaymentCanceledAndBillOffer_OfferCategory(Boolean paymentCanceled, CategoryEntity categoryEntity);
	
	List<BillEntity> findByBillOffer(OfferEntity offerEntity);
}
