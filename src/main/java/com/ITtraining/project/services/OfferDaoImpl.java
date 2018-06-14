package com.ITtraining.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entitiesEnum.EOfferStatus;
import com.ITtraining.project.repositories.OfferRepository;

@Service
public class OfferDaoImpl implements OfferDao {

	@Autowired
	private OfferRepository offerRepo;

	@Override
	public List<OfferEntity> findActiveOffersForCategory(CategoryEntity categoryEntity) {

		return offerRepo.findByOfferStatusAndOfferCategory(EOfferStatus.APPROVED, categoryEntity);
	}

	@Override
	public void updateOffer(OfferEntity offer, boolean billCreated) {

		if (billCreated) {
			offer.setAvailableOffers(offer.getAvailableOffers() - 1);
			offer.setBoughtOffers(offer.getBoughtOffers() + 1);
		} else {
			offer.setAvailableOffers(offer.getAvailableOffers() + 1);
			offer.setBoughtOffers(offer.getBoughtOffers() - 1);
		}

		offerRepo.save(offer);

	}

}
