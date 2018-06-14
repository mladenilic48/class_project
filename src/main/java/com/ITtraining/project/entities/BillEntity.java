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

@Entity
@Table(name = "bills")
public class BillEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@Column(name = "payment_made")
	private Boolean paymentMade;

	@Column(name = "payment_canceled")
	private Boolean paymentCanceled;

	@Column(name = "bill_created")
	private Date billCreated;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "billOffer")
	private OfferEntity billOffer;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "billUser")
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
