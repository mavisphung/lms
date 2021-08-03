package com.lmsapp.project.instructor;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsapp.project.entities.Answer;
import com.lmsapp.project.entities.Content;
import com.lmsapp.project.entities.Course;
import com.lmsapp.project.entities.Module;
import com.lmsapp.project.entities.Question;
import com.lmsapp.project.entities.Quiz;
import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.services.AnswerService;
import com.lmsapp.project.services.ContentService;
import com.lmsapp.project.services.CourseService;
import com.lmsapp.project.services.ModuleService;
import com.lmsapp.project.services.QuestionService;
import com.lmsapp.project.services.QuizService;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

	private CourseService courseService;
	private ModuleService moduleService;
	private ContentService contentService;
	private QuizService quizService;
	private QuestionService questionService;
	private AnswerService answerService;

	@Autowired
	public InstructorController(CourseService courseService, ModuleService moduleService, ContentService contentService,
			QuizService quizService, QuestionService questionService, AnswerService answerService) {
		this.courseService = courseService;
		this.moduleService = moduleService;
		this.contentService = contentService;
		this.quizService = quizService;
		this.questionService = questionService;
		this.answerService = answerService;
	}

	@GetMapping("/")
	public String showinstructorIndex(Model model) {

		// get course list
		List<Course> courses = courseService.findAll();
		model.addAttribute("courses", courses);

		return "instructor/index";
	}

	// _________________________________________________START_COURSE__________________________________________________

	@GetMapping("course/showCourseFormForAdd")
	public String showCreateCoursePage(Model model) {
		model.addAttribute("course", new Course());
		return "instructor/course-form-page";
	}
	
	@PostMapping("/searchCourse")
	public String showListCourseBySearchCourseName(Model model, @RequestParam("courseName") String courseName) {

		// get course list
		List<Course> courses = courseService.findCoursesByCourseName(courseName);
		model.addAttribute("courses", courses);

		return "instructor/index";
	}

	@PostMapping("course/saveCourse")
	public String processCreateCourse(@ModelAttribute("course") Course course, Model model,
			RedirectAttributes redirectAttributes, BindingResult bindingResult, Principal principal) {
		String url = null;
		if (course.getId() == 0) {
			url = "redirect:/instructor/module";
			course.setUsername(principal.getName());
		} else {
			url = "redirect:/instructor/";
		}
		System.out.println(course);
		System.out.println("instructorController: /instructor/createCourse POST >> Created " + course.toString());

		try {
			courseService.save(course);
			redirectAttributes.addAttribute("courseId", course.getId());
		} catch (UserAlreadyExistException e) {
			System.err.println("instructorController: /instructor/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}

	@GetMapping("course/showForFormForUpdate")
	public String showUpdateCoursePage(Model model, @RequestParam("courseId") int courseId) {
		Course course = courseService.findById(courseId);
		model.addAttribute("course", course);
		return "instructor/course-form-page";
	}

	@PostMapping("course/deleteCourse")
	public String processDeleteCourse(@RequestParam("courseId") int courseId) {
		System.out.println("instructorController: /instructor/deleteCourse/ POST >> Deleting courseid " + courseId);
		String url = "redirect:/instructor/";
		courseService.deleteById(courseId);
		return url;
	}

	@PostMapping("course/enableCourse")
	public String processSetActiveCourse(@RequestParam("courseId") int courseId) {
		System.out
				.println("instructorController: /instructor/enableCourse/ POST >> Enable/Disable courseid " + courseId);
		String url = "redirect:/instructor/";
		courseService.setIsActive(courseId);
		return url;
	}

	@PostMapping("course/showCourseDetails")
	public String processRedirectCourseDetailsPage(@RequestParam("courseId") int courseId,
			RedirectAttributes redirectAttribute) {
		String url = "redirect:/instructor/module/";
		redirectAttribute.addAttribute("courseId", courseId);
		return url;
	}

	// _______________________________________________END_COURSE__________________________________________________

	// ______________________________________________START_MODULE_________________________________________________

	@GetMapping("module")
	public String showCreateModulePage(Model model, @RequestParam("courseId") int courseId) {
		Course course = courseService.findById(courseId);
		model.addAttribute("course", course);
		return "instructor/module-page";
	}

	@PostMapping("module/createModule")
	public String processCreateModule(@RequestParam("courseId") int courseId, Model model,
			@ModelAttribute("module") Module module, RedirectAttributes redirectAttributes) {
		String url = "redirect:/instructor/module/";
		System.out.println("instructorController: /instructor/createModule POST >> Created " + module.toString());

		try {
			Course course = courseService.findById(courseId);
			module.setCourse(course);
			moduleService.save(module);
			redirectAttributes.addAttribute("courseId", courseId);
		} catch (UserAlreadyExistException e) {
			System.err.println("instructorController: /instructor/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}

	@PostMapping("module/createModuleForm")
	public String processCreateModuleForm(@RequestParam("courseId") int courseId, Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println("instructorController: /instructor/createModuleForm POST >> Module Created ");

		model.addAttribute("module", new Module());
		String permitOpenForm = "yes";
		model.addAttribute("moduleForm", permitOpenForm);
		Course course = courseService.findById(courseId);
		model.addAttribute("course", course);
		return "instructor/module-page";
	}

	@PostMapping("module/updateModuleForm")
	public String processUpdateModuleForm(@RequestParam("courseId") int courseId,
			@RequestParam("moduleId") int moduleId, Model model, RedirectAttributes redirectAttributes) {
		Module module = moduleService.findById(moduleId);
		model.addAttribute("module", module);
		String permitOpenForm = "yes";
		model.addAttribute("moduleForm", permitOpenForm);
		Course course = courseService.findById(courseId);
		model.addAttribute("course", course);
		return "instructor/module-page";
	}

	@PostMapping("module/deleteModule")
	public String processDeleteModule(@RequestParam("moduleId") int moduleId, @RequestParam("courseId") int courseId,
			RedirectAttributes redirectAttribute, Model model) {
		System.out.println("instructorController: /instructor/deleteModule/ POST >> Deleting moduleId " + moduleId);
		String url = "redirect:/instructor/module/";
		redirectAttribute.addAttribute("courseId", courseId);
		moduleService.deleteById(moduleId);
		return url;
	}

	@PostMapping("module/moduleDetails")
	public String processRedirectModuleDetails(@RequestParam("moduleId") int moduleId,
			RedirectAttributes redirectAttribute, Model model) {
		System.out
				.println("instructorController: /instructor/module/moduleDetails/ POST >> Redirect to module details");
		String url = "redirect:/instructor/moduleDetails/";
		redirectAttribute.addAttribute("moduleId", moduleId);
		return url;
	}

	// ______________________________________________END_MODULE_________________________________________________

	// ______________________________________________START_MODULE_DETAILS_________________________________________________

	@GetMapping("moduleDetails")
	public String showModuleDetailsPage(Model model, @RequestParam int moduleId) {

		// Tìm module
		Module module = moduleService.findById(moduleId);

		// Thêm module vào model
		model.addAttribute("module", module);

		// Lấy tất cả contents trong module và lưu vào model
		List<Content> contents = module.getContents();
		model.addAttribute("contents", contents);

		// Lấy tất cả quizes trong module và lưu vào model

		List<Quiz> quizes = module.getQuizzes();
		model.addAttribute("quizes", quizes);

		return "instructor/module-details";
	}

	@PostMapping("moduleDetails/createQuiz")
	public String processRedirectCreateQuiz(@RequestParam("moduleId") int moduleId,
			RedirectAttributes redirectAttribute) {
		System.out
				.println("instructorController: /instructor/module/moduleDetails/ POST >> Redirect to module details");
		String url = "redirect:/instructor/quiz/createQuiz/";
		redirectAttribute.addAttribute("moduleId", moduleId);
		return url;
	}

	@PostMapping("moduleDetails/backToModule")
	public String processRedirectToModule(@RequestParam("moduleId") int moduleId,
			RedirectAttributes redirectAttribute) {
		String url = "redirect:/instructor/module/";
		Module module = moduleService.findById(moduleId);
		Course course = module.getCourse();
		redirectAttribute.addAttribute("courseId", course.getId());
		return url;
	}

	@PostMapping("moduleDetails/deleteQuiz")
	public String processDeleteQuiz(@RequestParam("moduleId") int moduleId, @RequestParam("quizId") int quizId,
			RedirectAttributes redirectAttribute, Model model) {
		String url = "redirect:/instructor/moduleDetails/";
		redirectAttribute.addAttribute("moduleId", moduleId);
		quizService.deleteById(quizId);
		return url;
	}
	
	@PostMapping("moduleDetails/quizDetails")
	public String processRedirectToQuizDetails(@RequestParam("quizId") int quizId,
			RedirectAttributes redirectAttribute) {
		String url = "redirect:/instructor/question/";
		redirectAttribute.addAttribute("quizId", quizId);
		return url;
	}

	// ______________________________________________END_MODULE_DETAILS_________________________________________________

	// ______________________________________________START_QUIZ_________________________________________________

	@GetMapping("quiz/createQuiz")
	public String showCreateQuizPage(Model model, @RequestParam int moduleId) {
		Module module = moduleService.findById(moduleId);
		model.addAttribute("module", module);
		model.addAttribute("quiz", new Quiz());
		return "instructor/create-new-quiz";
	}

	@GetMapping("quiz/updateQuiz")
	public String showUpdateQuizPage(Model model, @RequestParam int moduleId, @RequestParam int quizId) {
		Module module = moduleService.findById(moduleId);
		model.addAttribute("module", module);
		Quiz quiz = quizService.findById(quizId);
		model.addAttribute("quiz", quiz);
		return "instructor/create-new-quiz";
	}

	@PostMapping("quiz/createQuiz")
	public String processCreateQuiz(@RequestParam("moduleId") int moduleId, RedirectAttributes redirectAttribute,
			@ModelAttribute("quiz") Quiz quiz, Model model, RedirectAttributes redirectAttributes) {
		String url = "";

		if (quiz.getId() == 0) {
			url = "redirect:/instructor/question";
			System.out.println();
		} else {
			url = "redirect:/instructor/moduleDetails";
		}
		System.out.println(url);
		System.out.println("instructorController: /instructor/createQuiz POST >> Quiz Created ");

		try {
			Module module = moduleService.findById(moduleId);
			System.out.println(1);
			quiz.setModule(module);
			System.out.println(2);
			quizService.save(quiz);
			System.out.println(3);
			redirectAttributes.addAttribute("quizId", quiz.getId());
			redirectAttributes.addAttribute("moduleId", moduleId);
		} catch (UserAlreadyExistException e) {
			System.err.println("instructorController: /instructor/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}

	// ______________________________________________END_QUIZ_________________________________________________

	// ______________________________________________START_QUESTION_________________________________________________

	@GetMapping("question")
	public String showCreateQuestionPage(Model model, @RequestParam int quizId) {
		Quiz quiz = quizService.findById(quizId);
		model.addAttribute("quiz", quiz);
		return "instructor/quiz-details";
	}

	@PostMapping("question/createQuestionForm")
	public String processCreateQuestionForm(@RequestParam("quizId") int quizId, Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println("instructorController: /instructor/createQuestionForm POST >> Question Created ");

		String url = "redirect:/instructor/question/";

		String permitOpenForm = "yes";
		model.addAttribute("questionForm", permitOpenForm);
		Quiz quiz = quizService.findById(quizId);
		model.addAttribute("quiz", quiz);
		model.addAttribute("question", new Question());
		System.out
				.println("___________________________________________________________________________________________");
		return "instructor/quiz-details";
	}

	@PostMapping("question/updateQuestionForm")
	public String processUpdateQuestionForm(@RequestParam("quizId") int quizId,
			@RequestParam("questionId") int questionId, Model model, RedirectAttributes redirectAttributes) {
		String url = "redirect:/instructor/question/";

		String permitOpenForm = "yes";
		model.addAttribute("questionForm", permitOpenForm);
		Quiz quiz = quizService.findById(quizId);
		model.addAttribute("quiz", quiz);
		Question question = questionService.findById(questionId);
		model.addAttribute("question", question);
		System.out
				.println("___________________________________________________________________________________________");
		return "instructor/quiz-details";
	}

	@PostMapping("question/createQuestion")
	public String processCreateQuestion(@RequestParam("quizId") int quizId, RedirectAttributes redirectAttribute,
			@ModelAttribute("question") Question question, Model model) {
		String url = "redirect:/instructor/question/";

		System.out.println("instructorController: /instructor/createQuiz POST >> Quiz Created ");

		try {
			Quiz quiz = quizService.findById(quizId);
			question.setQuiz(quiz);
			questionService.save(question);
			redirectAttribute.addAttribute("quizId", quizId);
		} catch (UserAlreadyExistException e) {
			System.err.println("instructorController: /instructor/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}

	@PostMapping("question/answer")
	public String processRedirectAnswer(@RequestParam("questionId") int questionId,
			RedirectAttributes redirectAttribute, Model model) {
		System.out
				.println("instructorController: /instructor/module/moduleDetails/ POST >> Redirect to module details");
		String url = "redirect:/instructor/answer/";
		redirectAttribute.addAttribute("questionId", questionId);
		return url;
	}
	
	@PostMapping("question/deleteQuestion")
	public String processDeleteQuestion(@RequestParam("questionId") int questionId, @RequestParam("quizId") int quizId,
			RedirectAttributes redirectAttribute, Model model) {
		String url = "redirect:/instructor/question/";
		redirectAttribute.addAttribute("quizId", quizId);
		questionService.deleteById(questionId);
		return url;
	}
	
	

	// ______________________________________________END_QUESTION_________________________________________________

	// ______________________________________________START_ANSWER_________________________________________________

	@GetMapping("answer")
	public String showAnswerPage(Model model, @RequestParam int questionId) {
		Question question = questionService.findById(questionId);
		model.addAttribute("question", question);
		return "instructor/answer-page";
	}

	@PostMapping("answer/createAnswerForm")
	public String processCreateAnswerForm(@RequestParam("questionId") int questionId, Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println("instructorController: /instructor/createAnswerForm POST >> Answer Created ");

		String url = "redirect:/instructor/answer/";

		String permitOpenForm = "yes";
		model.addAttribute("answerForm", permitOpenForm);
		Question question = questionService.findById(questionId);
		model.addAttribute("question", question);
		model.addAttribute("answer", new Answer());
		System.out
				.println("___________________________________________________________________________________________");
		return "instructor/answer-page";
	}

	@PostMapping("answer/updateAnswerForm")
	public String processUpdateAnswerForm(@RequestParam("questionId") int questionId,
			@RequestParam("answerId") int answerId, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("instructorController: /instructor/createAnswerForm POST >> Answer Created ");

		String url = "redirect:/instructor/answer/";

		String permitOpenForm = "yes";
		model.addAttribute("answerForm", permitOpenForm);
		Question question = questionService.findById(questionId);
		model.addAttribute("question", question);
		Answer answer = answerService.findById(answerId);
		model.addAttribute("answer", answer);
		System.out
				.println("___________________________________________________________________________________________");
		return "instructor/answer-page";
	}

	@PostMapping("answer/createAnswer")
	public String processCreateAnswer(@RequestParam("questionId") int questionId, RedirectAttributes redirectAttribute,
			@ModelAttribute("answer") Answer answer, Model model, @RequestParam("isCorrect") String isCorrect) {
		String url = "redirect:/instructor/answer/";

		System.out.println("instructorController: /instructor/answer/createAnswer POST >> Answer Created ");

		try {
			if (isCorrect.equals("correct")) {
				answer.setCorrect(true);
			} else if (isCorrect.equals("incorrect")) {
				answer.setCorrect(false);
			} else {
				throw new RuntimeException("Not find isCorrect variable");
			}
			Question question = questionService.findById(questionId);
			answer.setQuestion(question);
			answerService.save(answer);
			redirectAttribute.addAttribute("questionId", questionId);
		} catch (UserAlreadyExistException e) {
			System.err.println("instructorController: /instructor/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}

	@PostMapping("answer/deleteAnswer")
	public String processDeleteAnswer(@RequestParam("answerId") int answerId,
			@RequestParam("questionId") int questionId, RedirectAttributes redirectAttribute, Model model) {
		String url = "redirect:/instructor/answer/";
		redirectAttribute.addAttribute("questionId", questionId);
		answerService.deleteById(answerId);
		return url;
	}

	// ______________________________________________END_ANSWER_________________________________________________

}
