package com.lmsapp.project.instructor;

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

import com.lmsapp.project.enties.Answer;
import com.lmsapp.project.enties.Content;
import com.lmsapp.project.enties.Course;
import com.lmsapp.project.enties.Module;
import com.lmsapp.project.enties.Question;
import com.lmsapp.project.enties.Quiz;
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

	@PostMapping("course/saveCourse")
	public String processCreateCourse(@ModelAttribute("course") Course course, Model model,
			RedirectAttributes redirectAttributes, BindingResult bindingResult) {
		String url = "redirect:/instructor/module/createModule";
		System.out.println(course);
		System.out.println("instructorController: /instructor/createCourse POST >> Created " + course.toString());

		try {
			courseService.save(course);
			redirectAttributes.addAttribute("course", course);
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
		System.out.println(course);
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

	// _______________________________________________END_COURSE__________________________________________________

	// ______________________________________________START_MODULE_________________________________________________

	@GetMapping("module/createModule")
	public String showCreateModulePage(Model model, @RequestParam Course course) {
		model.addAttribute("course", course);
		return "instructor/module-page";
	}

	@PostMapping("module/createModule")
	public String processCreateModule(@RequestParam("courseId") int courseId, Model model,
			@ModelAttribute("module") Module module, RedirectAttributes redirectAttributes) {
		String url = "redirect:/instructor/module/createModule";
		System.out.println("instructorController: /instructor/createModule POST >> Created " + module.toString());

		try {
			System.out.println(courseId);
			Course course = courseService.findById(courseId);
			redirectAttributes.addAttribute("course", course);
			module.setCourse(course);
			moduleService.save(module);
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
		System.out.println(courseId);
		Course course = courseService.findById(courseId);
		model.addAttribute("course", course);
		return "instructor/module-page";
	}

	@PostMapping("module/deleteModule")
	public String processDeleteModule(@RequestParam("moduleId") int moduleId, @RequestParam("courseId") int courseId,
			RedirectAttributes redirectAttribute, Model model) {
		System.out.println("instructorController: /instructor/deleteModule/ POST >> Deleting moduleId " + moduleId);
		String url = "redirect:/instructor/module/createModule/";
		Course course = courseService.findById(courseId);
		System.out.println(courseId);
		redirectAttribute.addAttribute("course", course);
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
		
		List<Quiz> quizes = module.getQuizes();
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

	// ______________________________________________END_MODULE_DETAILS_________________________________________________

	// ______________________________________________START_QUIZ_________________________________________________

	@GetMapping("quiz/createQuiz")
	public String showCreateQuizPage(Model model, @RequestParam int moduleId) {
		Module module = moduleService.findById(moduleId);
		System.out.println(module);
		model.addAttribute("module", module);
		model.addAttribute("quiz", new Quiz());
		return "instructor/create-new-quiz";
	}

	@PostMapping("quiz/createQuiz")
	public String processCreateQuiz(@RequestParam("moduleId") int moduleId, RedirectAttributes redirectAttribute,
			@ModelAttribute("quiz") Quiz quiz, Model model, RedirectAttributes redirectAttributes) {
		String url = "redirect:/instructor/question";

		System.out.println("instructorController: /instructor/createQuiz POST >> Quiz Created ");

		try {
			Module module = moduleService.findById(moduleId);
			quiz.setModule(module);
			quizService.save(quiz);
			redirectAttributes.addAttribute("quizId", quiz.getId());

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
		System.out.println(quiz);
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

	// ______________________________________________END_QUESTION_________________________________________________

	// ______________________________________________START_ANSWER_________________________________________________

	@GetMapping("answer")
	public String showAnswerPage(Model model, @RequestParam int questionId) {
		Question question = questionService.findById(questionId);
		System.out.println(question);
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

	@PostMapping("answer/createAnswer")
	public String processCreateAnswer(@RequestParam("questionId") int questionId, RedirectAttributes redirectAttribute,
			@ModelAttribute("answer") Answer answer, Model model, @RequestParam("isCorrect") String isCorrect) {
		String url = "redirect:/instructor/answer/";

		System.out.println("instructorController: /instructor/answer/createAnswer POST >> Answer Created ");

		try {
			if(isCorrect.equals("correct")) {
				answer.setCorrect(true);
			}else if(isCorrect.equals("incorrect")) {
				answer.setCorrect(false);
			}else {
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
	
	// ______________________________________________END_ANSWER_________________________________________________
}
