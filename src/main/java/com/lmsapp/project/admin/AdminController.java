package com.lmsapp.project.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletContext;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.lmsapp.project.content.ContentService;
import com.lmsapp.project.content.MediaTypeUtils;
import com.lmsapp.project.enties.Content;
import com.lmsapp.project.enties.Course;
import com.lmsapp.project.enties.Module;
import com.lmsapp.project.exception.UserAlreadyExistException;
import com.lmsapp.project.model.UserRegistration;
import com.lmsapp.project.role.Role;
import com.lmsapp.project.role.RoleService;
import com.lmsapp.project.services.ModuleService;
import com.lmsapp.project.services.iservices.ICourseService;
import com.lmsapp.project.services.iservices.IModuleService;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {

	private UserService userService;
	private RoleService roleService;
	private PasswordEncoder encoder;
	private ICourseService courseService;
	private IModuleService moduleService;
	private ContentService contentService;

	@Autowired
	public AdminController(UserService userService, RoleService roleService, PasswordEncoder encoder,
			ICourseService courseService, IModuleService moduleService, ContentService contentService) {
		this.userService = userService;
		this.roleService = roleService;
		this.encoder = encoder;
		this.courseService = courseService;
		this.moduleService = moduleService;
		this.contentService = contentService;
	}

	@GetMapping("/")
	public String showAdminIndex(Model model) {

		// get user list
		List<User> users = userService.findAll();

		for (User user : users) {
			Set<Role> userRoles = user.getRoles();
			List<String> strRoles = convertToStringList(userRoles);
			user.setStrRoles(strRoles);
		}

		callGetRoles(users);

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
	public String processCreateUser(
			@ModelAttribute("registration") UserRegistration registration,
			Model model) {
		String url = "redirect:/admin/";
		System.out.println("AdminController: /admin/create POST >> Created " + registration.getUser().toString());
		System.out.println("AdminController: /admin/create POST >> Created user role " + registration.getRole());
		System.out.println(
				"AdminController: /admin/create POST >> Created confirm password " + registration.getConfirmPassword());

		try {
			User registered = userService.registerNewUser(registration);
		} catch (UserAlreadyExistException e) {
			System.err.println("AdminController: /admin/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		return url;
	}

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

	@GetMapping("/createModule")
	public String showCreateModulePage(Model model, @RequestParam Course course) {
		model.addAttribute("course", course);
		return "admin/create-modules";
	}

	// ______________________________________________________________________________________________

	@PostMapping("/createModule")
	public String processCreateModule(@RequestParam("courseId") int courseId, Model model,
			@ModelAttribute("module") Module module, RedirectAttributes redirectAttributes) {
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

	// ______________________________________________________________________________________________

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

	
	@GetMapping("/search")
	public String searchValue(
			@RequestParam("searchValue") String searchValue,
			Model model) {
		List<User> users = userService.findUsersByUsername(searchValue);
		callGetRoles(users);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("users", users);
		System.out.println("AdminController: /admin/search GET >> " + users.toString());
		System.out.println("AdminController: /admin/search GET >> Search value: " + searchValue.trim());
		
		return "admin/index";
	}
	
	private void callGetRoles(List<User> users) {
		for (User user : users) {
			Set<Role> userRoles = user.getRoles();
			List<String> strRoles = convertToStringList(userRoles);
			user.setStrRoles(strRoles);
		}
	}	
	

	private List<String> convertToStringList(Set<Role> roles) {
		List<String> strRoles = new ArrayList<String>();
		for (Role role : roles) {
			strRoles.add(role.getName().toString());
		}
		return strRoles;
	}

	@GetMapping("/content")
	public String createContent(@RequestParam("moduleId") int id, Model model) {
		model.addAttribute("content", new Content());
		model.addAttribute("moduleId", id);
		// get list content
		List<Content> contents = contentService.findAll();
		model.addAttribute("files", contents);
		return "admin/create-content";
	}

	
	public static String uploadDirectory = System.getProperty("user.dir") +"/src/main/webapp/uploads";
	@PostMapping("/createContentForm")
	public String createContent(@RequestParam("moduleId") int moduleId, @RequestParam("file") MultipartFile file,
			Model model, @ModelAttribute("content") Content content, RedirectAttributes redirectAttributes)
			throws IOException {
		String url = "redirect:/admin/content/";
		System.out.println("AdminController: /admin/createContent POST >> Created " + content.toString());
		
		try {
			String path = uploadDirectory + "/m" +moduleId;
			File pathAsFile = new File(path);
			if (!Files.exists(Paths.get(path))) {
				pathAsFile.mkdir();
			}
			System.out.println(moduleId);
			Module module = moduleService.findById(moduleId);
			redirectAttributes.addAttribute("module", module);
			redirectAttributes.addAttribute("moduleId", moduleId);
			content.setModule(module);			
			String filePath = Paths.get(path,file.getOriginalFilename()).toString();
			content.setUrl(filePath);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			stream.write(file.getBytes());
			stream.close();
			contentService.save(content);
		} catch (UserAlreadyExistException e) {
			System.err.println("AdminController: /admin/create POST >> " + e.getMessage());
			model.addAttribute("error", e.getMessage());
		}
		return url;
	}

	@GetMapping("/deleteContent")
	public String deleteContent(@RequestParam("contentId") int id) {
		String url = "redirect:/admin/";
		contentService.deleteContentById(id);
		return url;
	}
	 
	
}
