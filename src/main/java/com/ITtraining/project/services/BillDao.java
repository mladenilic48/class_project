package com.ITtraining.project.services;

import java.util.Date;
import java.util.List;

import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.dto.ReportDTO;

public interface BillDao {

	List<BillEntity> findActiveBillsForCategory(CategoryEntity categoryEntity);

	void cancelBillsForOffer(OfferEntity offerEntity);

	ReportDTO createReportByDate(Date startDate, Date endDate);

	ReportDTO createReportByCategoryAndDate(Date startDate, Date endDate, Integer categoryId);

	List<BillEntity> findBillsInPeriod(Date startDate, Date endDate);

	List<BillEntity> findBillsByCategoryAndPeriod(Date startDate, Date endDate, Integer categoryId);
}