package com.Checkout.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="scheduled_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledAddress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int scheduled_id;
	private int address_id;
	private String token;
	private int userid;
	private String date_and_time;
	private String address;
	private String address1;
	private String city;
	private String state;
	private String country;
	private String zip;
	private String status;

}
