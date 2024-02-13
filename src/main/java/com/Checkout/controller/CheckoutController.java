package com.Checkout.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.Checkout.Repository.DateRepository;
import com.Checkout.Repository.DeliveryRepository;
import com.Checkout.entity.Check;
import com.Checkout.entity.Date;
import com.Checkout.entity.DeliveryAddress;
import com.Checkout.entity.ScheduledAddress;
import com.Checkout.services.CheckoutServices;

//from here added
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.awt.geom.Rectangle;
//pdf download
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.itextpdf.text.Image;

//this.dateServices.methodName(); points to the method in the CheckoutServices.java file

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
	
	private ObjectMapper objectMapper = new ObjectMapper();

    public void RouteServiceController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    //calculates the distances between the designer location and user delivery location and returns distance in km.
    //@GetMapping("/calculateDistance")
    @RequestMapping(value = "/calculateDistance", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
    public String calculateDistance(
            @RequestParam("startLat") String startLat,
            @RequestParam("startLon") String startLon,
            @RequestParam("endLat") String endLat,
            @RequestParam("endLon") String endLon
    ) {
        String apiKey = "5b3ce3597851110001cf6248561b9913668b440b935ebfe2b5877858";
        String url = String.format("https://api.openrouteservice.org/v2/directions/driving-car?api_key=%s&start=%s,%s&end=%s,%s&units=m",
                apiKey, startLon, startLat, endLon, endLat);

        HttpHeaders headers = new HttpHeaders();
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        String responseBody = responseEntity.getBody();

        try {
            JsonNode responseJson = objectMapper.readTree(responseBody);
            JsonNode features = responseJson.get("features");
            if (features != null && features.isArray() && features.size() > 0) {
                JsonNode properties = features.get(0).get("properties");
                if (properties != null) {
                    double totalDistance = 0.0;
                    JsonNode segments = properties.get("segments");
                    if (segments != null && segments.isArray()) {
                        for (JsonNode segment : segments) {
                            double distance = segment.get("distance").asDouble();
                            totalDistance += distance;
                        }
                    }
                    double totalDistanceKm = totalDistance / 1000.0;
                    System.out.println(totalDistanceKm);
                    return String.valueOf(totalDistanceKm);//return totalDistance;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to parse the response and extract the distance.");
    }

//    @GetMapping("/calculateDistance")
//    public Double calculateDistance(
//            @RequestParam("startLat") String startLat,
//            @RequestParam("startLon") String startLon,
//            @RequestParam("endLat") String endLat,
//            @RequestParam("endLon") String endLon
//    ) {
//        String apiKey = "5b3ce3597851110001cf6248561b9913668b440b935ebfe2b5877858";
//        String url = String.format("https://api.openrouteservice.org/v2/directions/driving-car?api_key=%s&start=%s,%s&end=%s,%s",
//                apiKey, startLon, startLat, endLon, endLat);
//
//        HttpHeaders headers = new HttpHeaders();
//        //headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
//
//        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
//        String responseBody = responseEntity.getBody();
//
//        try {
//            JsonNode responseJson = objectMapper.readTree(responseBody);
//            JsonNode routes = responseJson.get("routes");
//            if (routes != null && routes.isArray() && routes.size() > 0) {
//            	JsonNode properties = routes.get(0).get("properties");
//                JsonNode summary = routes.get(0).get("summary");
//                if (summary != null) {
////                    double distance = summary.get("distance").asDouble();
////                    return distance;
//                	double totalDistance = 0.0;
//                    JsonNode segments = summary.get("segments");
//                    if (segments != null && segments.isArray()) {
//                        for (JsonNode segment : segments) {
//                            double distance = segment.get("distance").asDouble();
//                            String distanceUnit = segment.get("distance_unit").asText();
//                            if (distanceUnit.equals("m")) {
//                                totalDistance += distance;
//                            } else if (distanceUnit.equals("km")) {
//                                totalDistance += distance * 1000; // Convert kilometers to meters
//                            }
//                        }
//                    }
//                    return totalDistance;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        throw new RuntimeException("Failed to parse the response and extract the distance.");
//    }
	
//	@GetMapping("/calculateDistance")
//    public String calculateDistance(
//            @RequestParam("startLat") String startLat,
//            @RequestParam("startLon") String startLon,
//            @RequestParam("endLat") String endLat,
//            @RequestParam("endLon") String endLon
//    ) {
//        String apiKey = "5b3ce3597851110001cf6248561b9913668b440b935ebfe2b5877858";
//        String url = String.format("https://api.openrouteservice.org/v2/directions/driving-car?api_key=%s&start=%s,%s&end=%s,%s",
//                apiKey, startLon, startLat, endLon, endLat);
//        
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(url, String.class);
//        
//        // Parse the response JSON to extract the distance
//        // You can use a JSON parsing library like Jackson or Gson for this
//        // Assuming the JSON structure is {"routes":[{"summary":{"distance":123.45}}]}
//        // you can extract the distance as follows:
//        // double distance = Double.parseDouble(response.routes[0].summary.distance);
//        
//        return response; // Return the response from OpenRouteService API
//    }

    //get the Schedule address for user ,it will take address and userid as arugmenet
	@RequestMapping(value = "/getScheduler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<Map<Date, Object>> getScheduler(@RequestBody Check checkout) {
		Date date = new Date();
		String token = checkout.getToken();
		System.out.println("1               " + token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		date.setUserid(userid);
		List date1=dateServices.getScheduler(date.getUserid(), date.getDate(), date.getDel_address1(), date.getCity(), date.getCountry(), date.getState(), date.getZip());
		for(int i=0;i<date1.size();i++){
		    System.out.println("address "+i+date1.get(i));
		} 
		return this.dateServices.getScheduler(date.getUserid(), date.getDate(), date.getDel_address1(), date.getCity(), date.getCountry(), date.getState(), date.getZip());
	}
	
	//get the Schedule address for user ,it will take userid as argument
	@RequestMapping(value = "/getScheduledAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  List<Map<ScheduledAddress, Object>> getScheduledAddress(@RequestBody Check checkout){
		ScheduledAddress scheduledAddress = new ScheduledAddress();
		String token = checkout.getToken();
		System.out.println("2                 " +token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println(scheduledAddress.getAddress_id()+"----------");
		return this.dateServices.getScheduledAddress(scheduledAddress.getUserid(), scheduledAddress.getAddress(),scheduledAddress.getAddress1(), scheduledAddress.getCity(), scheduledAddress.getState(), scheduledAddress.getCountry(), scheduledAddress.getZip(), scheduledAddress.getStatus(), scheduledAddress.getAddress_id(), scheduledAddress.getDate_and_time(), scheduledAddress.getScheduled_id() );
	}  
	
	//is used to get the measurements of logged in users based on the userid
	@RequestMapping(value = "/getMyMeasurements", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  List<Map<ScheduledAddress, Object>> getMyMeasurements(@RequestBody Check checkout){
		String token = checkout.getToken();
		System.out.println("2                 " +token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		System.out.println("--->"+userid );
		checkout.setUserid(userid);
		//System.out.println(this.dateServices.getMyMeasurements(userid));
		//return this.dateServices.getMyMeasurements( userid );
		return this.dateServices.getMyMeasurementsForMySchedules( checkout.getUserid());
	}  
	
	//is used to get the measurements of logged in users based on the userid when scheduling the appointment
	@RequestMapping(value = "/getMyMeasurementsForMySchedules", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  List<Map<ScheduledAddress, Object>> getMyMeasurementsForMySchedules(@RequestBody Check checkout){
//		String token = checkout.getToken();
//		System.out.println("2                 " +token);
//		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
//		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
//		int userid = Integer.parseInt(result);
//		System.out.println("--->"+userid );
//		checkout.setUserid(userid);
		//System.out.println(this.dateServices.getMyMeasurements(userid));
		return this.dateServices.getMyMeasurementsForMySchedules( checkout.getUserid());
	}  
	/*
	@PostMapping("/getMyMeasurementsForScheudle")
	@ResponseBody
	public Map<String, Object> getUserAddressDetails(@RequestBody Check checkout) {
		String token = checkout.getToken();
		System.out.println("2                 " +token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		System.out.println("--->"+userid );
		checkout.setUserid(userid);
		Map<String, Object> myMeasurements = dateServices.getMyMeasurementsDetails(userid);
		return myMeasurements; //useraddressdetails
	}*/
	
	//is used to get the all scheduled addresses
	@RequestMapping(value = "/getAllScheduledAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  List<Map<ScheduledAddress, Object>> getAllScheduledAddress(@RequestBody Check checkout){
		ScheduledAddress scheduledAddress = new ScheduledAddress();
		String token = checkout.getToken();
		System.out.println("2                 " +token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println("<---->"+userid);
		System.out.println(scheduledAddress.getUserid());
		List<Map<String, Object>> Address=this.dateServices.getMyAddress(userid);
		String designerCity = null;
		String designerState = null;
		for (Map<String, Object> addressMap : Address) {
			designerCity = (String) addressMap.get("city");
		     designerState = (String) addressMap.get("state");
		    String country = (String) addressMap.get("country");
		    String zip = (String) addressMap.get("zip");

		    System.out.println("City: " + designerCity);
		    System.out.println("State: " + designerState);
		    System.out.println("Country: " + country);
		    System.out.println("Zip: " + zip);
		}
		System.out.println(scheduledAddress.getAddress_id()+"----------");
		return this.dateServices.getAllScheduledAddress(scheduledAddress.getUserid(), scheduledAddress.getAddress(),
		scheduledAddress.getAddress1(), scheduledAddress.getCity(), scheduledAddress.getState(), scheduledAddress.getCountry(),
		scheduledAddress.getZip(), scheduledAddress.getStatus(), result, result, result, scheduledAddress.getAddress_id(), 
		scheduledAddress.getDate_and_time(), scheduledAddress.getScheduled_id(), designerCity, designerState );
	}  
	
	//is used to get my scheduled addresses
	@RequestMapping(value = "/getMyScheduledAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  List<Map<ScheduledAddress, Object>> getMyScheduledAddress(@RequestBody Check checkout){
		ScheduledAddress scheduledAddress = new ScheduledAddress();
		String token = checkout.getToken();
		System.out.println("2                 " +token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println("<---->"+userid);
		System.out.println(scheduledAddress.getUserid());
		List<Map<String, Object>> Address=this.dateServices.getMyAddress(userid);
		String designerCity = null;
		String designerState = null;
		/*
		for (Map<String, Object> addressMap : Address) {
			designerCity = (String) addressMap.get("city");
		     designerState = (String) addressMap.get("state");
		    String country = (String) addressMap.get("country");
		    String zip = (String) addressMap.get("zip");

		    System.out.println("City: " + designerCity);
		    System.out.println("State: " + designerState);
		    System.out.println("Country: " + country);
		    System.out.println("Zip: " + zip);
		}*/
		System.out.println(scheduledAddress.getAddress_id()+"----------");
		return this.dateServices.getMyScheduledAddress(scheduledAddress.getUserid(), scheduledAddress.getAddress(),
		scheduledAddress.getAddress1(), scheduledAddress.getCity(), scheduledAddress.getState(), scheduledAddress.getCountry(),
		scheduledAddress.getZip(), scheduledAddress.getStatus(), result, result, result, scheduledAddress.getAddress_id(), 
		scheduledAddress.getDate_and_time(), scheduledAddress.getScheduled_id(), designerCity, designerState );
	}  
	
	//is used to get the all scheduled addresses in the PDF format for the designers 
	@RequestMapping(value = "/downloadMyScheduledAddresses", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> downloadMyScheduledAddresses(@RequestBody Check checkout) throws MalformedURLException, IOException {
	    ScheduledAddress scheduledAddress = new ScheduledAddress();
	    String token = checkout.getToken();
	    String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
	    String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
	    int userid = Integer.parseInt(result);
	    scheduledAddress.setUserid(userid);
	    String designerCity = null;
		String designerState = null;
		List<ScheduledAddress> schedules = this.dateServices.getMyScheduledAddressDownload(scheduledAddress.getUserid(), scheduledAddress.getAddress(),
	    		scheduledAddress.getAddress1(), scheduledAddress.getCity(), scheduledAddress.getState(), scheduledAddress.getCountry(),
	    		scheduledAddress.getZip(), scheduledAddress.getStatus(), result, result, result, scheduledAddress.getAddress_id(), 
	    		scheduledAddress.getDate_and_time(), scheduledAddress.getScheduled_id(), scheduledAddress.getMeasurement_id(), designerCity, designerState );
	    // Create a PDF document
	    Document document = new Document();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    try {
	        PdfWriter.getInstance(document, baos);
	        document.open();

	        Image image = Image.getInstance("http://localhost:8080/AIMORCProject/images/aimorc_logo1.jpg");
            image.setAlignment(Image.ALIGN_CENTER);
            image.scaleToFit(100, 100);
            document.add(image);
	     
	        PdfPTable table = new PdfPTable(1); // 1 column
	        table.setWidthPercentage(100); // Table width 100% of page widt
	        float cellHeight = 20;
	        for (ScheduledAddress schedule : schedules) { 
	        	PdfPCell cell = new PdfPCell();

	            // Add borders to the cell
	            cell.setBorderWidth(1);
	            cell.setPadding(5);

	            cell.addElement(new Paragraph("Name : " + schedule.getFirstname() + " " + schedule.getLastname()));
	            cell.addElement(new Paragraph("Phone Number : " + schedule.getPhonenum()));
	            cell.addElement(new Paragraph("Date and Time : " + schedule.getDate_and_time()));
	            cell.addElement(new Paragraph("Address : " + schedule.getAddress() + ", " + schedule.getAddress1() + ", " + schedule.getCity() + ", " + schedule.getState() + ", " + schedule.getCountry() + " - " + schedule.getZip()));
	            cell.addElement(new Paragraph("Status : " + schedule.getStatus()));

	            table.addCell(cell);
	            
	         // Add an empty cell for spacing
	            PdfPCell spaceCell = new PdfPCell();
	            spaceCell.setFixedHeight(cellHeight);
	            spaceCell.setBorderWidth(0);
	            table.addCell(spaceCell);
	        }

	        document.add(table);
	        document.close();
	    } catch (DocumentException e) {
	        e.printStackTrace();
	    }

	    // Convert ByteArrayOutputStream to a byte array
	    byte[] pdfContents = baos.toByteArray();

	    // Set the content disposition header for attachment
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "mySchedules.pdf");

	    // Return the PDF as a ResponseEntity
	    return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
	} 
	
	//is used to get the all completed schedules
	@RequestMapping(value = "/getMyCompletedSchedules", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  List<Map<ScheduledAddress, Object>> getMyCompletedSchedules(@RequestBody Check checkout){
		ScheduledAddress scheduledAddress = new ScheduledAddress();
		String token = checkout.getToken();
		System.out.println("2                 " +token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println("<--My Completed Schedules-->"+userid);
		System.out.println(scheduledAddress.getUserid());
		List<Map<String, Object>> Address=this.dateServices.getMyAddress(userid);
		String designerCity = null;
		String designerState = null;
		for (Map<String, Object> addressMap : Address) {
			designerCity = (String) addressMap.get("city");
		     designerState = (String) addressMap.get("state");
		    String country = (String) addressMap.get("country");
		    String zip = (String) addressMap.get("zip");

		    System.out.println("City: " + designerCity);
		    System.out.println("State: " + designerState);
		    System.out.println("Country: " + country);
		    System.out.println("Zip: " + zip);
		}
		System.out.println(scheduledAddress.getAddress_id()+"----------");
		return this.dateServices.getMyCompletedSchedules(scheduledAddress.getUserid(), scheduledAddress.getAddress(),
		scheduledAddress.getAddress1(), scheduledAddress.getCity(), scheduledAddress.getState(), scheduledAddress.getCountry(),
		scheduledAddress.getZip(), scheduledAddress.getStatus(), result, result, result, scheduledAddress.getAddress_id(), 
		scheduledAddress.getDate_and_time(), scheduledAddress.getScheduled_id(), designerCity, designerState );
	}  
	
	
	//is used to get the address details of scheduled address based on scheduled id
	@RequestMapping(value = "/getSingleScheduledAddressInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<Map<ScheduledAddress, Object>> getSingleScheduledAddressInfo(@RequestBody Check checkout) {
	    ScheduledAddress scheduledAddress = new ScheduledAddress();
	    String token = checkout.getToken();
	    System.out.println("2 " + token);
	    String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
	    String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
	    int userid = Integer.parseInt(result);
	    scheduledAddress.setUserid(userid);
	    scheduledAddress.setScheduled_id(checkout.getScheduled_id()); // Add this line to set the scheduledId
	    System.out.println("--->"+checkout.getScheduled_id());
//	    return this.dateServices.getSingleScheduledAddressInfo(
//	        scheduledAddress.getUserid(), scheduledAddress.getAddress(), scheduledAddress.getAddress1(),
//	        scheduledAddress.getCity(), scheduledAddress.getState(), scheduledAddress.getCountry(),
//	        scheduledAddress.getZip(), scheduledAddress.getStatus(), result, result, result,
//	        scheduledAddress.getAddress_id(), scheduledAddress.getDate_and_time(),
//	        scheduledAddress.getScheduled_id()
//	    );
	    return this.dateServices.getSingleScheduledAddressInfo(
		        scheduledAddress.getScheduled_id()
		    );
	}

	// used to get the delivery address given for a schedule
	@RequestMapping(value = "/getDeliveryAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  List<Map<ScheduledAddress, Object>> getDeliveryAddress(@RequestBody Check checkout){
		ScheduledAddress scheduledAddress = new ScheduledAddress();
		DeliveryAddress deliveryaddress = new DeliveryAddress();
		String token = checkout.getToken();
		System.out.println("2                 " +token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println(scheduledAddress.getState());
		return this.dateServices.getDeliveryAddress(scheduledAddress.getUserid(),scheduledAddress.getAddress(),scheduledAddress.getAddress1(), scheduledAddress.getCity(), scheduledAddress.getState(), scheduledAddress.getCountry(), scheduledAddress.getZip(), scheduledAddress.getAddress_id(), deliveryaddress.getDel_id() );
	}  
	
	
	
	//used to add the delivery address given for a schedule.
	@RequestMapping(value = "/setSchedulerAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void schedulingPickUpAddr(@RequestBody Check checkout){
		System.out.println("here");   
		Check check =new Check();
		   String token = checkout.getToken();
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
			String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int userid = Integer.parseInt(result);
			Integer datefromdb = dateRepo.findByUserid(userid, checkout.getZip());
			Integer numberOfAddress = dateRepo.fetchNumberOfAddress(userid);
			System.out.println("schedulingPickUpAddr "+checkout.getDel_address1());
			System.out.println(numberOfAddress);
			if(numberOfAddress<5) {
				dateServices.updatePickUpAddr(checkout.getCity(),checkout.getCountry(),checkout.getDel_address1(),checkout.getState(),checkout.getZip(), userid);	
			}else {
				System.out.println("User can't save more than 5 address");
			}
			//dateServices.updatePickUpAddr(checkout.getCity(),checkout.getCountry(),checkout.getDel_address1(),checkout.getState(),checkout.getZip(), userid);
	}
	
	//used to get scheduler address of the user
	@RequestMapping(value = "/getSchedulerAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void getschedulingPickUpAddr(@RequestBody Check checkout){
		   Check check =new Check();
		   String token = checkout.getToken();
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
			String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int userid = Integer.parseInt(result);
			Integer datefromdb = dateRepo.findByUserid(userid, checkout.getDel_address1());
			System.out.println("getschedulingPickUpAddr"+checkout.getZip());
			dateServices.updatePickUpAddr(checkout.getCity(),checkout.getCountry(),checkout.getDel_address1(),checkout.getState(),checkout.getZip(), userid);
	}

	//sused to add the delivery address given for a schedule.
	@RequestMapping(value = "/setScheduler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void schedulingPickUp(@RequestBody Check checkout) {
		
		 Check check =new Check();
		
		String token = checkout.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		//System.out.println("getting error");
		Integer datefromdb = dateRepo.findByUserid(userid, checkout.getZip());
		
		if (datefromdb == null) {
			
			System.out.println("date from db null "+dateServices.schedulingPickUp(checkout, userid));
			//dateServices.schedulingPickUp(checkout, userid);

			
		} else {
			//System.out.println("date from db not null "+dateServices.updatePickUp(checkout.getDate(),checkout.getDel_address1()), userid));
			//dateServices.updatePickUp(checkout.getDate(),checkout.getDel_address1()	, userid);
		}

	}
	
	//used to set the pickup address given for a created schedule
	@RequestMapping(value = "/setPickUpScheduler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void setPickUpScheduler(@RequestBody ScheduledAddress scheduledAddress) {
		
		String token = scheduledAddress.getToken();
		System.out.println("token   " + token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		System.out.println("tokenUsername           "+ tokenUsername);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		System.out.println("result      " + result);
		int userid = Integer.parseInt(result);
		System.out.println("userid       " + userid);
		System.out.println("getting error");
		System.out.println(scheduledAddress);
		dateServices.schedulingPickUpAddress(scheduledAddress, userid);

	}
	
	//used to edit the pickup address given for a created schedule 
	@RequestMapping(value = "/editScheduler", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void editPickUpScheduler(@RequestBody ScheduledAddress scheduledAddress) {
		
		String token = scheduledAddress.getToken();
		System.out.println("token   " + token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		System.out.println("tokenUsername           "+ tokenUsername);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		System.out.println("result      " + result);
		int userid = Integer.parseInt(result);
		System.out.println("userid       " + userid);
		System.out.println("getting error");
		System.out.println(scheduledAddress);
		dateServices.editscheduledPickUpAddress(scheduledAddress, userid);

	}
	
	//used to edit the devlivery address
	@RequestMapping(value = "/editDeliveryAddress", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void editDeliveryAddress(@RequestBody ScheduledAddress scheduledAddress) {
		
		String token = scheduledAddress.getToken();
		System.out.println("token   " + token);
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		System.out.println("tokenUsername           "+ tokenUsername);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		System.out.println("result      " + result);
		int userid = Integer.parseInt(result);
		System.out.println("userid       " + userid);
		System.out.println("getting error");
		System.out.println(scheduledAddress);
		dateServices.editDeliveryAddress(scheduledAddress, userid);

	}

	//used to cancel the pickup address given for a created schedule
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
	
	//used to delete the pickup address given for a created schedule
	@RequestMapping(value = "/deleteScheduler", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> deleteScheduledPickUp(@RequestBody ScheduledAddress scheduledAddress) {
		//ScheduledAddress scheduledAddress = new ScheduledAddress();
		String token = scheduledAddress.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println("scheduledAddress-------"+scheduledAddress);
		System.out.println("cancel scheduled adress "+this.dateServices.deleteScheduledPickUp(scheduledAddress));
		return this.dateServices.deleteScheduledPickUp(scheduledAddress);
	}
	
	//used to check the address based on the address id
	@RequestMapping(value = "/checkAddressId", method = RequestMethod.POST, produces = "application/json")
	public void checkAddressId(@RequestBody ScheduledAddress scheduledAddress, HttpServletRequest request, HttpServletResponse response)
			throws  ServletException, IOException{
		//ScheduledAddress scheduledAddress = new ScheduledAddress();
		String token = scheduledAddress.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println(userid);
		System.out.println(scheduledAddress.getAddress_id());
		if(dateServices.checkAddress(userid, scheduledAddress.getAddress_id() )) {
			response.setStatus(200);
		}else {
			response.sendError(401, "Invalid Authenication");
		}
	}
	
	//used to delete the address based on the address id
	@RequestMapping(value = "/deleteAddressId", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> deleteAddressId(@RequestBody ScheduledAddress scheduledAddress) {
		//ScheduledAddress scheduledAddress = new ScheduledAddress();
		String token = scheduledAddress.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println("scheduledAddress-------"+scheduledAddress);
		System.out.println("cancel scheduled adress "+this.dateServices.deleteScheduledAddress(scheduledAddress));
		return this.dateServices.deleteScheduledAddress(scheduledAddress);
	}
	

	//used to cancel the schedule
	@RequestMapping(value = "/cancelScheduledAddress", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> cancelScheduledPickUp(@RequestBody ScheduledAddress scheduledAddress) {
		//ScheduledAddress scheduledAddress = new ScheduledAddress();
		String token = scheduledAddress.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int userid = Integer.parseInt(result);
		scheduledAddress.setUserid(userid);
		System.out.println("scheduledAddress-------"+scheduledAddress);
		//System.out.println("cancel scheduled adress "+this.dateServices.cancelScheduledPickUp(scheduledAddress));
		return this.dateServices.cancelScheduledPickUp(scheduledAddress);
	}

	//used to save the measurements of the user
	@RequestMapping(value = "/saveMeasurementDetails", method = RequestMethod.POST, produces = "application/json")
	  @ResponseBody
	  public int   saveMeasurementDetails(@RequestBody Check checkout) { //Map<String, Object>{
		String token = checkout.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int designer_userid = Integer.parseInt(result);
		//System.out.println("<--->"+designer_userid);
		//checkout.setUserid(userid);
		checkout.setDesigner_userid(designer_userid);
		this.dateServices.updateScheduleStatus(checkout.getScheduled_id(), checkout.getStatus(), checkout.getUserid(), checkout.getDesigner_userid());
		 return this.dateServices.saveMeasurementDetails(checkout.getUserid(), checkout.getGender(), checkout.getFabricCollected(), checkout.getDressing_category(), checkout.getMeasurement_unit(), checkout.getShirts_collar_size(), checkout.getShirts_sleeve_length(), checkout.getShirts_size(), checkout.getT_shirt_size(), checkout.getPant_inseam_length(), checkout.getPant_size(), checkout.getPant_waist_size(), checkout.getBlouse_bust_size(), checkout.getBlouse_length(), checkout.getBlouse_neck_size(), checkout.getBlouse_shoulder_size(), checkout.getBlouse_sleeve_length(), checkout.getBlouse_waist_size(), checkout.getTops_bust_size(), checkout.getTops_shoulder_size(), checkout.getTops_sleeve_length(), checkout.getTops_waist_size() );
	  }
	
	//used to update the measurement details of user
	@RequestMapping(value = "/updateMeasurementDetails", method = RequestMethod.POST, produces = "application/json")
	  @ResponseBody
	  public int   updateMeasurementDetails(@RequestBody Check checkout) { //Map<String, Object>{
		String token = checkout.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int designer_userid = Integer.parseInt(result);
		System.out.println("<--->"+designer_userid);
		//checkout.setUserid(userid);
		checkout.setDesigner_userid(designer_userid);
		//System.out.println(checkout.getFabricCollected());
		
		this.dateServices.updateScheduleStatus(checkout.getScheduled_id(), checkout.getStatus(), checkout.getUserid(), checkout.getDesigner_userid());
		return this.dateServices.updateMeasurementDetails(checkout.getMeasurement_id(), checkout.getGender(), checkout.getFabricCollected(), checkout.getDressing_category(), checkout.getMeasurement_unit(), checkout.getShirts_collar_size(), checkout.getShirts_sleeve_length(), checkout.getShirts_size(), checkout.getT_shirt_size(), checkout.getPant_inseam_length(), checkout.getPant_size(), checkout.getPant_waist_size(), checkout.getBlouse_bust_size(), checkout.getBlouse_length(), checkout.getBlouse_neck_size(), checkout.getBlouse_shoulder_size(), checkout.getBlouse_sleeve_length(), checkout.getBlouse_waist_size(), checkout.getTops_bust_size(), checkout.getTops_shoulder_size(), checkout.getTops_sleeve_length(), checkout.getTops_waist_size() );
	  }
	
	//is used to accept schdule the by designer
	@RequestMapping(value = "/acceptSchedule", method = RequestMethod.POST, produces = "application/json") //updateDesignerId
	  @ResponseBody
	  public int   acceptSchedule(@RequestBody Check checkout) { //Map<String, Object>{
		String token = checkout.getToken();
		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
		int designer_userid = Integer.parseInt(result);
		System.out.println("<--here-->"+designer_userid);
		//checkout.setUserid(userid);
		checkout.setDesigner_userid(designer_userid);
		//System.out.println(checkout.getFabricCollected());
		
		return this.dateServices.assignDesignerIdForSchedule(checkout.getScheduled_id(), checkout.getDesigner_userid());
		//return this.dateServices.updateMeasurementDetails(checkout.getMeasurement_id(), checkout.getGender(), checkout.getFabricCollected(), checkout.getDressing_category(), checkout.getMeasurement_unit(), checkout.getShirts_collar_size(), checkout.getShirts_sleeve_length(), checkout.getShirts_size(), checkout.getT_shirt_size(), checkout.getPant_inseam_length(), checkout.getPant_size(), checkout.getPant_waist_size(), checkout.getBlouse_bust_size(), checkout.getBlouse_length(), checkout.getBlouse_neck_size(), checkout.getBlouse_shoulder_size(), checkout.getBlouse_sleeve_length(), checkout.getBlouse_waist_size(), checkout.getTops_bust_size(), checkout.getTops_shoulder_size(), checkout.getTops_sleeve_length(), checkout.getTops_waist_size() );
	  }
	
	//is used for the user to select the prefere measurement details while scheduling
	@RequestMapping(value = "/getUserPrefernceMeasurementDetails", method = RequestMethod.POST, produces = "application/json")
	  @ResponseBody
	  public List<Map<String, Object>>   getUserPrefernceMeasurementDetails(@RequestBody Check checkout) { 
		 return this.dateServices.getUserPrefernceMeasurementDetails(checkout.getMeasurement_id());
	}
	
	//is used to delete the measurement details of user
	@RequestMapping(value = "/deleteMeasurementDetails", method = RequestMethod.POST, produces = "application/json")
	  @ResponseBody
	  public int   deleteMeasurementDetails(@RequestBody Check checkout) { //Map<String, Object>{
//		String token = checkout.getToken();
//		String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
//		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
//		int userid = Integer.parseInt(result);
//		checkout.setUserid(userid);
		 return this.dateServices.deleteMeasurementDetails(checkout.getMeasurement_id());
		}
	
	//to get profile address for user ,it will take userid as argument
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
	  
	  //get delivery address for user ,it will take userid as argument
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
	  
	  @RequestMapping(value = "/addDeliveryAddr", method = RequestMethod.POST, produces = "application/json")
	  @ResponseBody
	  public boolean  addDeliveryAddress(@RequestBody  Check checkout) {
		  String token = checkout.getToken();
			String tokenUsername = restTemplate.postForObject("http://localhost:8081/decodeToken", token, String.class);
   		String result = restTemplate.postForObject("http://localhost:8081/getUserId", tokenUsername, String.class);
			int userid = Integer.parseInt(result);
			Integer numberOfAddress = deliveryRepo.fetchNumberOfAddress(userid);	
			if(numberOfAddress<1) {
				dateServices.addDeliveryAddr(checkout.getAddress_id(),userid);
			}
			else {
				System.out.println("delivery address added");
			}
			
			new ResponseEntity<String>("success!", HttpStatus.ACCEPTED);
			return true;
	  }
	  
	  
	  //to update delivery  address for user ,it will take userid, delivery address as argument 
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