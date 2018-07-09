package com.ITtraining.project.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.UserEntity;
import com.ITtraining.project.entities.VoucherEntity;
import com.ITtraining.project.entitiesEnum.EUserRole;
import com.ITtraining.project.repositories.OfferRepository;
import com.ITtraining.project.repositories.UserRepository;
import com.ITtraining.project.repositories.VoucherRepository;

@RestController
@RequestMapping(value = "/api/v1/project/vouchers")
public class VoucherController {

	@Autowired
	private VoucherRepository voucherRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OfferRepository offerRepo;

	// find all vouchers
	@RequestMapping
	public List<VoucherEntity> getVouchers() {
		return (List<VoucherEntity>) voucherRepo.findAll();
	}

	// create new voucher
	@RequestMapping(value = "/{offerId}/buyer/{buyerId}", method = RequestMethod.POST)
	public VoucherEntity addVoucher(@PathVariable Integer offerId, @PathVariable Integer buyerId) {

		VoucherEntity newVoucher = new VoucherEntity();
		OfferEntity offerEntity = offerRepo.findById(offerId).get();
		UserEntity buyer = userRepo.findById(buyerId).get();

		if (offerEntity == null || buyer == null || newVoucher == null
				|| !buyer.getUserRole().equals(EUserRole.ROLE_CUSTOMER)) {

			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);

		newVoucher.setExpirationDate(calendar.getTime());
		newVoucher.setIsUsed(false);
		newVoucher.setVoucherOffer(offerEntity);
		newVoucher.setVoucherUser(buyer);

		return voucherRepo.save(newVoucher);
	}

	// modify an existing voucher
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public VoucherEntity updateVoucher(@PathVariable Integer id, @RequestBody VoucherEntity updatedEntity) {

		VoucherEntity voucherEntity = voucherRepo.findById(id).get();

		if (voucherEntity == null || updatedEntity == null) {
			return null;
		}

		if (updatedEntity.getIsUsed() != null) {
			voucherEntity.setIsUsed(updatedEntity.getIsUsed());
		}

		return voucherRepo.save(voucherEntity);
	}

	// delete voucher
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public VoucherEntity deleteVoucher(@PathVariable Integer id) {

		VoucherEntity voucherEntity = voucherRepo.findById(id).get();

		if (voucherEntity == null) {
			return null;
		}

		voucherRepo.deleteById(id);

		return voucherEntity;
	}

	// find vouchers by buyer
	@RequestMapping(value = "/findByBuyer/{buyerId}")
	public List<VoucherEntity> getVouchersByUser(@PathVariable Integer buyerId) {

		UserEntity buyer = userRepo.findById(buyerId).get();

		if (buyer != null) {
			return voucherRepo.findByVoucherUser(buyer);
		}

		return null;
	}

	// find vouchers by offer
	@RequestMapping(value = "/findByOffer/{offerId}")
	public List<VoucherEntity> getVouchersByOffer(@PathVariable Integer offerId) {

		OfferEntity offerEntity = offerRepo.findById(offerId).get();

		if (offerId != null) {
			return voucherRepo.findByVoucherOffer(offerEntity);
		}

		return null;
	}

	// find non expired vouchers
	@RequestMapping(value = "/findNonExpiredVoucher")
	public List<VoucherEntity> getNonExpiredVouchers() {

		return voucherRepo.findByExpirationDateGreaterThanEqual(new Date());
	}

}
