package com.ITtraining.project.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ITtraining.project.entities.BillEntity;
import com.ITtraining.project.entities.CategoryEntity;
import com.ITtraining.project.entities.OfferEntity;
import com.ITtraining.project.entities.dto.ReportDTO;
import com.ITtraining.project.entities.dto.ReportItemDTO;
import com.ITtraining.project.repositories.BillRepository;
import com.ITtraining.project.repositories.CategoryRepository;

@Service
public class BillDaoImpl implements BillDao {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private BillRepository billRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Override
	public List<BillEntity> findActiveBillsForCategory(CategoryEntity categoryEntity) {

		return billRepo.findByPaymentCanceledAndBillOffer_OfferCategory(false, categoryEntity);
	}

	@Override
	public void cancelBillsForOffer(OfferEntity offerEntity) {

		List<BillEntity> bills = billRepo.findByBillOffer(offerEntity);
		for (BillEntity billEntity : bills) {
			billEntity.setPaymentCanceled(true);
		}

		billRepo.saveAll(bills);
	}

	@Override
	public ReportDTO createReportByDate(Date startDate, Date endDate) {

		List<ReportItemDTO> items = new ArrayList<ReportItemDTO>();
		List<Date> dates = datePeriod(startDate, endDate);

		double income = 0, totalIncome = 0;
		int numberOfOffers = 0, totalNumberOfOffers = 0;

		for (Date date : dates) {

			for (BillEntity bill : findBillsInPeriod(startDate, endDate)) {
				if (date.equals(bill.getBillCreated())) {
					income += bill.getBillOffer().getActionPrice();
					numberOfOffers++;
				}
			}

			items.add(new ReportItemDTO(date, income, numberOfOffers));
			totalIncome += income;
			totalNumberOfOffers += numberOfOffers;
			income = 0;
			numberOfOffers = 0;
		}

		ReportDTO report = new ReportDTO();
		report.setReportItems(items);
		report.setSumOfIncomes(totalIncome);
		report.setTotalNumberOfSoldOffers(totalNumberOfOffers);

		return report;
	}

	@Override
	public ReportDTO createReportByCategoryAndDate(Date startDate, Date endDate, Integer categoryId) {

		List<ReportItemDTO> items = new ArrayList<ReportItemDTO>();
		List<Date> dates = datePeriod(startDate, endDate);

		double income = 0, totalIncome = 0;
		int numberOfOffers = 0, totalNumberOfOffers = 0;

		for (Date date : dates) {

			for (BillEntity bill : findBillsByCategoryAndPeriod(startDate, endDate, categoryId)) {
				if (date.equals(bill.getBillCreated())) {
					income += bill.getBillOffer().getActionPrice();
					numberOfOffers++;
				}
			}

			items.add(new ReportItemDTO(date, income, numberOfOffers));
			totalIncome += income;
			totalNumberOfOffers += numberOfOffers;
			income = 0;
			numberOfOffers = 0;
		}

		ReportDTO report = new ReportDTO();
		report.setReportItems(items);
		report.setSumOfIncomes(totalIncome);
		report.setTotalNumberOfSoldOffers(totalNumberOfOffers);
		report.setCategoryName(categoryRepo.findById(categoryId).get().getCategoryName());

		return report;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillEntity> findBillsInPeriod(Date startDate, Date endDate) {

		String sql = "select b" + "from BillEntity b" + "where b.billMade >= :startDate and b.billMade <= endDate";

		Query query = entityManager.createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		List<BillEntity> result = new ArrayList<BillEntity>();
		result = query.getResultList();

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillEntity> findBillsByCategoryAndPeriod(Date startDate, Date endDate, Integer categoryId) {

		String sql = "select b" + "from BillEntity b" + " left join fetch b.offer o left join fetch o.offerCategory c"
				+ "where b.billMade >= :startDate and b.billMade <= endDate and c.id=:id";

		Query query = entityManager.createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("id", categoryId);

		List<BillEntity> result = new ArrayList<BillEntity>();
		result = query.getResultList();

		return result;
	}

	// list of dates between start and end date
	private List<Date> datePeriod(Date startDate, Date endDate) {

		List<Date> dates = new ArrayList<Date>();

		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);

		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		while (startCalendar.before(endCalendar)) {
			Date result = startCalendar.getTime();
			dates.add(result);
			startCalendar.add(Calendar.DATE, 1);
		}

		dates.add(endDate);

		return dates;
	}

}