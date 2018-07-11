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

import com.ITtraining.project.security.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class VoucherEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	@JsonView(Views.Public.class)
	@JsonProperty("Id")
	private Integer id;

	@Column(name = "expiration_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Views.Public.class)
	private Date expirationDate;

	@Column(name = "is_used")
	@JsonView(Views.Admin.class)
	private Boolean isUsed;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "voucherOffer")
	@JsonView(Views.Private.class)
	private OfferEntity voucherOffer;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "voucherUser")
	@JsonView(Views.Private.class)
	private UserEntity voucherUser;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public OfferEntity getVoucherOffer() {
		return voucherOffer;
	}

	public void setVoucherOffer(OfferEntity voucherOffer) {
		this.voucherOffer = voucherOffer;
	}

	public UserEntity getVoucherUser() {
		return voucherUser;
	}

	public void setVoucherUser(UserEntity voucherUser) {
		this.voucherUser = voucherUser;
	}

	public VoucherEntity() {
		super();
	}

}
