package com.project.study.controller;

import java.io.File;
import java.io.IOException;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.study.domain.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//스프링 시큐리티는 기본 세션말고도 시큐리티가 관리하는 세션이 존재한다.
//이 세션에 들어갈 수 있는 타입은 Authentication 객체뿐이다.
//Authentication에는 UserDetails와 OAuth2User 타입이 들어갈 수 있다.
//UserDetails 타입은 일반 로그인, OAuth2User 타입은 간편 로그인 일 경우
//OAuth 로그인을 해도 PrincipalDetails로 받을 수 있고
//일반 로그인을 해도 PrincipalDetails로 받을 수 있다.

@Slf4j
@Controller
@RequiredArgsConstructor
public class UsersController {

	@Value("${file.dir}")
	private String fileDir;
	
	private final UsersService usersService;
	
//	로그인 페이지 이동
	@GetMapping("/login")
	public String login() {
		return "users/login";
	}
	
//	간편 로그인 버튼 누르면 이동
	@GetMapping("/loginNickname")
	public String loginNickname(HttpServletRequest request, Model model) {
		System.out.println("세션확인"+request.getSession().getAttribute("users"));
		if(request.getSession().getAttribute("users") == null || request.getSession().getAttribute("users").equals("not")) {
//			로그인 없이 loginNickname 주소로 다이렉트로 접속한 경우
			return "redirect:/";
		}  else {
			if( ((Users) request.getSession().getAttribute("users")).getUsersnickname() == null ) {				
				
//			닉네임이 null임(처음 접속한 유저)
				model.addAttribute("users", request.getSession().getAttribute("users"));
				return "/users/loginNickname";
			} else {
				return "redirect:/";
			}
		}
	}
	
//	닉네임 중복 체크
	@ResponseBody
	@PostMapping(value="checkNick",  consumes = "application/json")
	public ResponseEntity<String> checkNick(@RequestBody Users users){
		boolean check = usersService.checkNick(users);
		log.info("닉네임 중복 체크 성공");
		return check ? new ResponseEntity<String>("fail",HttpStatus.OK) : new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
//	기술 선택 페이지 직접적으로 접근시 홈으로 보내줌
	@GetMapping("/loginSkill")
	public String loginSkill() {
		return "redirect:/";
	}
	
//	로그인 한 유저의 기술이 이미 있다면 홈으로 보내줌, 그렇지 않으면 기술 선택창으로 이동
	@PostMapping("/loginSkill")
	public String loginSkill(Model model, Users users) {
		System.out.println("유저 확인 : " + users);
		if(users.getSkill()!=null) {
			return "redirect:/";
		}
		model.addAttribute("skillList", usersService.skill());
		model.addAttribute("users", users);
		return "users/loginSkill";

	}
	
//	로그인 or 회원가입 성공 후 홈으로 이동
	@PostMapping("/loginFinish")
	public String loginFinish(Users users) {		
		usersService.joinOAuth(users);
		return "redirect:/";
	}
	
//	로그아웃 시켜줌
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
	    new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
	    return "redirect:/";
	  }

//	유저 프로필 수정 페이지 이동
	@GetMapping("/myInfo")
	public String myInfo(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Users users =  (Users) session.getAttribute("users");
		UpdateUsers updateUsers = new UpdateUsers();
		updateUsers.setUsersnumber(users.getUsersnumber());
		updateUsers.setSkill(users.getSkill());
		updateUsers.setSkillarry(users.getSkillarry());
		updateUsers.setUsersnickname(users.getUsersnickname());
		updateUsers.setUsername(users.getUsername());
		model.addAttribute("skillList", usersService.skill());
		model.addAttribute("updateUsers", updateUsers);
		return "users/myInfo";
	}
	
//	회원 탈퇴
	@GetMapping("/fire")
	public String fire(@RequestParam(required = false) String username, @RequestParam(required = false) String usersnickname) {
		usersService.fire(username, usersnickname);
		return "redirect:/?fire=t";
	}
	
//	유저 프로필 수정 완료
	@PostMapping("/myInfo")
	public String myInfo(Users users, HttpServletRequest request) {
		System.out.println("updateUsers : " + users);
		HttpSession session = request.getSession();
		usersService.updateInfo(users);
		session.setAttribute("users", usersService.getUsers(users.getUsersnumber()));
		return "redirect:/?update=t";
	}

	
	public String getFullPath(Users users, String orgName) {
		int idx = orgName.indexOf(".");
		String imgName = orgName.substring(idx);
		String fullPath = fileDir + users.getProviderid() + imgName;
		users.setImagename(users.getProviderid() + imgName);
		return fullPath;
	}
}
