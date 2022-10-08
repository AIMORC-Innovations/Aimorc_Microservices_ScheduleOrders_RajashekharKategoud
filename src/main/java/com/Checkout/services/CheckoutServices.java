package com.Checkout.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Checkout.Repository.DateRepository;
import com.Checkout.Repository.DeliveryRepository;
import com.Checkout.Repository.ScheduleAddressRepository;
import com.Checkout.entity.Check;
import com.Checkout.entity.Date;
import com.Checkout.entity.DeliveryAddress;
import com.Checkout.entity.ScheduledAddress;


@Service
public class CheckoutServices{

	@Autowired
	private DateRepository daterepository;
	@Autowired
	private DeliveryRepository deliveryrepository;
	@Autowired
	private ScheduleAddressRepository scheduledaddressrepository;
	
	public List<Map<Date, Object>> getScheduler(int userid, String date, String del_address1, String city, String state, String country, String zip) {
		System.out.println("repo" + daterepository.fetchDate(userid, date, del_address1, city, state, country, zip));
		//return daterepository.fetchDate(userid, date, del_address1, city, state, country, zip);
		return daterepository.fetchDate(userid, date, del_address1, city, state, country, zip);
	}
	
	public List<Map<ScheduledAddress, Object>> getScheduledAddress(int userid,  String address,String address1,String city,String state,String country,String zip, String status, int address_id, String date_and_time, int scheduled_id ){
		return scheduledaddressrepository.fetchDate(userid, address, address1,city,state,country,zip,status,address_id,  date_and_time, scheduled_id);
	}
	
	public List<Map<ScheduledAddress, Object>> getDeliveryAddress(int userid, String address,String address1,String city,String state,String country,String zip, int address_id,  int del_id ){
		return deliveryrepository.fetchAddress(userid, address, address1,city,state,country,zip,address_id, del_id);
	}
	
	public Date schedulingPickUp(Check checkout, int userid) {
		Date date = new Date();
		date.setUserid(userid);
		date.setCountry(checkout.getCountry());
		date.setState(checkout.getState());
		date.setCity(checkout.getCity());
		date.setDate(checkout.getDate());
		date.setDel_address1(checkout.getDel_address1());
		date.setZip(checkout.getZip());
		
		return daterepository.save(date);
	}
	
	public int schedulingPickUpAddress(ScheduledAddress scheduledAddress, int userid) {
		scheduledAddress.getDate_and_time();
		scheduledAddress.getAddress_id();
		scheduledAddress.getStatus();
		scheduledAddress.getAddress();
		System.out.println(scheduledAddress.getDate_and_time()+"                      "+"              "+ userid );
		//scheduledaddressrepository.saveScheduledPickUpAddress(scheduledAddress.getDate_and_time(), scheduledAddress.getAddress_id(), userid );
		//return new ResponseEntity<String>("Scheduled address Successfully", HttpStatus.OK);
		return scheduledaddressrepository.saveScheduledPickUpAddress(scheduledAddress.getDate_and_time(), scheduledAddress.getAddress_id(),scheduledAddress.getAddress(), scheduledAddress.getStatus(), userid );
	}
	
	public int editscheduledPickUpAddress(ScheduledAddress scheduledAddress, int userid) {
		scheduledAddress.getDate_and_time();
		scheduledAddress.getAddress_id();
		scheduledAddress.getStatus();
		scheduledAddress.getAddress();
		System.out.println(scheduledAddress.getDate_and_time()+"                      "+"              "+ userid );
		return scheduledaddressrepository.editScheduledPickUpAddress(scheduledAddress.getDate_and_time(), scheduledAddress.getAddress_id(),scheduledAddress.getScheduled_id(), userid );
	} 
	
	public int editDeliveryAddress(ScheduledAddress scheduledAddress, int userid) {
		scheduledAddress.getUserid();
		scheduledAddress.getAddress_id();
		return deliveryrepository.editDeliveryAddress(scheduledAddress.getAddress_id(), userid );
	} 
	 

	
	@Transactional
	public int updatePickUp(String date,String del_address1,  int userid) {
		System.out.println(del_address1);
		return daterepository.updateDate(date,del_address1,userid);
	}


	
	public ResponseEntity<String> cancelPickUp(Date date) {
		daterepository.delete(date.getUserid());
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
		
	}
	
	public ResponseEntity<String> deleteScheduledPickUp(ScheduledAddress scheduledAddress){
		scheduledaddressrepository.delete(scheduledAddress.getScheduled_id(),scheduledAddress.getUserid() );
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
	}
	
	public ResponseEntity<String> deleteScheduledAddress(ScheduledAddress scheduledAddress){
		scheduledaddressrepository.deleteAddressId(scheduledAddress.getAddress_id(),scheduledAddress.getUserid() );
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
	}
	
	public boolean checkAddress(int userid, int address_id) {
		ScheduledAddress scheduledAddress = scheduledaddressrepository.findByUserId(userid, address_id);
		if(scheduledAddress.getAddress_id() == 0) {
			return false;
		}
		return true;
	}
	
	public ResponseEntity<String> cancelScheduledPickUp(ScheduledAddress scheduledAddress){
		scheduledaddressrepository.cancel(scheduledAddress.getScheduled_id(),scheduledAddress.getStatus(), scheduledAddress.getUserid() );
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
	}


	public Map<String, Object> deliveryProfileAddr( int userid) {
		//DeliveryAddress delAddress = new DeliveryAddress();
		//delAddress.setUserid(userid);
		//delAddress.setDel_profileaddr(checkout.getDel_profileaddr());
		return this.deliveryrepository.getAddress(userid);
	}


	@Transactional
	public int updateDeliveryAddr3(String del_address1,String del_address2, int userid) {
		//DeliveryAddress delAddr2 =new DeliveryAddress();
		//delAddr2.setUserid(userid);
		//delAddr2.setDel_address2(del_address2);
		return deliveryrepository.updateAddress3(del_address1,del_address2,userid);
	}
	
	@Transactional
	public int addDeliveryAddr(int address_id, int userid) {
		return deliveryrepository.saveAddr(address_id, userid);
	}
	
	@Transactional
	public int updateDeliveryAddr1(String del_address1, int userid) {
		//DeliveryAddress delAddr2 =new DeliveryAddress();
		//delAddr2.setUserid(userid);
		//delAddr2.setDel_address2(del_address2);
		return deliveryrepository.updateAddress1(del_address1,userid);
	}
	
	@Transactional
	public int updateDeliveryAddr2(String del_address1, int userid) {
		//DeliveryAddress delAddr1 =new DeliveryAddress();
	//	delAddr1.setUserid(userid);
		//delAddr1.setDel_address3(del_address1);
		return this.deliveryrepository.updateAddress2(del_address1,userid);
		
	}


	public Map<String, Object> displaydeliveryAddress(int userid) {
		
		return this.deliveryrepository.getAddress(userid);
	}





	public Date updatePickUpAddr(String city, String country, String del_address1, String state, String zip,int userid) {
		// TODO Auto-generated method stub
		return daterepository.saveAddress(del_address1,country,city,state,zip,userid);
		}
	
	/*
	 * public List<Object> displaydelAddress1(int userid) {
	 * 
	 * return this.deliveryrepository.getAddress1(userid); } public List<Object>
	 * displaydelAddress2(int userid) {
	 * 
	 * return this.deliveryrepository.getAddress2(userid); }
	 */
 	
	

		

}
