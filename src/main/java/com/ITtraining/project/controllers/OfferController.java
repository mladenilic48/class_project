package com.ITtraining.project.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.UserEntity;
import com.ITtraining.project.entitiesEnum.EOfferStatus;
import com.ITtraining.project.entitiesEnum.EUserRole;
import com.ITtraining.project.repositories.CategoryRepository;
import com.ITtraining.project.repositories.OfferRepository;
import com.ITtraining.project.repositories.UserRepository;
import com.ITtraining.project.services.BillDao;
import com.ITtraining.project.services.FileHandler;

@RestController
@RequestMapping(value = "/api/v1/project/offers")
public class OfferController {

	@Autowired
	private OfferRepository offerRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private FileHandler fileHandler;

	@Autowired
	private BillDao billDao;

	// find all offers
	@RequestMapping
	public List<OfferEntity> getAllOffers() {
		return (List<OfferEntity>) offerRepo.findAll();
	}

	// add new offer
	@RequestMapping(method = RequestMethod.POST, value = "/{categoryId}/seller/{sellerId}")
	public OfferEntity addNewOffer(@RequestBody OfferEntity newOffer, @PathVariable Integer categoryId,
			@PathVariable Integer sellerId) {

		CategoryEntity categoryEntity = categoryRepo.findById(categoryId).get();
		UserEntity userEntity = userRepo.findById(sellerId).get();

		if (newOffer == null || userEntity == null || !userEntity.getUserRole().equals(EUserRole.ROLE_SELLER)) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 10);

		newOffer.setOfferCreated(new Date());
		newOffer.setOfferExpires(calendar.getTime());
		newOffer.setOfferStatus(EOfferStatus.WAIT_FOR_APPROVING);
		newOffer.setSeller(userEntity);

		if (categoryEntity != null) {
			newOffer.setOfferCategory(categoryEntity);
		}

		return offerRepo.save(newOffer);
	}

	// modify an existing offer
	@RequestMapping(value = "/{id}/category/{categoryId}", method = RequestMethod.PUT)
	public OfferEntity updateOffer(@PathVariable Integer id, @RequestBody OfferEntity offer,
			@PathVariable Integer categoryId) {

		if (offerRepo.existsById(id) && offer != null) {

			OfferEntity offerEntity = offerRepo.findById(id).get();
			CategoryEntity categoryEntity = categoryRepo.findById(categoryId).get();

			if (offer.getOfferName() != null) {
				offerEntity.setOfferName(offer.getOfferName());
			}

			if (offer.getOfferDescription() != null) {
				offerEntity.setOfferDescription(offer.getOfferDescription());
			}

			if (offer.getAvailableOffers() != null) {
				offerEntity.setAvailableOffers(offer.getAvailableOffers());
			}

			if (offer.getBoughtOffers() != null) {
				offerEntity.setBoughtOffers(offer.getBoughtOffers());
			}

			if (offer.getOfferCreated() != null) {
				offerEntity.setOfferCreated(offer.getOfferCreated());
			}

			if (offer.getOfferExpires() != null) {
				offerEntity.setOfferExpires(offer.getOfferExpires());
			}

			if (offer.getRegularPrice() != null) {
				offerEntity.setRegularPrice(offer.getRegularPrice());
			}

			if (offer.getActionPrice() != null) {
				offerEntity.setActionPrice(offer.getActionPrice());
			}

			if (offer.getImagePath() != null) {
				offerEntity.setImagePath(offer.getImagePath());
			}

			if (categoryEntity != null) {
				offer.setOfferCategory(categoryEntity);
			}

			return offerRepo.save(offerEntity);
		}

		return null;
	}

	// delete offer
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public OfferEntity deleteOffer(@PathVariable Integer id) {

		if (offerRepo.existsById(id)) {

			OfferEntity offerEntity = offerRepo.findById(id).get();
			offerRepo.deleteById(id);

			return offerEntity;
		}

		return null;
	}

	// find offer by Id
	@RequestMapping(value = "/{Id}")
	public OfferEntity getOfferById(@PathVariable Integer Id) {
		return offerRepo.findById(Id).get();
	}

	// change offer status
	@RequestMapping(value = "/changeOffer/{id}/status/{status}", method = RequestMethod.PUT)
	public OfferEntity updateOfferStatus(@PathVariable Integer id, @PathVariable String status) {

		if (offerRepo.existsById(id) && status != null) {

			OfferEntity offerEntity = offerRepo.findById(id).get();
			EOfferStatus offerStatus = EOfferStatus.valueOf(status.toUpperCase());
			offerEntity.setOfferStatus(offerStatus);

			if (offerStatus.equals(EOfferStatus.EXPIRED)) {
				billDao.cancelBillsForOffer(offerEntity);
			}

			return offerRepo.save(offerEntity);

		}

		return null;
	}

	// returns the offer whose action price is within the given limits
	@RequestMapping(value = "/findByPrice/{lowerPrice}/and/{upperPrice}", method = RequestMethod.GET)
	public List<OfferEntity> getOffersByActionPriceValue(@PathVariable Double lowerPrice,
			@PathVariable Double upperPrice) {

		return offerRepo.findByActionPriceBetween(lowerPrice, upperPrice);
	}

	// upload image for an existing offer
	@RequestMapping(value = "/uploadImage/{offerId}", method = RequestMethod.POST)
	public OfferEntity uploadImage(@PathVariable Integer offerId, @RequestParam("file") MultipartFile file) {

		if (offerRepo.existsById(offerId)) {
			OfferEntity offerEntity = offerRepo.findById(offerId).get();
			String imagePath = null;

			try {
				imagePath = fileHandler.singleFileUpload(offerId, file);
			} catch (IOException e) {
				e.printStackTrace();
			}

			offerEntity.setImagePath(imagePath);
			return offerEntity;
		}

		return null;
	}

}
