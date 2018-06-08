package com.example.project.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.project.entities.BillEntity;
import com.example.project.entities.CategoryEntity;
import com.example.project.entities.UserEntity;

public interface BillRepository extends CrudRepository<BillEntity, Integer> {

	List<BillEntity> findByBillUser(UserEntity userEntity);

	List<BillEntity> findBybillOfferOfferCategory(CategoryEntity categoryEntity);
	
	List<BillEntity> findByBillCreatedBetween(Date startDate, Date endDate);
}
