package com.lmsapp.project.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.role.ERole;
import com.lmsapp.project.role.Role;
import com.lmsapp.project.role.RoleRepository;

@Service
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
		User clientUser = registration.getUser();
		//check xem có chứa dấu cách không
		if (clientUser.getUsername().contains(" ")) {
			throw new UserAlreadyExistException("Username can not have space character: " + clientUser.getUsername());
		}
		
		//check trùng username
		if (usernameExists(clientUser.getUsername())) {
			throw new UserAlreadyExistException("There is an account with that username: " + clientUser.getUsername());
		}
		
		//check 2 pass giống nhau
		if (!clientUser.getPassword().equals(registration.getConfirmPassword())) {
			throw new UserAlreadyExistException("The password is not matched.");
		}
		
		
		String encodedPassword = encoder.encode(clientUser.getPassword());
		clientUser.setPassword(encodedPassword);
		clientUser.setEnabled(true);
		Role userRole = roleRepo.findByName(convertToERole(registration.getRole()));
		clientUser.getRoles().add(userRole);
		
		return repo.save(clientUser);
	}
	
	private ERole convertToERole(String rolename) {
		ERole eRole = null;
		switch (rolename) {
			case "ROLE_ADMIN":
				eRole = ERole.ROLE_ADMIN;
				break;
			case "ROLE_INSTRUCTOR":
				eRole = ERole.ROLE_INSTRUCTOR;
				break;
			default:
				eRole = ERole.ROLE_STUDENT;
				break;
		}
		return eRole;
	}
	
	private boolean usernameExists(String username) {
		return repo.findByUsername(username) != null;
	}

	@Override
	public User findByUsername(String username) {
		return repo.findByUsername(username);
	}

	
	@Override
	public List<User> findAll() {
		return repo.findAll();
	}

	@Override
	public User save(User user) {
		return repo.save(user);
	}

	@Override
	public void remove(Long id) {
		repo.deleteById(id);
	}

	@Override
	public void remove(String username) throws RuntimeException {
		User delete = repo.findByUsername(username);
		if (delete == null) {
			throw new RuntimeException("Invalid username");
		}
		repo.delete(delete);
	}
	
	
	
}
