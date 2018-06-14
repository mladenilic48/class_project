package com.ITtraining.project.services;

import com.ITtraining.project.entities.VoucherEntity;

public interface EmailService {

	void sendTemplateMessage(VoucherEntity voucher) throws Exception;
}
