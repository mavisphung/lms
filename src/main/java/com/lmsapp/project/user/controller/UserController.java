package com.lmsapp.project.user.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.lmsapp.project.entities.Course;
import com.lmsapp.project.entities.Enrollment;
import com.lmsapp.project.entities.Module;
import com.lmsapp.project.entities.Quiz;
import com.lmsapp.project.entities.UserAnswer;
import com.lmsapp.project.entities.UserQuizz;
import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.services.AnswerService;
import com.lmsapp.project.services.CourseService;
import com.lmsapp.project.services.EnrollmentService;
import com.lmsapp.project.services.QuestionService;
import com.lmsapp.project.services.QuizService;
import com.lmsapp.project.services.UserAnswerService;
import com.lmsapp.project.services.UserQuizService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;
import com.lmsapp.project.util.Utility;

@Controller
public class UserController {

	// @Autowired
	private final UserService userService;
	private final CourseService courseService;
	private final EnrollmentService enrollService;
	private final QuizService quizService;
	private final QuestionService questionService;
	private final UserQuizService userQuizService;
	private final UserAnswerService userAnswerService;
	private final AnswerService answerService;

	@Autowired

	public UserController(UserService userService, CourseService courseService, EnrollmentService enrollService,
			QuizService quizService, QuestionService questionService, UserQuizService userQuizService,
			UserAnswerService userAnswerService, AnswerService answerService) {

		this.userService = userService;
		this.courseService = courseService;
		this.enrollService = enrollService;
		this.quizService = quizService;
		this.questionService = questionService;
		this.userQuizService = userQuizService;
		this.userAnswerService = userAnswerService;
		this.answerService = answerService;
	}

	@GetMapping("/")
	public String showIndex(Principal principal, Model model) {
		String username = principal.getName();
		User user = userService.findByUsername(username);

		List<Enrollment> enrollments = enrollService.findByUserId(user.getId());
//		model.addAttribute("courses", enrolledCourses);
		for (Enrollment enrollment : enrollments) {
			System.out.println("UserController >> user " + enrollment.getUser().getUsername() + " enrolled "
					+ enrollment.getCourse().getName());
		}
		model.addAttribute("enrollments", enrollments);

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

	// _______________________________________________START_COURSE______________________________________________
	@GetMapping("course")
	public String showCoursePage(Model model, @RequestParam("courseId") int courseId) {
		Course course = courseService.findById(courseId);
		List<Module> modules = course.getModules();
		String welcomeCourse = "Course " + course.getName();
		model.addAttribute("welcome", welcomeCourse);
		model.addAttribute("course", course);
		return "course-page";
	}

	// _______________________________________________END_COURSE_______________________________________________

	// __________________________________________START_ATTEMPT_QUIZ____________________________________________

	@GetMapping("quiz/attempt")
	public String showAttemptQuizPage(Model model, @RequestParam("quizId") int quizId) {
		Quiz quiz = quizService.findById(quizId);
		model.addAttribute("quiz", quiz);
		return "attempt-quiz-page";
	}

	@PostMapping("quiz/attempt")
	public String processAttemptQuiz(Model model, @RequestParam("quizId") int quizId, Principal principal,
			RedirectAttributes redirectAttribute) {
		String url = "redirect:/quiz/";
		User user = userService.findByUsername(principal.getName());
		Quiz quiz = quizService.findById(quizId);
		UserQuizz userQuiz = new UserQuizz();
		userQuiz.setQuiz(quiz);
		userQuiz.setUser(user);
		Date currentDate = new Date();
		System.out.println(currentDate);
		userQuiz.setDoingDate(currentDate);
		userQuizService.saveAttemptQuiz(userQuiz);
		redirectAttribute.addAttribute("quizId", quizId);
		int userQuizId = userQuiz.getId();
		redirectAttribute.addAttribute("userQuizId", userQuiz.getId());
		return url;
	}

	// ___________________________________________END_ATTEMPT_QUIZ_____________________________________________

	// _______________________________________________START_QUIZ_______________________________________________
	@GetMapping("quiz")
	public String showDoingQuizPage(Model model, @RequestParam("quizId") int quizId,
			@RequestParam("userQuizId") int userQuizId) {
		Quiz quiz = quizService.findById(quizId);
		String welcomeQuiz = "quiz " + quiz.getName();
		model.addAttribute("welcome", welcomeQuiz);
		model.addAttribute("quiz", quiz);
		model.addAttribute("userQuizId", userQuizId);
		return "doing-quiz-page";
	}

	@PostMapping("quiz/submit")
	public String processCreateModule(HttpServletRequest request, RedirectAttributes redirectAttribute) {
		String url = "redirect:/quiz/score";
		try {
			int count = 0;
			String[] questionIdsString = request.getParameterValues("questionId");
			int userQuizId = Integer.parseInt(request.getParameter("userQuizId"));
			UserQuizz userQuizz = userQuizService.findById(userQuizId);
			List<Integer> questionIds = new ArrayList<Integer>();
			for (String questionIdString : questionIdsString) {
				int questionId = Integer.parseInt(questionIdString);
				questionIds.add(questionId);
			}
			int quizId = Integer.parseInt(request.getParameter("quizId"));
			for (int questionId : questionIds) {
				int answerIdCorrect = questionService.findAnswerIdCorrect(questionId);
				String answerIdString = request.getParameter("answer_" + questionId);
				int answerId = 0;
				if (answerIdString != null) {
					answerId = Integer.parseInt(answerIdString);
				}
				if (answerIdCorrect == answerId) {
					count++;
				}
				// ------------------------------------
				if (answerId != 0) {
					UserAnswer userAnswer = new UserAnswer(userQuizz, questionService.findById(questionId),
							answerService.findById(answerId));
					userAnswerService.save(userAnswer);
				}else {
					UserAnswer userAnswer = new UserAnswer(userQuizz, questionService.findById(questionId),
							null);
					userAnswerService.save(userAnswer);
				}
			}
			float score = (float) (count * 10) / questionIds.size();
			userQuizz.setScore(score);
			userQuizService.saveAttemptQuiz(userQuizz);
			redirectAttribute.addAttribute("score", score);
			redirectAttribute.addAttribute("quizId", quizId);
		} catch (UserAlreadyExistException e) {
			System.err.println("instructorController: /instructor/create POST >> " + e.getMessage());
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
	// _______________________________________________END_QUIZ_________________________________________________

	// ____________________________________________THAO_________________________________________________________
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
				if (listCourse.containsKey(courseName)) {
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
			questionScore = 10 / (listUserAnswer.size());
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
