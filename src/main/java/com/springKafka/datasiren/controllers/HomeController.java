package com.springKafka.datasiren.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	@GetMapping("/home")
	public String home(Model model) {
		return "home";
	}
        @GetMapping("/timeline")
	public String timeline(Model model) {
		return "timeline";
	}
}
