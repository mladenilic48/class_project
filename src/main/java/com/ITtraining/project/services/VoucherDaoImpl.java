package com.ITtraining.project.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.VoucherEntity;
import com.ITtraining.project.repositories.VoucherRepository;

@Service
public class VoucherDaoImpl implements VoucherDao {

	@Autowired
	private VoucherRepository voucherRepo;

	@Autowired
	private EmailService emailService;

	@Override
	public VoucherEntity createVoucher(BillEntity bill) {
		VoucherEntity voucher = new VoucherEntity();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 7);

		voucher.setVoucherUser(bill.getBillUser());
		voucher.setVoucherOffer(bill.getBillOffer());
		voucher.setExpirationDate(cal.getTime());
		voucher.setIsUsed(false);

		try {
			emailService.sendTemplateMessage(voucher);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return voucherRepo.save(voucher);
	}
}
