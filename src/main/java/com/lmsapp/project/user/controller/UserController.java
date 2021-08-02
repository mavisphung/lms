package com.lmsapp.project.user.controller;

import java.security.Principal;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.lmsapp.project.entities.Course;
import com.lmsapp.project.entities.Module;
import com.lmsapp.project.entities.Question;
import com.lmsapp.project.entities.Quiz;
import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.services.CourseService;
import com.lmsapp.project.services.QuestionService;
import com.lmsapp.project.services.QuizService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;
import com.lmsapp.project.util.Utility;

@Controller
public class UserController {

	// @Autowired
	private final UserService userService;
	private final CourseService courseService;
	private final QuizService quizService;
	private final QuestionService questionService;

	@Autowired
	public UserController(UserService userService, CourseService courseService, QuizService quizService,
			QuestionService questionService) {
		this.userService = userService;
		this.courseService = courseService;
		this.quizService = quizService;
		this.questionService = questionService;
	}

	@GetMapping("/")
	public String showIndex(Principal principal, Model model) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		System.out.println("UserController >> Loaded logged in user: " + user.toString());
		System.out.println("UserController >> Load user roles: " + user.getRoles());

		List<Course> courses = courseService.findAll();

		model.addAttribute("courses", courses);

		return "index";
	}

	// ______________________________________________HUY_PC___________________________________________________

	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}

	@GetMapping("/register")
	public String showRegister(Model model) {
		// data for testing
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
	public ModelAndView processRegistration(@ModelAttribute("registration") UserRegistration registration, Model model,
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

		// lấy thông tin user
		User user = userService.findByUsername(username);
		List<String> roles = userService.convertToStringList(user.getRoles());

		// Tạo đối tượng update
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
			// ModelAndView mav = new ModelAndView("profile", "registration", registration);
			// mav.addObject("message", ex.getMessage());
			// return mav;
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

	// ______________________________________________DUNG_LQ___________________________________________________
	
	//_______________________________________________START_COURSE______________________________________________
	@GetMapping("course")
	public String showCoursePage(Model model, @RequestParam("courseId") int courseId) {
		Course course = courseService.findById(courseId);
		List<Module> modules = course.getModules();
		String welcomeCourse = "Course " + course.getName();
		model.addAttribute("welcome", welcomeCourse);
		model.addAttribute("course", course);
		return "course-page";
	}

	//_______________________________________________END_COURSE_______________________________________________
	
	//_______________________________________________START_QUIZ_______________________________________________
	@GetMapping("quiz")
	public String showDoingQuizPage(Model model, @RequestParam("quizId") int quizId) {
		Quiz quiz = quizService.findById(quizId);
		String welcomeQuiz = "quiz " + quiz.getName();
		model.addAttribute("welcome", welcomeQuiz);
		model.addAttribute("quiz", quiz);
		return "doing-quiz-page";
	}

	@PostMapping("quiz/submit")
	public String processCreateModule(@RequestParam("questionId") List<Integer> questionIds, Model model,
			HttpServletRequest request, @RequestParam("quizId") int quizId, RedirectAttributes redirectAttribute) {
		String url = "redirect:/quiz/score";
		try {
			int count = 0;
			String[] questionIdss = request.getParameterValues("questionId");
			System.out.println("__________________________________sdfsdfs__________");
			
			for (String questionId : questionIdss) {
				System.out.println(questionId);
			}
			for (int questionId : questionIds) {
				int answerIdCorrect = questionService.findAnswerIdCorrect(questionId);
				String answerIdString = request.getParameter("answer_" + questionId);
				int answerId = Integer.parseInt(answerIdString);
				if(answerIdCorrect == answerId) {
					count++;
				}
			}
			System.out.println("____________________________________________");
			float score = (float) (count * 10)/questionIds.size();
			redirectAttribute.addAttribute("score",score);
			redirectAttribute.addAttribute("quizId", quizId);
		} catch (UserAlreadyExistException e) {
			System.err.println("instructorController: /instructor/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}
	
	@GetMapping("quiz/score")
	public String showQuizScore(Model model, @RequestParam("quizId") int quizId, @RequestParam("score") float score) {
		Quiz quiz = quizService.findById(quizId);
		String welcomeQuiz = "quiz " + quiz.getName();
		model.addAttribute("welcome", welcomeQuiz);
		model.addAttribute("quiz", quiz);
		model.addAttribute("score", score);
		return "score-page";
	}
	//_______________________________________________END_QUIZ_________________________________________________
}
