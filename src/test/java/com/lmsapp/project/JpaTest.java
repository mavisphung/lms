package com.lmsapp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.lmsapp.project.user.User;
import com.lmsapp.project.user.UserRepository;
import com.lmsapp.project.user.UserService;

@DataJpaTest
//Cho phep su dung database thay vi in memory
@AutoConfigureTestDatabase(replace = Replace.NONE)
//khong rollback khi test xong
@Rollback(true)
public class JpaTest {
	
	
	private UserRepository userService;
	
	@Autowired
	public JpaTest(UserRepository userService) {
		this.userService = userService;
	}
	
	@Test
	public void whenCalledGet_thenPickUpAUser() {
		String username = "student";
		User objFromDb = userService.findByUsername(username);
		System.out.println(objFromDb.toString());
		
		assertEquals(objFromDb, null);
	}
}
