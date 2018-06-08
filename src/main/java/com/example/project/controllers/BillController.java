package com.example.project.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.entities.BillEntity;
import com.example.project.entities.CategoryEntity;
import com.example.project.entities.OfferEntity;
import com.example.project.entities.UserEntity;
import com.example.project.entitiesEnum.EUserRole;
import com.example.project.repositories.BillRepository;
import com.example.project.repositories.CategoryRepository;
import com.example.project.repositories.OfferRepository;
import com.example.project.repositories.UserRepository;

@RestController
@RequestMapping(value = "/api/v1/project/bills")
public class BillController {

	@Autowired
	private BillRepository billRepo;

	@Autowired
	private OfferRepository offerRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Value(value = "${dateFormat}")
	private String dateFormat;

	@RequestMapping
	public List<BillEntity> getBills() {
		return (List<BillEntity>) billRepo.findAll();
	}

	@RequestMapping(value = "/{offerId}/buyer/{buyerId}", method = RequestMethod.POST)
	public BillEntity addBill(@PathVariable Integer offerId, @PathVariable Integer buyerId,
			@RequestBody BillEntity newBill) {

		OfferEntity offerEntity = offerRepo.findById(offerId).get();
		UserEntity buyer = userRepo.findById(buyerId).get();

		if (offerEntity == null || buyer == null || !buyer.getUserRole().equals(EUserRole.ROLE_CUSTOMER)) {
			return null;
		}

		newBill.setBillCreated(new Date());
		newBill.setPaymentCanceled(false);
		newBill.setPaymentMade(false);
		newBill.setBillOffer(offerEntity);
		newBill.setBillUser(buyer);

		// zadatak 5.2
		offerEntity.setAvailableOffers(offerEntity.getAvailableOffers() - 1);
		offerEntity.setBoughtOffers(offerEntity.getBoughtOffers() + 1);
		offerRepo.save(offerEntity);

		return billRepo.save(newBill);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public BillEntity updateBill(@PathVariable Integer id, @RequestBody BillEntity updatedBill) {

		BillEntity billEntity = billRepo.findById(id).get();

		if (billEntity == null || updatedBill == null) {
			return null;
		}

		if (updatedBill.getPaymentCanceled() != null) {
			billEntity.setPaymentCanceled(updatedBill.getPaymentCanceled());
		}

		if (updatedBill.getPaymentMade() != null) {
			billEntity.setPaymentMade(updatedBill.getPaymentMade());
		}

		// zadatak 5.3
		if (billEntity.getPaymentCanceled()) {
			OfferEntity offerEntity = offerRepo.findById(billEntity.getBillOffer().getId()).get();
			offerEntity.setAvailableOffers(offerEntity.getAvailableOffers() + 1);
			offerEntity.setBoughtOffers(offerEntity.getBoughtOffers() - 1);
			offerRepo.save(offerEntity);
		}

		return billRepo.save(billEntity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public BillEntity deleteBill(@PathVariable Integer id) {

		BillEntity billEntity = billRepo.findById(id).get();

		if (billEntity == null) {
			return null;
		}

		billRepo.deleteById(id);

		return billEntity;
	}

	@RequestMapping(value = "/findByBuyer/{buyerId}", method = RequestMethod.GET)
	public List<BillEntity> getCustomerBills(@PathVariable Integer buyerId) {

		UserEntity buyer = userRepo.findById(buyerId).get();

		if (buyer == null) {
			return null;
		}

		return billRepo.findByBillUser(buyer);
	}

	@RequestMapping(value = "/findByCategory/{categoryId}")
	public List<BillEntity> getCategryBills(@PathVariable Integer categoryId) {

		CategoryEntity categoryEntity = categoryRepo.findById(categoryId).get();

		if (categoryEntity == null) {
			return null;
		}

		return billRepo.findBybillOfferOfferCategory(categoryEntity);
	}

	@RequestMapping(value = "/findByDate/{startDate}/and/{endDate}")
	public List<BillEntity> getBillsIntoIntervalDate(@PathVariable String startDate, @PathVariable String endDate)
			throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date start = simpleDateFormat.parse(startDate);
		Date end = simpleDateFormat.parse(endDate);

		return billRepo.findByBillCreatedBetween(start, end);
	}

}
