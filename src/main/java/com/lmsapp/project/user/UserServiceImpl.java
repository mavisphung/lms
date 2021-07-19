package com.lmsapp.project.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.role.ERole;
import com.lmsapp.project.role.Role;
import com.lmsapp.project.role.RoleRepository;

@Service()
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public User registerNewUser(UserRegistration registration) throws UserAlreadyExistException {
		
		//check trùng username
		if (usernameExists(registration.getUser().getUsername())) {
			throw new UserAlreadyExistException("There is an account with that username: " + registration.getUser().getUsername());
		}
		
		//check 2 pass giống nhau
		if (!registration.getUser().getPassword().equals(registration.getConfirmPassword())) {
			throw new UserAlreadyExistException("The password is not matched.");
		}
		
		User clientUser = registration.getUser();


		String encodedPassword = encoder.encode(clientUser.getPassword());


		clientUser.setPassword(encodedPassword);
		clientUser.setEnabled(true);
		Role roleStudent = roleRepo.findByName(ERole.ROLE_STUDENT);
		clientUser.getRoles().add(roleStudent);
		
		return repo.save(clientUser);
	}
	
	private boolean usernameExists(String username) {
		return repo.findByUsername(username) != null;
	}

	@Override
	public User findByUsername(String username) {
		return repo.findByUsername(username);
	}
	
	
}
