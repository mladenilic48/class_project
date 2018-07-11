package com.ITtraining.project.entities.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportItemDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Belgrade")
	private Date date;

	private Double income;

	private Integer numberOfOffers;

	
	public ReportItemDTO() {
		super();
	}

	public ReportItemDTO(Date date, Double income, Integer numberOfOffers) {
		super();
		this.date = date;
		this.income = income;
		this.numberOfOffers = numberOfOffers;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	public Integer getNumberOfOffers() {
		return numberOfOffers;
	}

	public void setNumberOfOffers(Integer numberOfOffers) {
		this.numberOfOffers = numberOfOffers;
	}

}
