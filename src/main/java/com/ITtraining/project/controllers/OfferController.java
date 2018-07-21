package com.ITtraining.project.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ITtraining.project.controllers.util.RESTError;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.UserEntity;
import com.ITtraining.project.entitiesEnum.EOfferStatus;
import com.ITtraining.project.entitiesEnum.EUserRole;
import com.ITtraining.project.repositories.CategoryRepository;
import com.ITtraining.project.repositories.OfferRepository;
import com.ITtraining.project.repositories.UserRepository;
import com.ITtraining.project.security.Views;
import com.ITtraining.project.services.BillDao;
import com.ITtraining.project.services.FileHandler;
import com.fasterxml.jackson.annotation.JsonView;

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

	private String createErrorMessage(BindingResult result) {
		String msg = " ";
		for (ObjectError error : result.getAllErrors()) {
			msg += error.getDefaultMessage();
			msg += " ";
		}
		return msg;
	}

	// find all offers
	@RequestMapping
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getAllOffers() {
		return new ResponseEntity<Iterable<OfferEntity>>(offerRepo.findAll(), HttpStatus.OK);
	}

	// add new offer
	@RequestMapping(method = RequestMethod.POST, value = "/{categoryId}/seller/{sellerId}")
	public ResponseEntity<?> addNewOffer(@Valid @RequestBody OfferEntity newOffer, BindingResult result,
			@PathVariable Integer categoryId, @PathVariable Integer sellerId) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		CategoryEntity categoryEntity = categoryRepo.findById(categoryId).get();
		UserEntity userEntity = userRepo.findById(sellerId).get();

		if (userEntity == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "User with provided ID not found."),
					HttpStatus.NOT_FOUND);
		}

		if (newOffer == null || !userEntity.getUserRole().equals(EUserRole.ROLE_SELLER)) {
			return new ResponseEntity<RESTError>(new RESTError(2, "User with provided ID is not seller."),
					HttpStatus.BAD_REQUEST);
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
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "Category with provided ID not found."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<OfferEntity>(offerRepo.save(newOffer), HttpStatus.OK);
	}

	// modify an existing offer
	@RequestMapping(value = "/{id}/category/{categoryId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateOffer(@PathVariable Integer id, @RequestBody OfferEntity offer,
			@PathVariable Integer categoryId) {

		if (offerRepo.existsById(id) && offer != null) {

			OfferEntity offerEntity = offerRepo.findById(id).get();
			CategoryEntity categoryEntity = categoryRepo.findById(categoryId).get();

			if (offer.getOfferName() != null || !offer.getOfferName().equals(" ") || !offer.getOfferName().equals("")) {
				offerEntity.setOfferName(offer.getOfferName());
			}

			if (offer.getOfferDescription() != null || !offer.getOfferDescription().equals(" ")
					|| !offer.getOfferDescription().equals("")) {
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

			return new ResponseEntity<OfferEntity>(offerRepo.save(offerEntity), HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Offer with provided ID not found."),
				HttpStatus.NOT_FOUND);
	}

	// delete offer
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOffer(@PathVariable Integer id) {

		if (offerRepo.existsById(id)) {

			OfferEntity offerEntity = offerRepo.findById(id).get();
			offerRepo.deleteById(id);

			return new ResponseEntity<OfferEntity>(offerEntity, HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Offer with provided ID not found."),
				HttpStatus.NOT_FOUND);
	}

	// find offer by Id
	@RequestMapping(value = "/{Id}")
	public ResponseEntity<?> getOfferById(@PathVariable Integer Id) {

		if (offerRepo.findById(Id).isPresent()) {
			return new ResponseEntity<OfferEntity>(offerRepo.findById(Id).get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "Offer with provided ID not found."),
					HttpStatus.NOT_FOUND);
		}
	}

	// change offer status
	@RequestMapping(value = "/changeOffer/{id}/status/{status}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateOfferStatus(@PathVariable Integer id, @PathVariable String status) {

		if (offerRepo.existsById(id) && status != null) {

			OfferEntity offerEntity = offerRepo.findById(id).get();
			EOfferStatus offerStatus = EOfferStatus.valueOf(status.toUpperCase());
			offerEntity.setOfferStatus(offerStatus);

			if (offerStatus.equals(EOfferStatus.EXPIRED)) {
				billDao.cancelBillsForOffer(offerEntity);
			}

			return new ResponseEntity<OfferEntity>(offerRepo.save(offerEntity), HttpStatus.OK);

		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Offer with provided ID not found."),
				HttpStatus.NOT_FOUND);
	}

	// returns the offers action price within the given limits
	@RequestMapping(value = "/findByPrice/{lowerPrice}/and/{upperPrice}", method = RequestMethod.GET)
	public ResponseEntity<?> getOffersByActionPriceValue(@PathVariable Double lowerPrice,
			@PathVariable Double upperPrice) {

		return new ResponseEntity<List<OfferEntity>>(offerRepo.findByActionPriceBetween(lowerPrice, upperPrice),
				HttpStatus.OK);
	}

	// upload image for an existing offer
	@RequestMapping(value = "/uploadImage/{offerId}", method = RequestMethod.POST)
	public ResponseEntity<?> uploadImage(@PathVariable Integer offerId, @RequestParam("file") MultipartFile file) {

		if (offerRepo.existsById(offerId)) {

			OfferEntity offerEntity = offerRepo.findById(offerId).get();
			String imagePath = null;

			try {

				imagePath = fileHandler.singleFileUpload(offerId, file);

			} catch (IOException e) {
				e.printStackTrace();
			}

			offerEntity.setImagePath(imagePath);

			return new ResponseEntity<String>("Image successfully uploaded", HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Offer with provided ID not found."),
				HttpStatus.NOT_FOUND);
	}

}
