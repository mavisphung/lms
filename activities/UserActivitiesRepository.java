package com.lmsapp.project.activities;



import org.springframework.data.repository.CrudRepository;

import com.lmsapp.project.activities.UserActivities;

public interface UserActivitiesRepository extends CrudRepository<UserActivities, Integer> {
//	@Query("from users_activities e where not(e.end < :from or e.start > :to)")
//	public List<UserActivitiesRepository> findBetween(@Param("from") @DateTimeFormat(iso=ISO.DATE_TIME) LocalDateTime start, @Param("to") @DateTimeFormat(iso=ISO.DATE_TIME) LocalDateTime end);
}
