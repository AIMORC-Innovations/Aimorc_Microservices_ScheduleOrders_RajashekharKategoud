package com.Checkout.Repository;

import java.util.List;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.Checkout.entity.Date;
import com.Checkout.entity.ScheduledAddress;


@Repository
@Transactional
public interface ScheduleAddressRepository extends JpaRepository<ScheduledAddress,Integer> {

	@Modifying
	@Query(value = "insert into scheduled_addresses(address_id,address,userid,date_and_time,status) values (:address_id,:address,:userid,:date_and_time,:status)",nativeQuery = true)
	public int saveScheduledPickUpAddress( @Param("date_and_time")String date_and_time, @Param("address_id") int address_id,@Param("address") String address, @Param("status") String status,  @Param("userid")int userid);

	
	@Modifying
	@Query(value = "update scheduled_addresses set date_and_time=:date_and_time, address_id=:address_id where userid=:userid and scheduled_id=:scheduled_id",nativeQuery = true)
	public int editScheduledPickUpAddress( @Param("date_and_time")String date_and_time, @Param("address_id") int address_id,@Param("scheduled_id") int scheduled_id,  @Param("userid")int userid);
    
	
	//@Query(value = "select * from scheduled_addresses where userid=?",nativeQuery = true) //select * from date where userid=?
	@Query(value = "select u.address, u.address1, u.city, u.state, u.country, u.zip, s.scheduled_id, s.userid, s.date_and_time, s.status from user_address u join scheduled_addresses s on u.address_id = s.address_id where s.userid=?",nativeQuery = true)
	public List<Map<ScheduledAddress, Object>> fetchDate(@Param("userid")int userid, @Param("address")String address, @Param("address1") String address1, @Param("city") String city, @Param("state") String state, @Param("country") String country, @Param("zip") String zip,     
			@Param("date_and_time")String date_and_time, @Param("address_id") int address_id ,@Param("status")String status, @Param("scheduled_id")int scheduled_id);
	
	@Modifying
    @Query(value = "delete from scheduled_addresses where scheduled_id=:scheduled_id and userid=:userid", nativeQuery = true)
    public int delete(int scheduled_id, int userid);
	
	@Modifying
    @Query(value = "update scheduled_addresses set status=:status where scheduled_id=:scheduled_id and userid=:userid", nativeQuery = true)
    public int cancel(int scheduled_id, String status, int userid);

}
