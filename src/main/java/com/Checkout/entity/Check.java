package com.Checkout.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Check {



		
		private int userid;
		private int designer_userid;
		private String date;
		private String token;
		private int address_id; //String
		private String country;
		private String state;
		private String city;
		private String zip;
		private String del_profileaddr;
		private String del_address1;
		private String del_address2;
		private String del_address3;
		private int scheduled_id;
		private String gender;
		private int height;
		private int chest;
		private int waist;
		private int hip;
		private int inseam;
		private String status;
		private String measurement_unit;
		private String dressing_category;
//		private Integer shirts_collar_size;
//		private Integer shirts_sleeve_length;
//		private Integer shirts_size;
//		private Integer t_shirt_size;
//		private Integer pant_waist_size;
//		private Integer pant_inseam_length;
//		private Integer pant_size;
//		private Integer blouse_bust_size;
//		private Integer blouse_waist_size;
//		private Integer blouse_shoulder_size;
//		private Integer blouse_neck_size;
//		private Integer blouse_length;
//		private Integer blouse_sleeve_length;
//		private Integer tops_bust_size;
//		private Integer tops_waist_size;
//		private Integer tops_shoulder_size;
//		private Integer tops_sleeve_length;
		
		private int shirts_collar_size;
		private int shirts_sleeve_length;
		private int shirts_size;
		private int t_shirt_size;
		private int pant_waist_size;
		private int pant_inseam_length;
		private int pant_size;
		private int blouse_bust_size;
		private int blouse_waist_size;
		private int blouse_shoulder_size;
		private int blouse_neck_size;
		private int blouse_length;
		private int blouse_sleeve_length;
		private int tops_bust_size;
		private int tops_waist_size;
		private int tops_shoulder_size;
		private int tops_sleeve_length;
		private int measurement_id;
		private boolean fabric_collected;
		
		public boolean getFabricCollected() {
	        return fabric_collected;
	    }
		
		//private String del_address3;
	

//		public String getToken() {
//			return token;
//		}
//
//		public void setToken(String token) {
//			this.token = token;
//		}
//
//		public String getUsername() {
//			return username;
//		}
//		
//		public void setUsername(String username) {
//			this.username = username;
//		}
//
//		public String getDate() {
//			return date;
//		}
//
//		public void setDate(String date) {
//			this.date = date;
//		}
//		
//		
		

	}

