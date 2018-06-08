package com.example.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.project.entities.OfferEntity;
import com.example.project.entities.UserEntity;
import com.example.project.entities.VoucherEntity;

public interface VoucherRepository extends CrudRepository<VoucherEntity, Integer> {

	List<VoucherEntity> findByVoucherUser(UserEntity buyer);
	
	List<VoucherEntity> findByVoucherOffer(OfferEntity offerEntity);
	
	List<VoucherEntity> findByExpirationDateGreaterThanEqual(Date date);
	
}
