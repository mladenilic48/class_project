package com.ITtraining.project.controllers;

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

import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.UserEntity;
import com.ITtraining.project.entitiesEnum.EUserRole;
import com.ITtraining.project.repositories.BillRepository;
import com.ITtraining.project.repositories.CategoryRepository;
import com.ITtraining.project.repositories.OfferRepository;
import com.ITtraining.project.repositories.UserRepository;
import com.ITtraining.project.services.OfferDao;
import com.ITtraining.project.services.VoucherDao;

@RestController
@RequestMapping(value = "/api/v1/project/bills")
public class BillController {

	@Autowired
	private BillRepository billRepo;

	@Autowired
	private OfferRepository offerRepo;

	@Autowired
	private OfferDao offerDao;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private VoucherDao voucherDao;

	@Value(value = "${dateFormat}")
	private String dateFormat;

	// find all bills
	@RequestMapping
	public List<BillEntity> getBills() {
		return (List<BillEntity>) billRepo.findAll();
	}

	// add new bill
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

		offerDao.updateOffer(offerEntity, true);

		return billRepo.save(newBill);
	}

	// modify an existing bill
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

		if (billEntity.getPaymentMade()) {
			voucherDao.createVoucher(billEntity);
		}

		if (billEntity.getPaymentCanceled()) {
			offerDao.updateOffer(updatedBill.getBillOffer(), false);
		}

		return billRepo.save(billEntity);
	}

	// delete bill
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public BillEntity deleteBill(@PathVariable Integer id) {

		BillEntity billEntity = billRepo.findById(id).get();

		if (billEntity == null) {
			return null;
		}

		billRepo.deleteById(id);

		return billEntity;
	}

	// find bill by buyer
	@RequestMapping(value = "/findByBuyer/{buyerId}", method = RequestMethod.GET)
	public List<BillEntity> getCustomerBills(@PathVariable Integer buyerId) {

		UserEntity buyer = userRepo.findById(buyerId).get();

		if (buyer == null) {
			return null;
		}

		return billRepo.findByBillUser(buyer);
	}

	// find bill by category
	@RequestMapping(value = "/findByCategory/{categoryId}")
	public List<BillEntity> getCategryBills(@PathVariable Integer categoryId) {

		CategoryEntity categoryEntity = categoryRepo.findById(categoryId).get();

		if (categoryEntity == null) {
			return null;
		}

		return billRepo.findBybillOfferOfferCategory(categoryEntity);
	}

	// find by date period
	@RequestMapping(value = "/findByDate/{startDate}/and/{endDate}")
	public List<BillEntity> getBillsIntoIntervalDate(@PathVariable String startDate, @PathVariable String endDate)
			throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date start = simpleDateFormat.parse(startDate);
		Date end = simpleDateFormat.parse(endDate);

		return billRepo.findByBillCreatedBetween(start, end);
	}

}
