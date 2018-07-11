package com.ITtraining.project.entities.dto;

import java.util.List;

public class ReportDTO {

	private String categoryName;

	private List<ReportItemDTO> reportItems;

	private Double sumOfIncomes;

	private Integer totalNumberOfSoldOffers;

	public ReportDTO() {
		super();
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<ReportItemDTO> getReportItems() {
		return reportItems;
	}

	public void setReportItems(List<ReportItemDTO> reportItems) {
		this.reportItems = reportItems;
	}

	public Double getSumOfIncomes() {
		return sumOfIncomes;
	}

	public void setSumOfIncomes(Double sumOfIncomes) {
		this.sumOfIncomes = sumOfIncomes;
	}

	public Integer getTotalNumberOfSoldOffers() {
		return totalNumberOfSoldOffers;
	}

	public void setTotalNumberOfSoldOffers(Integer totalNumberOfSoldOffers) {
		this.totalNumberOfSoldOffers = totalNumberOfSoldOffers;
	}

}
