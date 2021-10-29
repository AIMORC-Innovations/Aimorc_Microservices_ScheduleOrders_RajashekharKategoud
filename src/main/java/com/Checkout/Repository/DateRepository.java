package com.Checkout.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.Checkout.entity.Date;


@Repository
@Transactional
public interface DateRepository extends JpaRepository<Date,Integer> {

	@Query(value = "select userid from date where userid=?",nativeQuery = true)
	public Integer findByUserid(@Param("userid")int userid);
	
	@Query(value = "select del_address1 from date where userid=?",nativeQuery = true)
	public Integer findByAddress1(@Param("userid")int userid);
	
	/*
	 * @Query(value = "select del_address2 from date where userid=?",nativeQuery =
	 * true) public Integer findByAddress2(@Param("userid")int userid);
	 * 
	 * @Query(value = "select del_address3 from date where userid=?",nativeQuery =
	 * true) public Integer findByAddress3(@Param("userid")int userid);
	 */
	
	@Query(value = "select * from date where userid=?",nativeQuery = true)
	public Date fetchDate(@Param("userid")int userid);
	
	@Query(value = "insert into date(country,state,city,del_address1,del_address2,del_address3,zip,date,userid) values (:userid,:date,:country,:state,:city,:del_address1,:del_address2,:del_address3,:zip,)",nativeQuery = true)
	public Date save(@Param("userid")int userid, @Param("date")String date);

    @Modifying
	@Query(value = "UPDATE date set date=:date, del_address1=:del_address1  where userid=:userid", nativeQuery = true)
	public int updateDate(@Param("date")String date, @Param("del_address1") String del_address1 ,@Param("userid")int userid);
    
    @Modifying
    @Query(value = "delete from date where userid=:userid", nativeQuery = true)
    public int delete(int userid);

//    @Query(value = "insert into date(country,state,city,del_address1,zip,userid) values (:userid,:country,:state,:city,:del_address1,:zip,)",nativeQuery = true)
//	public Date saveAddress(@Param("userid")int userid, @Param("country")String country, @Param("city")String city, @Param("state")String state, @Param("del_address1")String del_address1);
//    
    @Modifying
    @Query(value = "UPDATE date set del_address1=:del_address1 ,city=:city,country=:country,state=:state,zip=:zip where userid=:userid",nativeQuery = true)
	public int saveAddress(@Param("del_address1") String del_address1,@Param("country") String country,@Param("city") String city,@Param("state") String state, @Param("zip")String zip, @Param("userid")int userid);


	


	
}
