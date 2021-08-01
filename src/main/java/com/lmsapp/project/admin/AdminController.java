package com.lmsapp.project.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsapp.project.entities.Course;
import com.lmsapp.project.entities.Module;
import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.role.Role;
import com.lmsapp.project.role.RoleService;
import com.lmsapp.project.services.CourseService;
import com.lmsapp.project.services.ModuleService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	

	private UserService userService;
	private CourseService courseService;
	private ModuleService moduleService;

	@Autowired
	public AdminController(UserService userService, RoleService roleService, PasswordEncoder encoder,
			CourseService courseService, ModuleService moduleService) {
		this.userService = userService;
		this.courseService = courseService;
		this.moduleService = moduleService;
	}

	@GetMapping(value = { "/", ""})
	public String showAdminIndex(Model model) {

		// get user list
		List<User> users = userService.findAll();

		for (User user : users) {
			Set<Role> userRoles = user.getRoles();
			List<String> strRoles = convertToStringList(userRoles);
			user.setStrRoles(strRoles);
		}
		model.addAttribute("users", users);

		// get course list
		List<Course> courses = courseService.findAll();
		model.addAttribute("courses", courses);

		return "admin/index";
	}

	@GetMapping("/create")
	public String showCreatePage(Model model) {
		model.addAttribute("registration", new UserRegistration());
		return "admin/create-new-user";
	}

	@PostMapping("/create")
	public String processCreateUser(@ModelAttribute("registration") UserRegistration registration, Model model) {
		String url = "redirect:/admin/";
		System.out.println("AdminController: /admin/create POST >> Created " + registration.getUser().toString());
		System.out.println("AdminController: /admin/create POST >> Created user role " + registration.getRole());
		System.out.println(
				"AdminController: /admin/create POST >> Created confirm password " + registration.getConfirmPassword());

		try {
			userService.registerNewUser(registration);
		} catch (UserAlreadyExistException e) {
			System.err.println("AdminController: /admin/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}
	
	@PostMapping("/search")
	public String showListCourseBySearchCourseName(Model model, @RequestParam("searchValue") String username) {

		// get course list
		List<User> users = userService.findUsersByUsername(username);

		for (User user : users) {
			Set<Role> userRoles = user.getRoles();
			List<String> strRoles = convertToStringList(userRoles);
			user.setStrRoles(strRoles);
		}
		model.addAttribute("users", users);

		return "admin/index";
	}

	//______________________________________COURSE________________________________________
	
	
	@GetMapping("/createCourse")
	public String showCreateCoursePage(Model model) {
		model.addAttribute("course", new Course());
		return "admin/create-new-course";
	}

	@PostMapping("/createCourse")
	public String processCreateCourse(@ModelAttribute("course") Course course, Model model,
			RedirectAttributes redirectAttributes) {
		String url = "redirect:/admin/createModule";
		System.out.println("AdminController: /admin/createCourse POST >> Created " + course.toString());

		try {
			courseService.save(course);
			redirectAttributes.addAttribute("course", course);
		} catch (UserAlreadyExistException e) {
			System.err.println("AdminController: /admin/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}
	
	@GetMapping("/updateCourse")
	public String showUpdateCoursePage(Model model, @RequestParam("courseId") int courseId) {
		Course course = courseService.findById(courseId);
		model.addAttribute("course", course);
		return "admin/update-course";
	}
	
	@PostMapping("/updateCourse")
	public String processUpdateCourse(@ModelAttribute("course") Course course, Model model,
			RedirectAttributes redirectAttributes) {
		String url = "redirect:/admin/";
		System.out.println("AdminController: /admin/updateCourse POST >> Updated " + course.toString());

		try {
			courseService.save(course);
			redirectAttributes.addAttribute("course", course);
		} catch (UserAlreadyExistException e) {
			System.err.println("AdminController: /admin/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}
	
	@PostMapping("/deleteCourse")
	public String processDeleteCourse(@RequestParam("courseId") int courseId) {
		System.out.println("AdminController: /admin/deleteCourse/ POST >> Deleting courseid " + courseId);
		String url = "redirect:/admin/";
		courseService.deleteById(courseId);
		return url;
	}
	
	@PostMapping("/enableCourse")
	public String processSetActiveCourse(@RequestParam("courseId") int courseId) {
		System.out.println("AdminController: /admin/enableCourse/ POST >> Enable/Disable courseid " + courseId);
		String url = "redirect:/admin/";
		courseService.setIsActive(courseId);
		return url;
	}
	
	
	// _____________________________________________MODULE_________________________________________________
	

	@GetMapping("/createModule")
	public String showCreateModulePage(Model model, @RequestParam Course course) {
		model.addAttribute("course", course);
//		List<Module> module = moduleService.getModulesByCourseId(course.getId());
//		model.addAttribute("modules", module);
		return "admin/create-modules";
	}



	@PostMapping("/createModule")
	public String processCreateModule(@RequestParam("courseId") int courseId,
										Model model,
										@ModelAttribute("module") Module module,
										RedirectAttributes redirectAttributes) {
		String url = "redirect:/admin/createModule";
		System.out.println("AdminController: /admin/createModule POST >> Created " + module.toString());

		try {
			System.out.println(courseId);
			Course course = courseService.findById(courseId);
			redirectAttributes.addAttribute("course", course);
			module.setCourse(course);
			moduleService.save(module);
		} catch (UserAlreadyExistException e) {
			System.err.println("AdminController: /admin/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}

	@PostMapping("/createModuleForm")
	public String processCreateModuleForm(@RequestParam("courseId") int courseId, Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println("AdminController: /admin/createModuleForm POST >> Module Created ");

		model.addAttribute("module", new Module());
		String permitOpenForm = "yes";
		model.addAttribute("moduleForm", permitOpenForm);
		System.out.println(courseId);
		Course course = courseService.findById(courseId);
		model.addAttribute("course", course);
		return "admin/create-modules";
	}
	
	@PostMapping("/deleteModule")
	public String processDeleteModule(@RequestParam("moduleId") int moduleId,
										@RequestParam("courseId") int courseId,
										RedirectAttributes redirectAttribute,
										Model model) {
		System.out.println("AdminController: /admin/deleteModule/ POST >> Deleting moduleId " + moduleId);
		String url = "redirect:/admin/createModule/";
		Course course = courseService.findById(courseId);
		System.out.println(courseId);
		redirectAttribute.addAttribute("course", course);
		moduleService.deleteById(moduleId);
		return url;
	}
	// ______________________________________________________________________________________________
	@PostMapping("/delete")
	public String processDelete(@RequestParam("username") String username) {
		System.out.println("AdminController: /admin/delete/ POST >> Deleting user " + username);
		String url = "redirect:/admin/";
		userService.remove(username);
		return url;
	}

	@PostMapping("/enable")
	public String showEditPage(@RequestParam("username") String username, Model model) {
		System.out.println("AdminController: /admin/edit/ GET >> Retrieving user " + username);
		String url = "redirect:/admin/";
		try {
			User userFromDb = userService.findByUsername(username);
			if (userFromDb.isEnabled()) {
				userFromDb.setEnabled(false);
				System.out.println("Username " + userFromDb.getUsername() + " is locked");
			} else {
				userFromDb.setEnabled(true);
				System.out.println("Username " + userFromDb.getUsername() + " is unlocked");
			}
			userService.save(userFromDb);

		} catch (Exception e) {
			System.err.println("AdminController: /admin/enable/ POST >> " + e.getMessage());
			model.addAttribute("error", "Invalid username");
		}

		return url;
	}

	private List<String> convertToStringList(Set<Role> roles) {
		List<String> strRoles = new ArrayList<String>();
		for (Role role : roles) {
			strRoles.add(role.getName().toString());
		}
		return strRoles;
	}

}
