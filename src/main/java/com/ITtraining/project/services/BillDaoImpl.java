package com.ITtraining.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.repositories.BillRepository;

@Service
public class BillDaoImpl implements BillDao {

	@Autowired
	private BillRepository billRepo;

	@Override
	public List<BillEntity> findActiveBillsForCategory(CategoryEntity categoryEntity) {

		return billRepo.findByPaymentCanceledAndBillOffer_OfferCategory(false, categoryEntity);
	}

	@Override
	public void cancelBillsForOffer(OfferEntity offerEntity) {

		List<BillEntity> bills = billRepo.findByBillOffer(offerEntity);
		for (BillEntity billEntity : bills) {
			billEntity.setPaymentCanceled(true);
		}

		billRepo.saveAll(bills);
	}

}