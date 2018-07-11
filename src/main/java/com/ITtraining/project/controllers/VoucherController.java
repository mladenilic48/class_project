package com.ITtraining.project.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ITtraining.project.controllers.util.RESTError;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.UserEntity;
import com.ITtraining.project.entities.VoucherEntity;
import com.ITtraining.project.entitiesEnum.EUserRole;
import com.ITtraining.project.repositories.OfferRepository;
import com.ITtraining.project.repositories.UserRepository;
import com.ITtraining.project.repositories.VoucherRepository;
import com.ITtraining.project.security.Views;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(value = "/api/v1/project/vouchers")
public class VoucherController {

	@Autowired
	private VoucherRepository voucherRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OfferRepository offerRepo;

	// find all public vouchers
	@RequestMapping(value = "/public")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getVouchersPublic() {
		return new ResponseEntity<Iterable<VoucherEntity>>(voucherRepo.findAll(), HttpStatus.OK);
	}

	// find all private vouchers
	@RequestMapping(value = "/private")
	@JsonView(Views.Private.class)
	public ResponseEntity<?> getVouchersPrivate() {
		return new ResponseEntity<Iterable<VoucherEntity>>(voucherRepo.findAll(), HttpStatus.OK);
	}

	// find all admin vouchers
	@RequestMapping(value = "/admin")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getVouchersAdmin() {
		return new ResponseEntity<Iterable<VoucherEntity>>(voucherRepo.findAll(), HttpStatus.OK);
	}

	// create new voucher
	@RequestMapping(value = "/{offerId}/buyer/{buyerId}", method = RequestMethod.POST)
	public ResponseEntity<?> addVoucher(@PathVariable Integer offerId, @PathVariable Integer buyerId) {

		VoucherEntity newVoucher = new VoucherEntity();
		OfferEntity offerEntity = offerRepo.findById(offerId).get();
		UserEntity buyer = userRepo.findById(buyerId).get();

		if (offerEntity == null || buyer == null || newVoucher == null
				|| !buyer.getUserRole().equals(EUserRole.ROLE_CUSTOMER)) {

			return new ResponseEntity<RESTError>(new RESTError(1, "Offer with provided ID not found"),
					HttpStatus.NOT_FOUND);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);

		newVoucher.setExpirationDate(calendar.getTime());
		newVoucher.setIsUsed(false);
		newVoucher.setVoucherOffer(offerEntity);
		newVoucher.setVoucherUser(buyer);

		return new ResponseEntity<VoucherEntity>(voucherRepo.save(newVoucher), HttpStatus.OK);
	}

	// modify an existing voucher
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateVoucher(@PathVariable Integer id, @RequestBody VoucherEntity updatedEntity) {

		VoucherEntity voucherEntity = voucherRepo.findById(id).get();

		if (voucherEntity == null || updatedEntity == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Voucher with provided ID not found."),
					HttpStatus.NOT_FOUND);
		}

		if (updatedEntity.getIsUsed() != null) {
			voucherEntity.setIsUsed(updatedEntity.getIsUsed());
		}

		return new ResponseEntity<VoucherEntity>(voucherRepo.save(voucherEntity), HttpStatus.OK);
	}

	// delete voucher
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteVoucher(@PathVariable Integer id) {

		VoucherEntity voucherEntity = voucherRepo.findById(id).get();

		if (voucherEntity == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Voucher with provided ID not found."),
					HttpStatus.NOT_FOUND);
		}

		voucherRepo.deleteById(id);

		return new ResponseEntity<VoucherEntity>(voucherEntity, HttpStatus.OK);
	}

	// find vouchers by buyer
	@RequestMapping(value = "/findByBuyer/{buyerId}")
	public ResponseEntity<?> getVouchersByUser(@PathVariable Integer buyerId) {

		UserEntity buyer = userRepo.findById(buyerId).get();

		if (buyer != null) {
			return new ResponseEntity<List<VoucherEntity>>(voucherRepo.findByVoucherUser(buyer), HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Buyer with provided ID not found"),
				HttpStatus.NOT_FOUND);
	}

	// find vouchers by offer
	@RequestMapping(value = "/findByOffer/{offerId}")
	public ResponseEntity<?> getVouchersByOffer(@PathVariable Integer offerId) {

		OfferEntity offerEntity = offerRepo.findById(offerId).get();

		if (offerId != null) {
			return new ResponseEntity<List<VoucherEntity>>(voucherRepo.findByVoucherOffer(offerEntity), HttpStatus.OK);
		}

		return new ResponseEntity<RESTError>(new RESTError(1, "Buyer with provided ID not found"),
				HttpStatus.NOT_FOUND);
	}

	// find non expired vouchers
	@RequestMapping(value = "/findNonExpiredVoucher")
	public ResponseEntity<?> getNonExpiredVouchers() {

		return new ResponseEntity<List<VoucherEntity>>(voucherRepo.findByExpirationDateGreaterThanEqual(new Date()),
				HttpStatus.OK);
	}

}
