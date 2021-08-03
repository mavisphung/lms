package com.lmsapp.project.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lmsapp.project.entities.Module;
import com.lmsapp.project.model.ContentVM;
import com.lmsapp.project.services.ContentService;
import com.lmsapp.project.services.ModuleService;

@Controller
@RequestMapping("/instructor/content")
public class ContentController {

	@Autowired
	private ContentService contentService;

	@Autowired
	private ModuleService moduleService;

	@GetMapping(value = { "/", "" })
	public String showContentIndex(@RequestParam(name = "moduleId", required = false) Integer moduleId, Model model) {
		ContentVM vm = new ContentVM();
		Module module = moduleService.findById(moduleId);
		vm.setModule(module);
		vm.setContents(module.getContents());
		model.addAttribute("contentVM", vm);
		return "content/add-form";
	}

	@PostMapping("/add")
	public String processAddContents(@ModelAttribute("contentVM") ContentVM vm, Model model) {
		String url = "/instructor/content";
		System.out.println(vm.getModule().toString());
		// update lại module
		Module moduleFromdB = moduleService.findById(vm.getModule().getId());
		// xử lí add bằng service
		try {
			for (MultipartFile file : vm.getFiles()) {
				contentService.save(file, moduleFromdB);
			}
		} catch (Exception e) {
			System.out.println("ContentController >> Error: " + e.getMessage());
			model.addAttribute("error", e);
			return "error";
		}
		

		return "redirect:" + url + "?moduleId=" + vm.getModule().getId();
	}
}
