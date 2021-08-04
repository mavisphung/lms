package com.lmsapp.project.activities;



import java.time.LocalDateTime;
import java.util.List;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Repository;




@Repository
public interface UserActivitiesRepository extends CrudRepository<UserActivities, Integer> {
	@Query("from UserActivities e where not(e.end < :from or e.start > :to)")
	public List<UserActivities> findBetween(@Param("from") @DateTimeFormat(iso=ISO.DATE_TIME) LocalDateTime start, @Param("to") @DateTimeFormat(iso=ISO.DATE_TIME) LocalDateTime end);

	@Query("SELECT coalesce(max(e.id), 0) FROM UserActivities e")
    int getMaxId();
	
	@Query("from UserActivities e where not(e.end < :from or e.start > :to) and e.username = :username")
	public List<UserActivities> findBetweenByUsername(@Param("from") @DateTimeFormat(iso=ISO.DATE_TIME) LocalDateTime start,
			@Param("to") @DateTimeFormat(iso=ISO.DATE_TIME) LocalDateTime end,@Param("username") String username);
	
	@Query("from UserActivities e where e.username like :username")
	public List<UserActivities> findActivitiesByname(@Param("username") String name);
	
}
