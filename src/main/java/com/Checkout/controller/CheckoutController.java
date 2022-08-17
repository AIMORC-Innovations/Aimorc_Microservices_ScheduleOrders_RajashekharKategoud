package com.Checkout.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.Checkout.Repository.DateRepository;
import com.Checkout.Repository.DeliveryRepository;
import com.Checkout.entity.Check;
import com.Checkout.entity.Date;
import com.Checkout.entity.DeliveryAddress;
import com.Checkout.services.CheckoutServices;

@Controller
@CrossOrigin("*")
public class CheckoutController {

	@Autowired
	private CheckoutServices dateServices;

	@Autowired
	private DateRepository dateRepo;

	@Autowired
	private DeliveryRepository deliveryRepo;

	RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/getScheduler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Date getScheduler(@RequestBody Check checkout) {
		Date date = new Date();
		String token = checkout.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		date.setUserid(userid);
		System.out.println("date scheduler "+ this.dateServices.getScheduler(date.getUserid()));
		return this.dateServices.getScheduler(date.getUserid());
	}
	@RequestMapping(value = "/setSchedulerAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void schedulingPickUpAddr(@RequestBody Check checkout){
		   Check check =new Check();
		   String token = checkout.getToken();
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
			String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int userid = Integer.parseInt(result);
			Integer datefromdb = dateRepo.findByUserid(userid);
			System.out.println("schedulingPickUpAddr"+checkout.getDel_address1());
			dateServices.updatePickUpAddr(checkout.getCity(),checkout.getCountry(),checkout.getDel_address1(),checkout.getState(),checkout.getZip(), userid);
	}
	
	@RequestMapping(value = "/getSchedulerAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void getschedulingPickUpAddr(@RequestBody Check checkout){
		   Check check =new Check();
		   String token = checkout.getToken();
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
			String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int userid = Integer.parseInt(result);
			Integer datefromdb = dateRepo.findByUserid(userid);
			System.out.println("getschedulingPickUpAddr"+checkout.getDel_address1());
			dateServices.updatePickUpAddr(checkout.getCity(),checkout.getCountry(),checkout.getDel_address1(),checkout.getState(),checkout.getZip(), userid);
	}

	@RequestMapping(value = "/setScheduler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void schedulingPickUp(@RequestBody Check checkout) {
		
		 Check check =new Check();
		
		String token = checkout.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		Integer datefromdb = dateRepo.findByUserid(userid);
		
		if (datefromdb == null) {
			
			System.out.println("date from db null "+dateServices.schedulingPickUp(checkout, userid));
			dateServices.schedulingPickUp(checkout, userid);

			
		} else {
			System.out.println("date from db not null "+dateServices.updatePickUp(checkout.getDate(),checkout.getDel_address1()	, userid));
			dateServices.updatePickUp(checkout.getDate(),checkout.getDel_address1()	, userid);
		}

	}

	@RequestMapping(value = "/cancelScheduler", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> cancelPickUp(@RequestBody Check checkout) {
		Date date = new Date();
		String token = checkout.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		date.setUserid(userid);
		System.out.println("cancel scheduled adress "+this.dateServices.cancelPickUp(date));
		return this.dateServices.cancelPickUp(date);
	}

	
	  @RequestMapping(value = "/deliveryProfileAddr", method = RequestMethod.POST, produces = "application/json")
	  @ResponseBody
	  public Map<String, Object>  deliveryProfileAddress(@RequestBody Check checkout) 
	      {
		 
		    String token = checkout.getToken();
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
			String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int userid = (int) Integer.parseInt(result);
			System.out.println(userid+" deliveryProfileAddr is "+ this.dateServices.deliveryProfileAddr(userid));
			return this.dateServices.deliveryProfileAddr(userid);
            

}
	  
	  @RequestMapping(value = "/displaydeliveryAddr", method = RequestMethod.POST, produces = "application/json")
	  @ResponseBody
	  public Map<String, Object>   displaydeliveryAddr(@RequestBody Check checkout) 
	      {
		    System.out.println("display delivery profile address");
		    String token = checkout.getToken();
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
			String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int userid = (int) Integer.parseInt(result);
			System.out.println(userid + " displaydeliveryAddr is "+this.dateServices.displaydeliveryAddress(userid));
			return this.dateServices.displaydeliveryAddress(userid);


}
	  @RequestMapping(value = "/updatedeliveryAddr", method = RequestMethod.POST, produces = "application/json")
	  @ResponseBody
	  public boolean  deliveryAddress(@RequestBody  Check checkout) 
	      {
	        
		    String token = checkout.getToken();
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
			String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int userid = Integer.parseInt(result);
			String Address1 = deliveryRepo.getAddress1(userid);
			String Address2 = deliveryRepo.getAddress2(userid);
		
			 
			    if(checkout.getDel_address1() != null )
					 {
			  
			      if(Address1 == null &&  Address2 == null) {
			    
						dateServices.updateDeliveryAddr1(checkout.getDel_address1(),userid);
				    	

				   }
			      else if(Address1 == null) {
			   
			    	  dateServices.updateDeliveryAddr1(checkout.getDel_address1(),userid);
			     	  
			      }
			      else {
					dateServices.updateDeliveryAddr2(checkout.getDel_address1(),userid);
				}
			      
			  	new ResponseEntity<String>("success!", HttpStatus.ACCEPTED);
				return true;
			    
					 }else
					 {
						  	new ResponseEntity<String>("Unable to update!", HttpStatus.ACCEPTED);
								 return false;
					 }
			


	      }

}