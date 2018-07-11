package com.ITtraining.project.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ITtraining.project.security.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "bills")
public class BillEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	@JsonView(Views.Public.class)
	@JsonProperty("Id")
	private Integer id;

	@Column(name = "payment_made")
	@JsonView(Views.Admin.class)
	private Boolean paymentMade;

	@Column(name = "payment_canceled")
	@JsonView(Views.Admin.class)
	private Boolean paymentCanceled;

	@Column(name = "bill_created")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Views.Public.class)
	private Date billCreated;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "billOffer")
	@JsonView(Views.Private.class)
	private OfferEntity billOffer;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "billUser")
	@JsonView(Views.Private.class)
	private UserEntity billUser;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getPaymentMade() {
		return paymentMade;
	}

	public void setPaymentMade(Boolean paymentMade) {
		this.paymentMade = paymentMade;
	}

	public Boolean getPaymentCanceled() {
		return paymentCanceled;
	}

	public void setPaymentCanceled(Boolean paymentCanceled) {
		this.paymentCanceled = paymentCanceled;
	}

	public Date getBillCreated() {
		return billCreated;
	}

	public void setBillCreated(Date billCreated) {
		this.billCreated = billCreated;
	}

	public OfferEntity getBillOffer() {
		return billOffer;
	}

	public void setBillOffer(OfferEntity billOffer) {
		this.billOffer = billOffer;
	}

	public UserEntity getBillUser() {
		return billUser;
	}

	public void setBillUser(UserEntity billUser) {
		this.billUser = billUser;
	}

	public BillEntity() {
		super();
	}

}
