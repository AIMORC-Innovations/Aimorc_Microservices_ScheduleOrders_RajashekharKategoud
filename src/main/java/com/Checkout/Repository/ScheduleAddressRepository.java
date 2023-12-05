package com.Checkout.Repository;

import java.util.List;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.Checkout.entity.Check;
import com.Checkout.entity.Date;
import com.Checkout.entity.ScheduledAddress;


@Repository
@Transactional
public interface ScheduleAddressRepository extends JpaRepository<ScheduledAddress, Integer> {

	@Modifying
	@Query(value = "insert into scheduled_addresses(address_id,userid,date_and_time,measurement_id,status) values (:address_id,:userid,:date_and_time,:measurement_id,:status)",nativeQuery = true)//address,:address,
	public int saveScheduledPickUpAddress( @Param("date_and_time")String date_and_time, @Param("address_id") int address_id, @Param("status") String status, @Param("measurement_id") Integer measurement_id,  @Param("userid")int userid);//@Param("address") String address,

	@Query(value = "select * from scheduled_addresses where  userid=? and address_id=?", nativeQuery = true)
	public ScheduledAddress findByUserId(@Param("userid") int userid, @Param("address_id") int address_id);
	
	@Modifying
	@Query(value = "update scheduled_addresses set date_and_time=:date_and_time, address_id=:address_id,measurement_id=:measurement_id  where userid=:userid and scheduled_id=:scheduled_id",nativeQuery = true)
	public int editScheduledPickUpAddress( @Param("date_and_time")String date_and_time, @Param("address_id") int address_id,@Param("scheduled_id") int scheduled_id, @Param("measurement_id") Integer measurement_id, @Param("userid")int userid);
    
	
	//@Query(value = "select * from scheduled_addresses where userid=?",nativeQuery = true) //select * from date where userid=?
	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status from user_address u join scheduled_addresses s on u.address_id = s.address_id where s.userid=?",nativeQuery = true)
	public List<Map<ScheduledAddress, Object>> fetchDate(@Param("userid")int userid, @Param("address")String address, @Param("address1") String address1, @Param("city") String city, @Param("state") String state, @Param("country") String country, @Param("zip") String zip,     
			@Param("date_and_time")String date_and_time, @Param("address_id") int address_id ,@Param("status")String status, @Param("scheduled_id")int scheduled_id);
	
	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status, r.firstname, r.lastname, r.phonenum from user_address u join scheduled_addresses s on u.address_id = s.address_id join registration r on r.userid = s.userid WHERE s.designer_userid IS NULL",nativeQuery = true) //WHERE u.city = :designerCity AND u.state = :designerState //WHERE s.designer_userid IS NULL
	public List<Map<ScheduledAddress, Object>> fetchAllDate(@Param("userid")int userid, @Param("firstname")String firstname, @Param("lastname")String lastname, @Param("phonenum")String phonenum, @Param("address")String address, @Param("address1") String address1, @Param("city") String city, @Param("state") String state, @Param("country") String country, @Param("zip") String zip,     
			@Param("date_and_time")String date_and_time, @Param("address_id") int address_id ,@Param("status")String status, @Param("scheduled_id")int scheduled_id
			,@Param("designerCity")String designerCity, @Param("designerState")String designerState);
	
	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status, r.firstname, r.lastname, r.phonenum from user_address u join scheduled_addresses s on u.address_id = s.address_id join registration r on r.userid = s.userid WHERE s.designer_userid=:userid AND s.status!='Completed'",nativeQuery = true) //WHERE u.city = :designerCity AND u.state = :designerState //WHERE s.designer_userid IS NULL
//	public List<Map<ScheduledAddress, Object>> fetchMySchedules(@Param("userid")int userid, @Param("firstname")String firstname, @Param("lastname")String lastname, @Param("phonenum")String phonenum, @Param("address")String address, @Param("address1") String address1, @Param("city") String city, @Param("state") String state, @Param("country") String country, @Param("zip") String zip,     
//			@Param("date_and_time")String date_and_time, @Param("address_id") int address_id ,@Param("status")String status, @Param("scheduled_id")int scheduled_id
//			,@Param("designerCity")String designerCity, @Param("designerState")String designerState);
	public List<Map<ScheduledAddress, Object>> fetchMySchedules(@Param("userid") int userid);
	
	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status, s.measurement_id, s.token, r.firstname, r.lastname, r.phonenum from user_address u join scheduled_addresses s on u.address_id = s.address_id join registration r on r.userid = s.userid WHERE s.designer_userid=:userid AND s.status!='Completed'",nativeQuery = true) //WHERE u.city = :designerCity AND u.state = :designerState //WHERE s.designer_userid IS NULL
	public List<ScheduledAddress> fetchMySchedulesDownload(@Param("userid") int userid);
	
	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status, r.firstname, r.lastname, r.phonenum from user_address u join scheduled_addresses s on u.address_id = s.address_id join registration r on r.userid = s.userid WHERE s.designer_userid=:userid AND s.status='Completed'",nativeQuery = true) //WHERE u.city = :designerCity AND u.state = :designerState //WHERE s.designer_userid IS NULL
	public List<Map<ScheduledAddress, Object>> fetchMyCompletedSchedules(@Param("userid") int userid);
	
	@Query(value = "select address_id, city, state, country, zip from user_address where userid=:userid",nativeQuery = true)
	public List<Map<String, Object>> getMyAddress(@Param("userid")int userid);
//	@Query(value = "select address_id, address, address1, city, state, country, zip from user_address where userid=:userid",nativeQuery = true)
//	public List<Map<String, Object>> getMyAddress(@Param("userid")int userid);
//	
//	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip from user_address u where u.userid=:userid",nativeQuery = true)
//	public List<Map<String, Object>> getMyAddress(@Param("userid")int userid,  @Param("address")String address, @Param("address1") String address1, @Param("city") String city, @Param("state") String state, @Param("country") String country, @Param("zip") String zip,  @Param("address_id") int address_id );
//	
//	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status, r.firstname, r.lastname, r.phonenum from user_address u join scheduled_addresses s on u.address_id = s.address_id join registration r on r.userid = s.userid where s.scheduled_id=?",nativeQuery = true)
//	public List<Map<ScheduledAddress, Object>> fetchSingleDateInfo(@Param("userid")int userid, @Param("firstname")String firstname, @Param("lastname")String lastname, @Param("phonenum")String phonenum, @Param("address")String address, @Param("address1") String address1, @Param("city") String city, @Param("state") String state, @Param("country") String country, @Param("zip") String zip,     
//			@Param("date_and_time")String date_and_time, @Param("address_id") int address_id ,@Param("status")String status, @Param("scheduled_id")int scheduled_id);
	
	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status, m.gender, m.height, m.chest, m.waist, m.hip, m.inseam, m.measurement, r.firstname, r.lastname, r.phonenum from user_address u join scheduled_addresses s on u.address_id = s.address_id join registration r on r.userid = s.userid join  user_measurement_details m ON m.scheduled_id = s.scheduled_id where s.scheduled_id=?", nativeQuery = true)
	public List<Map<ScheduledAddress, Object>> fetchSingleDateInfo(@Param("scheduled_id") int scheduledId);
	
	@Query(value = "select u.address_id, u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status, s.measurement_id,  r.firstname, r.lastname, r.phonenum from user_address u join scheduled_addresses s on u.address_id = s.address_id join registration r on r.userid = s.userid where s.scheduled_id=?", nativeQuery = true)
	public List<Map<ScheduledAddress, Object>> fetchSingleDateInfoWithoutMeasurements(@Param("scheduled_id") int scheduledId);
	
	@Query(value="SELECT EXISTS(SELECT 1 FROM user_measurement_details WHERE scheduled_id = :scheduled_id)", nativeQuery = true)
	public boolean checkIfExists(@Param("scheduled_id") int scheduledId);
	
	@Modifying
    @Query(value = "delete from scheduled_addresses where scheduled_id=:scheduled_id and userid=:userid", nativeQuery = true)
    public int delete(int scheduled_id, int userid);
	
	
	@Query(value = "select * from user_measurement_details where measurement_id=:measurement_id", nativeQuery = true)
	public List<Map<String, Object>> getUserPrefernceMeasurementDetails(@Param("measurement_id") int measurement_id);
	
	
	
	@Modifying
    @Query(value = "delete from scheduled_addresses where address_id=:address_id and userid=:userid", nativeQuery = true)
    public int deleteAddressId(int address_id, int userid);
	
	@Modifying
    @Query(value = "update scheduled_addresses set status=:status where scheduled_id=:scheduled_id and userid=:userid", nativeQuery = true)
    public int cancel(int scheduled_id, String status, int userid);


//	@Modifying
//	@Query(value = "BEGIN insert into user_measurement_details(userid, scheduled_id, height, chest, waist, hip, inseam, gender) values (:userid, :scheduled_id, :height, :chest, :waist, :hip, :inseam, :gender); UPDATE scheduled_addresses SET status = :status WHERE scheduled_id = :scheduled_id; COMMIT; END;", nativeQuery = true)
//	public int updateMeasurementDetails(@Param("userid") int userid, @Param("scheduled_id") int scheduled_id, @Param("gender") String gender, @Param("height") int height, @Param("chest") int chest, @Param("hip") int hip, @Param("waist") int waist, @Param("inseam") int inseam, @Param("status") String status);

//	@Modifying
//	@Query(value = "BEGIN insert into user_measurement_details(userid, scheduled_id, height, chest, waist, hip, inseam, gender) values (:userid, :scheduled_id, :height, :chest, :waist, :hip, :inseam, :gender); UPDATE scheduled_addresses SET status = :status WHERE scheduled_id = :scheduled_id; COMMIT; END;", nativeQuery = true)
//	public int updateMeasurementDetails(int userid, int scheduled_id, String gender, int height, int chest, int hip,
//	        int waist, int inseam); //, String status
	/*
	@Modifying
	@Query(value = "insert into user_measurement_details(userid, scheduled_id, height, chest, waist, hip, inseam, gender, measurement) values (:userid, :scheduled_id, :height, :chest, :waist, :hip, :inseam, :gender, :measurement);", nativeQuery = true)
	public int insertMeasurementDetails(int userid, int scheduled_id, String gender, int height, int chest, int hip,
	        int waist, int inseam, String measurement); 
	*/
	@Modifying
	@Query(value = "INSERT INTO user_measurement_details(userid, gender, fabric_collected, dressing_category, measurement_unit, shirts_collar_size, shirts_sleeve_length, shirts_size, t_shirt_size, pant_inseam_length, pant_size, pant_waist_size, blouse_bust_size, blouse_length, blouse_neck_size, blouse_shoulder_size, blouse_sleeve_length, blouse_waist_size, tops_bust_size, tops_shoulder_size, tops_sleeve_length, tops_waist_size) VALUES (:userid, :gender, :fabric_collected, :dressing_category, :measurement_unit,  :shirts_collar_size, :shirts_sleeve_length, :shirts_size, :t_shirt_size, :pant_inseam_length, :pant_size, :pant_waist_size, :blouse_bust_size, :blouse_length, :blouse_neck_size, :blouse_shoulder_size, :blouse_sleeve_length, :blouse_waist_size, :tops_bust_size, :tops_shoulder_size, :tops_sleeve_length, :tops_waist_size)", nativeQuery = true)
	public int insertMeasurementDetails(int userid, String gender, boolean fabric_collected, String dressing_category, String measurement_unit,
			Integer shirts_collar_size, Integer shirts_sleeve_length, Integer shirts_size, Integer t_shirt_size,
			Integer pant_inseam_length, Integer pant_size, Integer pant_waist_size, Integer blouse_bust_size,
			Integer blouse_length, Integer blouse_neck_size, Integer blouse_shoulder_size, Integer blouse_sleeve_length,
			Integer blouse_waist_size, Integer tops_bust_size, Integer tops_shoulder_size, Integer tops_sleeve_length,
			Integer tops_waist_size);
	
	@Modifying
	@Query(value = "UPDATE user_measurement_details SET gender = :gender, fabric_collected =:fabric_collected, dressing_category = :dressing_category, measurement_unit = :measurement_unit, shirts_collar_size = :shirts_collar_size, shirts_sleeve_length = :shirts_sleeve_length, shirts_size = :shirts_size, t_shirt_size = :t_shirt_size, pant_inseam_length = :pant_inseam_length, pant_size = :pant_size, pant_waist_size = :pant_waist_size, blouse_bust_size = :blouse_bust_size, blouse_length = :blouse_length, blouse_neck_size = :blouse_neck_size, blouse_shoulder_size = :blouse_shoulder_size, blouse_sleeve_length = :blouse_sleeve_length, blouse_waist_size = :blouse_waist_size, tops_bust_size = :tops_bust_size, tops_shoulder_size = :tops_shoulder_size, tops_sleeve_length = :tops_sleeve_length, tops_waist_size = :tops_waist_size WHERE measurement_id = :measurement_id", nativeQuery = true)
	public int updateMeasurementDetails(@Param("measurement_id") int measurement_id, @Param("gender") String gender, @Param("fabric_collected") boolean fabric_collected, @Param("dressing_category") String dressing_category, @Param("measurement_unit") String measurement_unit, @Param("shirts_collar_size") Integer shirts_collar_size, @Param("shirts_sleeve_length") Integer shirts_sleeve_length, @Param("shirts_size") Integer shirts_size, @Param("t_shirt_size") Integer t_shirt_size, @Param("pant_inseam_length") Integer pant_inseam_length, @Param("pant_size") Integer pant_size, @Param("pant_waist_size") Integer pant_waist_size, @Param("blouse_bust_size") Integer blouse_bust_size, @Param("blouse_length") Integer blouse_length, @Param("blouse_neck_size") Integer blouse_neck_size, @Param("blouse_shoulder_size") Integer blouse_shoulder_size, @Param("blouse_sleeve_length") Integer blouse_sleeve_length, @Param("blouse_waist_size") Integer blouse_waist_size, @Param("tops_bust_size") Integer tops_bust_size, @Param("tops_shoulder_size") Integer tops_shoulder_size, @Param("tops_sleeve_length") Integer tops_sleeve_length, @Param("tops_waist_size") Integer tops_waist_size);

	@Modifying
	@Query(value = "delete from user_measurement_details where measurement_id=:measurement_id", nativeQuery = true)
	public int deleteMeasurementDetails(int measurement_id);
	
//	@Modifying
//	@Query(value = "INSERT INTO user_measurement_details(userid, gender,dressing_category, measurement_unit, shirts_collar_size, shirts_sleeve_length, shirts_size, t_shirt_size, pant_inseam_length, pant_size, pant_waist_size, blouse_bust_size, blouse_length, blouse_neck_size, blouse_shoulder_size, blouse_sleeve_length, blouse_waist_size, tops_bust_size, tops_shoulder_size, tops_sleeve_length, tops_waist_size) VALUES (:userid, :gender, :dressing_category, :measurement_unit,  :shirts_collar_size, :shirts_sleeve_length, :shirts_size, :t_shirt_size, :pant_inseam_length, :pant_size, :pant_waist_size, :blouse_bust_size, :blouse_length, :blouse_neck_size, :blouse_shoulder_size, :blouse_sleeve_length, :blouse_waist_size, :tops_bust_size, :tops_shoulder_size, :tops_sleeve_length, :tops_waist_size)", nativeQuery = true)
//	public int updateMeasurementDetails(int measurement_id, String gender, String dressing_category, String measurement_unit,
//			Integer shirts_collar_size, Integer shirts_sleeve_length, Integer shirts_size, Integer t_shirt_size,
//			Integer pant_inseam_length, Integer pant_size, Integer pant_waist_size, Integer blouse_bust_size,
//			Integer blouse_length, Integer blouse_neck_size, Integer blouse_shoulder_size, Integer blouse_sleeve_length,
//			Integer blouse_waist_size, Integer tops_bust_size, Integer tops_shoulder_size, Integer tops_sleeve_length,
//			Integer tops_waist_size);
	
	@Modifying
	@Query(value = "update user_measurement_details set gender=:gender, height=:height, chest=:chest, hip=:hip,waist=:waist,inseam=:inseam, measurement=:measurement  where scheduled_id=:scheduled_id and userid=:userid", nativeQuery = true)
	public int updateMeasurementDetails(int userid, int scheduled_id, String gender, int height, int chest, int hip,
	        int waist, int inseam, String measurement); 
	
	@Query(value="SELECT EXISTS(SELECT 1 FROM user_measurement_details WHERE scheduled_id = :scheduled_id and userid=:userid)", nativeQuery = true)
	public boolean checkIfMeasurementsExists(@Param("scheduled_id") int scheduledId, @Param("userid") int userid);
	
//	
//	@Modifying
//	@Query(value = "BEGIN insert into user_measurement_details(userid, scheduled_id, height, chest, waist, hip, inseam, gender) values (:userid, :scheduled_id, :height, :chest, :waist, :hip, :inseam, :gender); UPDATE scheduled_addresses SET status = :status WHERE scheduled_id = :scheduled_id; COMMIT; END;", nativeQuery = true)
//	public int updateMeasurementDetails(String userid, int scheduled_id, String gender, int height, int chest, int hip,
//			int waist, int inseam, String status);
//	}
//@Query(value = "update scheduled_addresses set status=:status, gender=:gender, height=:height, chest=:chest, hip=:hip,waist=:waist,inseam=:inseam  where scheduled_id=:scheduled_id", nativeQuery = true)

	@Modifying
	@Query(value = "update scheduled_addresses set status=:status where scheduled_id=:scheduled_id",nativeQuery = true)
	public void updateMeasurementDetailsStatus(String status, int scheduled_id);
	
	
	@Query(value = "select umd.height, umd.chest, umd.hip, umd.waist, umd.inseam, umd.gender FROM user_measurement_details umd JOIN scheduled_addresses sa ON umd.scheduled_id = sa.scheduled_id WHERE sa.userid = ?",nativeQuery = true)
	public List<Map<ScheduledAddress, Object>> getMyMeasurements(int userid);
	
	@Query(value = "select * FROM user_measurement_details WHERE userid = ?",nativeQuery = true)
	public List<Map<ScheduledAddress, Object>> getMyMeasurementsForMySchedules(int userid);
	/*
	@Query(value = "select umd.* FROM user_measurement_details umd JOIN scheduled_addresses sa ON umd.scheduled_id = sa.scheduled_id WHERE sa.userid = ?",nativeQuery = true)
	public List<Map<String, Object>> findMeasurementsById(int userid);*/

	@Modifying
    @Query(value = "update scheduled_addresses set status=:status, designer_userid=:designer_userid where scheduled_id=:scheduled_id and userid=:userid", nativeQuery = true)
	public int updateScheduleStatus(int scheduled_id, String status, int userid, int designer_userid);

	
	@Modifying
    @Query(value = "update scheduled_addresses set designer_userid=:designer_userid where scheduled_id=:scheduled_id", nativeQuery = true)
	public int assignDesignerIdForSchedule(int scheduled_id, int designer_userid);

	
	
	
}
