package com.Checkout.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Check {



		
		private String userid;
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

