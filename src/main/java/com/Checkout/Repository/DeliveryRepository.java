package com.Checkout.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Checkout.entity.Check;
import com.Checkout.entity.Date;
import com.Checkout.entity.DeliveryAddress;
@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryAddress,Integer> {

	
	@Query(value = "select userid from deliveryAddress where userid=?",nativeQuery = true)
	public Integer findByUserid(@Param("userid")int userid);
	
	@Query(value = "select r.address,  d.del_address1, d.del_address2 from registration r join deliveryAddress d on r.userid = d.userid where d.userid= ?",nativeQuery = true)
	public Map<String, Object> getAddress(@Param("userid")int userid);
	
	@Query(value = "select del_address1  from deliveryAddress where userid=?",nativeQuery = true)
	public String   getAddress1(@Param("userid")int userid);
	
	@Query(value = "select del_address2 from deliveryAddress where userid=?",nativeQuery = true)
	public String   getAddress2(@Param("userid")int userid);
	
	@Query(value = "select r.address from registration r join deliveryaddress d on d.userid= r.userid where d.userid=:userid",nativeQuery = true)
	public List<Object> ProfileAddress(@Param("userid")int userid);
	
	@Modifying
	@Query(value = "UPDATE deliveryAddress set del_address1=:del_address1 where userid=:userid", nativeQuery = true)
	public void save( @Param("del_address1") String del_address1,
			 @Param("userid") int userid);
	
	@Modifying
	@Query(value = "UPDATE deliveryAddress set del_address1=:del_address1 ,  del_address2=:del_address2 where userid=:userid", nativeQuery = true)
	public int updateAddress3(  @Param("del_address1") String del_address1,@Param("del_address2") String del_address2,
			 @Param("userid") int userid);
	
	@Modifying
	@Query(value = "UPDATE deliveryAddress set del_address2 =:del_address1 where userid=:userid", nativeQuery = true)
	public int updateAddress2( @Param("del_address1") String del_address1,
			 @Param("userid") int userid);
	
	@Modifying
	@Query(value = "UPDATE deliveryAddress set del_address1=:del_address1 where userid=:userid", nativeQuery = true)
	public int updateAddress1( @Param("del_address1") String del_address1,
			 @Param("userid") int userid);
	
	
	/*
	 * @Query(value =
	 * "insert into deliveryaddress(del_profileAddr,userid) values (:del_profile,:userid)"
	 * ,nativeQuery = true) public int save(@Param("delAddress") String
	 * delAddress,@Param("userid")int userid);
	 */
}
