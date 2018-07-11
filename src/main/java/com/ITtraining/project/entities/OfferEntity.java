package com.ITtraining.project.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ITtraining.project.entitiesEnum.EOfferStatus;
import com.ITtraining.project.security.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "offers")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class OfferEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	@JsonView(Views.Public.class)
	@JsonProperty("Id")
	private Integer id;

	@Column(name = "available_offers")
	@JsonView(Views.Public.class)
	private Integer availableOffers;

	@Column(name = "bought_offers")
	@JsonView(Views.Public.class)
	private Integer boughtOffers;

	@Column(name = "offer_created")
	@JsonView(Views.Public.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date offerCreated;

	@Column(name = "offer_expires")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonView(Views.Public.class)
	private Date offerExpires;

	@Column(name = "offer_name")
	@JsonView(Views.Public.class)
	private String offerName;

	@Column(name = "offer_description")
	@JsonView(Views.Public.class)
	private String offerDescription;

	@Column(name = "image_path")
	@JsonView(Views.Public.class)
	private String imagePath;

	@Column(name = "regular_price")
	@JsonView(Views.Public.class)
	private Double regularPrice;

	@Column(name = "action_price")
	@JsonView(Views.Public.class)
	private Double actionPrice;

	@Column(name = "offer_status")
	@JsonView(Views.Public.class)
	private EOfferStatus offerStatus;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "offerCategory")
	@JsonView(Views.Public.class)
	private CategoryEntity offerCategory;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "seller")
	@JsonView(Views.Public.class)
	private UserEntity seller;

	@OneToMany(mappedBy = "billOffer", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<BillEntity> offerBills = new ArrayList<>();

	@OneToMany(mappedBy = "voucherOffer", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<VoucherEntity> offerVouchers = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAvailableOffers() {
		return availableOffers;
	}

	public void setAvailableOffers(Integer availableOffers) {
		this.availableOffers = availableOffers;
	}

	public Integer getBoughtOffers() {
		return boughtOffers;
	}

	public void setBoughtOffers(Integer boughtOffers) {
		this.boughtOffers = boughtOffers;
	}

	public Date getOfferCreated() {
		return offerCreated;
	}

	public void setOfferCreated(Date offerCreated) {
		this.offerCreated = offerCreated;
	}

	public Date getOfferExpires() {
		return offerExpires;
	}

	public void setOfferExpires(Date offerExpires) {
		this.offerExpires = offerExpires;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getOfferDescription() {
		return offerDescription;
	}

	public void setOfferDescription(String offerDescription) {
		this.offerDescription = offerDescription;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Double getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(Double regularPrice) {
		this.regularPrice = regularPrice;
	}

	public Double getActionPrice() {
		return actionPrice;
	}

	public void setActionPrice(Double actionPrice) {
		this.actionPrice = actionPrice;
	}

	public EOfferStatus getOfferStatus() {
		return offerStatus;
	}

	public void setOfferStatus(EOfferStatus offerStatus) {
		this.offerStatus = offerStatus;
	}

	public CategoryEntity getOfferCategory() {
		return offerCategory;
	}

	public void setOfferCategory(CategoryEntity offerCategory) {
		this.offerCategory = offerCategory;
	}

	public UserEntity getSeller() {
		return seller;
	}

	public void setSeller(UserEntity seller) {
		this.seller = seller;
	}

	public List<BillEntity> getOfferBills() {
		return offerBills;
	}

	public void setOfferBills(List<BillEntity> offerBills) {
		this.offerBills = offerBills;
	}

	public List<VoucherEntity> getOfferVouchers() {
		return offerVouchers;
	}

	public void setOfferVouchers(List<VoucherEntity> offerVouchers) {
		this.offerVouchers = offerVouchers;
	}

	public OfferEntity() {
		super();
	}

	public OfferEntity(Integer id, Integer availableOffers, Integer boughtOffers, Date offerCreated, Date offerExpires,
			String offerName, String offerDescription, String imagePath, Double regularPrice, Double actionPrice,
			EOfferStatus offerStatus) {
		super();
		this.setId(id);
		this.availableOffers = availableOffers;
		this.boughtOffers = boughtOffers;
		this.offerCreated = offerCreated;
		this.offerExpires = offerExpires;
		this.offerName = offerName;
		this.offerDescription = offerDescription;
		this.imagePath = imagePath;
		this.regularPrice = regularPrice;
		this.actionPrice = actionPrice;
		this.offerStatus = offerStatus;
	}

}
