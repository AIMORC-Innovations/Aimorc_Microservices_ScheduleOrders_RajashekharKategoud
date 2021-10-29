package com.Checkout.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name="deliveryaddress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int del_id;
	private int userid;
  	private String del_address1;
	private String del_address2;
	   


}
