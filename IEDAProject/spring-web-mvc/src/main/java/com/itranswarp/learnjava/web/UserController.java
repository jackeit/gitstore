package com.itranswarp.learnjava.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.itranswarp.learnjava.entity.Diary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.service.UserService;

@Controller
public class UserController {

	public static final String KEY_USER = "__user__";
	public static final String KEY_CONTENT = "__content__";
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;

	@GetMapping("/")
	public ModelAndView index(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		Map<String, Object> model = new HashMap<>();
		if (user != null) {
			model.put("user",user);
		}

		return new ModelAndView("index.html", model);
	}

	@GetMapping("/register")
	public ModelAndView register() {
		return new ModelAndView("register.html");
	}

	@PostMapping("/register")
	public ModelAndView doRegister(@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("name") String name) {
		try {
			User user = userService.register(email, password, name);
			logger.info("user registered: {}", user.getEmail());
		} catch (RuntimeException e) {
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("email",email);
			hashMap.put("error","Register failed");
//			return new ModelAndView("register.html", Map.of("email", email, "error", "Register failed"));
			return new ModelAndView("register.html", hashMap);
		}
		return new ModelAndView("redirect:/signin");
	}

	@GetMapping("/signin")
	public ModelAndView signin(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		if (user != null) {
			return new ModelAndView("redirect:/profile");
		}
		return new ModelAndView("signin.html");
	}
	@GetMapping("/diarytitle")
	public ModelAndView diarytitle(HttpSession session){
		User user = (User) session.getAttribute(KEY_USER);
		if(user!=null){
			HashMap<String,List<Diary>> hashMap =new HashMap<>();
			List<Diary> diaryList = (List<Diary>) session.getAttribute(KEY_CONTENT);
			hashMap.put("diaryList", diaryList);
			return new ModelAndView("diarytitle.html",hashMap);
		}

		return new ModelAndView("redirect:/signin");
	}

	@PostMapping("/signin")
	public ModelAndView doSignin(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session) {
		try {
			User user = userService.signin(email, password);
			List<Diary> diaryList = userService.GetDiaryList(user.getName());
			session.setAttribute(KEY_USER, user);
			session.setAttribute(KEY_CONTENT,diaryList);
		} catch (RuntimeException e) {
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("email",email);
			hashMap.put("error","Signin failed");
//			return new ModelAndView("signin.html", Map.of("email", email, "error", "Signin failed"));
			return new ModelAndView("signin.html", hashMap);
		}
		return new ModelAndView("redirect:/profile");
	}

	@GetMapping("/profile")
	public ModelAndView profile(HttpSession session) {
		User user = (User) session.getAttribute(KEY_USER);
		if (user == null) {
			return new ModelAndView("redirect:/signin");
		}
//		return new ModelAndView("profile.html", Map.of("user", user));
		HashMap<String,User> hashMap =new HashMap<>();
		hashMap.put("user", user);
		return new ModelAndView("profile.html", hashMap);
	}

	@GetMapping("/signout")
	public String signout(HttpSession session) {
		session.removeAttribute(KEY_USER);
		return "redirect:/signin";
	}
	/*
	* 将修改完后的日记数据重新绑定到session，再回到日记简图*/
	@PostMapping("/adddiaryinfo")
	public ModelAndView adddiaryInfo(Diary diary,HttpSession session){
		User user = (User) session.getAttribute(KEY_USER);
		if(user!=null) {
			userService.insertDiary(diary);
			List<Diary> diaryList = userService.GetDiaryList(user.getName());
			session.setAttribute(KEY_CONTENT, diaryList);
			HashMap<String, List<Diary>> diaryHashMap = new HashMap<>();
			diaryHashMap.put("diaryList", diaryList);
			return new ModelAndView("diarytitle.html", diaryHashMap);
		}
		return new ModelAndView("redirect:/signin");
	}


	@RequestMapping("/changediary/{username}/{diarytitle}/{id}")
	public ModelAndView setDiary(@PathVariable String username,
						 @PathVariable String diarytitle,
						 @PathVariable long id) {
		Diary diary = userService.getDiaryByid(id);
		HashMap<String,Diary> diaryHashMap = new HashMap<>();
		diaryHashMap.put("diary",diary);
		return new ModelAndView("adddiary.html",diaryHashMap);
	}
}
