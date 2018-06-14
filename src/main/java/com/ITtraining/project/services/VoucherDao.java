package com.ITtraining.project.services;

import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.VoucherEntity;

public interface VoucherDao {

	public VoucherEntity createVoucher(BillEntity bill);
}
