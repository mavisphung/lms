package com.lmsapp.project.user.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.lmsapp.project.entities.Answer;
import com.lmsapp.project.entities.Question;
import com.lmsapp.project.entities.Quiz;
import com.lmsapp.project.entities.UserAnswer;
import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.services.AnswerService;
import com.lmsapp.project.services.QuestionService;
import com.lmsapp.project.services.UserAnswerService;
import com.lmsapp.project.services.UserQuizService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;
import com.lmsapp.project.util.Utility;

@Controller
public class UserController {

	//@Autowired
	private final UserService userService;
	private final UserQuizService userQuizService;
	private final UserAnswerService userAnswerService;
	private final AnswerService answerService;

	@Autowired
	public UserController(UserService userService, UserQuizService userQuizService, UserAnswerService userAnswerService, AnswerService answerService) {
		this.userService = userService;
		this.userQuizService = userQuizService;
		this.userAnswerService = userAnswerService;
		this.answerService = answerService;
	}

	@GetMapping("/")
	public String showIndex(Principal principal) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		System.out.println("UserController >> Loaded logged in user: " + user.toString());
		System.out.println("UserController >> Load user roles: " + user.getRoles());		
		return "index";
	}
	
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		//data for testing
//		UserRegistration registration = new UserRegistration(new User(), "12345678", "ROLE_STUDENT");
//		registration.getUser().setUsername("huypc2410");
//		registration.getUser().setPassword("12345678");
//		registration.getUser().setAddress("218/25 Hong Bang");
//		registration.getUser().setFirstName("Huy");
//		registration.getUser().setLastName("Phùng");
//		registration.getUser().setEmail("nguoibimatthegioi@gmail.com");
//		model.addAttribute("registration", registration);
		
		
		model.addAttribute("registration", new UserRegistration());
		return "user/register";
	}
	
	@GetMapping("/template")
	public String showTemplate() {
		return "template";
	}
	
	@PostMapping("/register")
	public ModelAndView processRegistration(
			@ModelAttribute("registration") UserRegistration registration, 
			Model model,
			HttpServletRequest request) {
		try {
			User registered = userService.registerNewUser(registration);
			userService.sendVerification(registered, Utility.getSiteUrl(request));
			
		} catch (UserAlreadyExistException ex) {
			ModelAndView mav = new ModelAndView("user/register", "registration", registration);
			mav.addObject("messageError", "User is already existed, try another username");
			return mav;
		} catch (Exception ex) {
			ModelAndView mav = new ModelAndView("user/register", "registration", registration);
			System.out.println(ex.getMessage());
			mav.addObject("messageError", "Username or email is existed!");
			return mav;
		}
		return new ModelAndView("user/confirm-page");
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code) {
		boolean verified = userService.verify(code);
		return verified ? "redirect:/" : "error";
	}
	
	@GetMapping("/profile")
	public String showUserProfile(Principal principal, Model model) {
		String username = principal.getName();
		
		//lấy thông tin user
		User user = userService.findByUsername(username);
		List<String> roles = userService.convertToStringList(user.getRoles());
		
		//Tạo đối tượng update
		UserRegistration registration = new UserRegistration();
		registration.setUser(user);
		registration.setRole(roles.get(0));
		model.addAttribute("registration", registration);
		return "profile";
	}
	
	@PostMapping("/profile")
	public RedirectView processChangeProfile(@ModelAttribute("registration") UserRegistration registration) {
		RedirectView rv = null;
		try {
			User updated = userService.updateProfile(registration);
		} catch (UserAlreadyExistException ex) {
			System.out.println("UserController >> " + ex.getMessage());
			//ModelAndView mav = new ModelAndView("profile", "registration", registration);
			//mav.addObject("message", ex.getMessage());
			//return mav;
			rv = new RedirectView("/profile");
			rv.addStaticAttribute("registration", registration);
			rv.addStaticAttribute("message", ex.getMessage());
			return rv;
		}
		rv = new RedirectView("/profile");
		rv.addStaticAttribute("registration", registration);
		rv.addStaticAttribute("message", "Your profile has been updated!");
		return rv;
	}
	
	@GetMapping("/quizzes")
	public String showAttemptedQuizzesPage(Principal principal, Model model) {
		String rv = "user/attempted-quizzes";
		String username = principal.getName();
		Map<Quiz, Float> listQuiz = new HashMap<Quiz, Float>();
		Map<String, List<Quiz>> listCourse = new HashMap<String, List<Quiz>>();
		try {
			listQuiz = userQuizService.getAttemptedQuiz(username);
			String courseName;
			for (Map.Entry<Quiz, Float> quiz : listQuiz.entrySet()) {
				courseName = quiz.getKey().getModule().getCourse().getName().trim();
				if(listCourse.containsKey(courseName)) {
					listCourse.get(courseName).add(quiz.getKey());
				} else {
					List<Quiz> tempList = new ArrayList<Quiz>();
					tempList.add(quiz.getKey());
					listCourse.put(courseName, tempList);
				}
			}
			
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return rv;
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return rv;
		}
		model.addAttribute("listCourse", listCourse);
		model.addAttribute("listQuiz", listQuiz);
		return rv;
	}
	
	@GetMapping("/reviewQuiz")
	public String showReviewQuizPage(Principal principal, Model model, @Param("quiz") int quizId) {
		String rv = "user/review-quiz";
		String username = principal.getName();
		List<Integer> listCorrectAnswer = new ArrayList<Integer>();
		List<UserAnswer> listUserAnswer = new ArrayList<UserAnswer>();
		float questionScore;
		try {
			listCorrectAnswer = answerService.findAllCorrectAnswer();
			listUserAnswer = userAnswerService.findByUsernameQuiz(username, quizId);
			questionScore = 10/(listUserAnswer.size());
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return rv;
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return rv;
		}
		model.addAttribute("questionScore", questionScore);
		model.addAttribute("listUserAnswer", listUserAnswer);
		model.addAttribute("listCorrectAnswer", listCorrectAnswer);
		return rv;
	}
}
