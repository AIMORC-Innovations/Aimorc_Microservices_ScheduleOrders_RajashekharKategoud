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
@Table(name="date")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Date {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int date_id;
  	private String date;
    private int userid;
    private String country;
	private String state;
	private String city;
	private String del_address1;
	/*
	 * private String del_address2; private String del_address3;
	 */
	private String zip;

}
