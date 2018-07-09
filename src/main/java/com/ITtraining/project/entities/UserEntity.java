package com.ITtraining.project.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ITtraining.project.entitiesEnum.EUserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	@JsonIgnore
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "user_role")
	private EUserRole userRole;

	@OneToMany(mappedBy = "seller", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<OfferEntity> sellerOffers = new ArrayList<>();

	@OneToMany(mappedBy = "billUser", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<BillEntity> userBills = new ArrayList<>();

	@OneToMany(mappedBy = "voucherUser", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<VoucherEntity> userVouchers = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EUserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(EUserRole userRole) {
		this.userRole = userRole;
	}

	public List<OfferEntity> getSellerOffers() {
		return sellerOffers;
	}

	public void setSellerOffers(List<OfferEntity> sellerOffers) {
		this.sellerOffers = sellerOffers;
	}

	public List<BillEntity> getUserBills() {
		return userBills;
	}

	public void setUserBills(List<BillEntity> userBills) {
		this.userBills = userBills;
	}

	public List<VoucherEntity> getUserVouchers() {
		return userVouchers;
	}

	public void setUserVouchers(List<VoucherEntity> userVouchers) {
		this.userVouchers = userVouchers;
	}

	public UserEntity() {
		super();
	}

	public UserEntity(Integer id, String firstName, String lastName, String username, String password, String email,
			EUserRole userRole) {
		super();
		this.setId(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.setUserRole(userRole);
	}

}
