package com.ITtraining.project.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ITtraining.project.controllers.util.RESTError;
import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.UserEntity;
import com.ITtraining.project.entitiesEnum.EUserRole;
import com.ITtraining.project.repositories.BillRepository;
import com.ITtraining.project.repositories.CategoryRepository;
import com.ITtraining.project.repositories.OfferRepository;
import com.ITtraining.project.repositories.UserRepository;
import com.ITtraining.project.security.Views;
import com.ITtraining.project.services.OfferDao;
import com.ITtraining.project.services.VoucherDao;
import com.fasterxml.jackson.annotation.JsonView;

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

	// find all public bills
	@RequestMapping(value = "/public")
	@JsonView(Views.Public.class)
	public ResponseEntity<?> getBillsPublic() {
		return new ResponseEntity<Iterable<BillEntity>>(billRepo.findAll(), HttpStatus.OK);
	}

	// find all private bills
	@RequestMapping(value = "/private")
	@JsonView(Views.Private.class)
	public ResponseEntity<?> getBillsPrivate() {
		return new ResponseEntity<Iterable<BillEntity>>(billRepo.findAll(), HttpStatus.OK);
	}

	// find all admin bills
	@RequestMapping(value = "/admin")
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> getBillsAdmin() {
		return new ResponseEntity<Iterable<BillEntity>>(billRepo.findAll(), HttpStatus.OK);
	}

	// add new bill
	@RequestMapping(value = "/{offerId}/buyer/{buyerId}", method = RequestMethod.POST)
	public ResponseEntity<?> addBill(@PathVariable Integer offerId, @PathVariable Integer buyerId) {

		BillEntity newBill = new BillEntity();
		OfferEntity offerEntity = offerRepo.findById(offerId).get();
		UserEntity buyer = userRepo.findById(buyerId).get();

		if (offerEntity == null || buyer == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Offer with provided ID not found"),
					HttpStatus.NOT_FOUND);
		}

		if (!buyer.getUserRole().equals(EUserRole.ROLE_CUSTOMER)) {
			return new ResponseEntity<RESTError>(new RESTError(2, "User with provided ID is not buyer."),
					HttpStatus.BAD_REQUEST);
		}

		newBill.setBillCreated(new Date());
		newBill.setPaymentCanceled(false);
		newBill.setPaymentMade(false);
		newBill.setBillOffer(offerEntity);
		newBill.setBillUser(buyer);

		offerDao.updateOffer(offerEntity, true);

		return new ResponseEntity<BillEntity>(billRepo.save(newBill), HttpStatus.OK);
	}

	// modify an existing bill
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBill(@PathVariable Integer id, @RequestBody BillEntity updatedBill) {

		BillEntity billEntity = billRepo.findById(id).get();

		if (billEntity == null || updatedBill == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Bill with provided ID not found"),
					HttpStatus.NOT_FOUND);
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

		return new ResponseEntity<BillEntity>(billRepo.save(billEntity), HttpStatus.OK);
	}

	// delete bill
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBill(@PathVariable Integer id) {

		BillEntity billEntity = billRepo.findById(id).get();

		if (billEntity == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Bill with provided ID not found"),
					HttpStatus.NOT_FOUND);
		}

		billRepo.deleteById(id);

		return new ResponseEntity<BillEntity>(billEntity, HttpStatus.OK);
	}

	// find bill by buyer
	@RequestMapping(value = "/findByBuyer/{buyerId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerBills(@PathVariable Integer buyerId) {

		UserEntity buyer = userRepo.findById(buyerId).get();

		if (buyer == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Buyer with provided ID not found"),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<BillEntity>>(billRepo.findByBillUser(buyer), HttpStatus.OK);
	}

	// find bill by category
	@RequestMapping(value = "/findByCategory/{categoryId}")
	public ResponseEntity<?> getCategryBills(@PathVariable Integer categoryId) {

		CategoryEntity categoryEntity = categoryRepo.findById(categoryId).get();

		if (categoryEntity == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Category with provided ID not found"),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<BillEntity>>(billRepo.findBybillOfferOfferCategory(categoryEntity),
				HttpStatus.OK);
	}

	// find by date period
	@RequestMapping(value = "/findByDate/{startDate}/and/{endDate}")
	public ResponseEntity<?> getBillsIntoIntervalDate(@PathVariable String startDate, @PathVariable String endDate)
			throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date start = simpleDateFormat.parse(startDate);
		Date end = simpleDateFormat.parse(endDate);

		return new ResponseEntity<List<BillEntity>>(billRepo.findByBillCreatedBetween(start, end), HttpStatus.OK);
	}

	// find by Id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id) {

		if (billRepo.findById(id).isPresent()) {
			return new ResponseEntity<BillEntity>(billRepo.findById(id).get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(1, "Bill with provided ID not found"),
					HttpStatus.NOT_FOUND);
		}

	}
}
